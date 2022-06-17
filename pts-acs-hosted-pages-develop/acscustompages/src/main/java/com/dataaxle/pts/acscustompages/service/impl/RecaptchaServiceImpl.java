package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.GoogleResponse;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.service.RecaptchaService;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.regex.Pattern;

@Service
@Slf4j
@ConditionalOnProperty(value = "microservices.useStubbedServices.recaptchaService", havingValue = "false", matchIfMissing = true)
public class RecaptchaServiceImpl implements RecaptchaService {

	/*
	Based on the code from here: https://www.baeldung.com/spring-security-registration-captcha
	 */

	private final RestOperations restOperations;

	private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

	private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";

	public RecaptchaServiceImpl(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	@Override
	public ValidationResult processResponse(CustomPagesRequest customPagesRequest) {
		String recaptchaResponse = customPagesRequest.getRecaptchaResponse();
		LOG.debug("Validating recaptcha response: {}", recaptchaResponse);
		if (!responseSanityCheck(recaptchaResponse)) {
			LOG.error("Response has invalid format!");
			return ValidationResult.invalid(RECAPTCHA_FIELD_NAME, recaptchaResponse, ErrorCode.RECAPTCHA_INVALID_TOKEN);
		}
		AppPage page = customPagesRequest.getCurrentPage();
		String ipAddress = customPagesRequest.getIpAddress();
		URI verifyUri = URI.create(String.format(VERIFY_URL, page.getRecaptchaSecret(), recaptchaResponse, ipAddress));
		LOG.debug("Validation URI: {}", verifyUri);
		GoogleResponse googleResponse = restOperations.getForObject(verifyUri, GoogleResponse.class);
		if (googleResponse == null) {
			LOG.debug("googleResponse is null!");
			return ValidationResult.invalid(RECAPTCHA_FIELD_NAME, GoogleResponse.responseStringNull(), ErrorCode.RECAPTCHA_ERROR);
		}
		LOG.debug("Validation response: {}", googleResponse);
		if (page.isRecaptchaV2()) {
			return googleResponse.isSuccess() ? ValidationResult.valid(RECAPTCHA_FIELD_NAME, googleResponse.responseString()) :
					   ValidationResult.invalid(RECAPTCHA_FIELD_NAME, googleResponse.responseString(), ErrorCode.RECAPTCHA_ERROR);
		}
		// Additional validation for Recaptcha V3
		LOG.debug("Page Recaptcha parameters: action: {}, score: {}", page.getRecaptchaAction(), page.getRecaptchaScore());
		boolean valid = false;
		ErrorCode errorCode = null;
		if (!page.getRecaptchaAction().equals(googleResponse.getAction())) {
			errorCode = ErrorCode.RECAPTCHA_WRONG_ACTION;
		} else if (googleResponse.getScore() < page.getRecaptchaScore()) {
			errorCode = ErrorCode.RECAPTCHA_LOW_SCORE;
		} else if (!googleResponse.isSuccess()) {
			errorCode = ErrorCode.RECAPTCHA_ERROR;
		} else {
			valid = true;
		}
		if (!valid) {
			return ValidationResult.invalid(RECAPTCHA_FIELD_NAME, googleResponse.responseString(), errorCode);
		}
		return ValidationResult.valid(RECAPTCHA_FIELD_NAME, googleResponse.responseString());
	}

	private boolean responseSanityCheck(String response) {
		return StringUtils.hasText(response) && RESPONSE_PATTERN.matcher(response).matches();
	}
}
