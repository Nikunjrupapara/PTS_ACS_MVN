package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.service.RecaptchaService;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "microservices.useStubbedServices.recaptchaService", havingValue = "true")
public class StubbedRecaptchaService implements RecaptchaService {

	@Override
	public ValidationResult processResponse(CustomPagesRequest customPagesRequest) {
		return new ValidationResult("recaptcha", "true;submit;0.99;", true, ErrorCode.VALID);
	}
}
