package com.dataaxle.pts.acscustompages.controller;

import com.dataaxle.pts.acscustompages.authentication.CurrentApp;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.GetProfileCustomerIdService;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.RecaptchaService;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Throwables;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = ControllerUtils.POST_PROFILE_PATH)
public class ProfileHttpPost {

	private final GetProfileCustomerIdService getProfileCustomerIdService;

	private final ProfileCompositeService profileCompositeService;

	private final ValidationService validationService;

	@Autowired
	public ProfileHttpPost(GetProfileCustomerIdService getProfileCustomerIdService,
						   ProfileCompositeService profileCompositeService, ValidationService validationService) {
		this.getProfileCustomerIdService = getProfileCustomerIdService;
		this.profileCompositeService = profileCompositeService;
		this.validationService = validationService;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpPostResponse> handleError(Exception ex) {
		LOG.error("An error occurred!", ex);
		String msg = "An error occurred while processing the request.  Please try again and if the error persists contact your support representative.";
		HttpPostResponse response = HttpPostResponse.builder().withMessage(msg).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@CrossOrigin
	@PostMapping(value = ControllerUtils.MULTIPLE_PATH_SEGMENTS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpPostResponse> submit(@CurrentApp CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean) {
		ResponseEntity<HttpPostResponse> result;
		Map<String, Object> formInput = formInputBean.getItems();
		ControllerUtils.logFields(LOG, "formInputBean", formInput);

		ValidationResults validationResults = validationService.validate(customPagesRequest, formInputBean);
		RecaptchaResponse recaptchaResponse = RecaptchaResponse.of(validationResults);
		if (validationResults.hasErrors()) {
			HttpPostResponse response;
			HttpPostResponse.Builder responseBuilder = HttpPostResponse.builder();
			validationResults.getResults().values().stream()
				.filter(error -> !error.getFieldName().equals(ValidationService.GLOBAL_ERROR))
				.filter(error -> !error.getFieldName().equals(RecaptchaService.RECAPTCHA_FIELD_NAME))
				.map(PostValidationResult::of)
				.forEach(responseBuilder::withError);
			responseBuilder.withMessage("Errors found in form input");
			responseBuilder.withRecaptchaResponse(recaptchaResponse);
			response = responseBuilder.build();
			result = ResponseEntity.badRequest().body(response);
			LOG.debug("result: {}", result);
			return result;
		}

		List<String> fieldsToSubmit = customPagesRequest.getFieldsToSubmit();

		GetProfileRequest getProfileRequest = new GetProfileRequest();
		formInputBean.getItems().entrySet().stream()
			.filter(entry -> fieldsToSubmit.contains(entry.getKey()))
			.forEach(entry -> {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				LOG.debug("searchItem: name: {}, value: {}", key, value);
				getProfileRequest.addParameter(key, value);
			});

		try {
			ProfileResponseWrapper profileResponseWrapper =
				getProfileCustomerIdService.getProfile(customPagesRequest.getAppDetails(), getProfileRequest);
			ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, formInputBean, profileResponseWrapper.getProfileResponse());
			if (profileRequest.hasActions()) {
				try {
					profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
					if (profileResponseWrapper.isAllSuccess()) {
						return success(profileResponseWrapper, recaptchaResponse);
					}
					result = error(profileResponseWrapper, recaptchaResponse);
					LOG.debug("result: {}", result);
					return result;
				} catch (Exception ex) {
					LOG.error("An error occurred!", ex);
					result = error(ex, recaptchaResponse);
					LOG.debug("result: {}", result);
					return result;
				}
			}
			result = noActions();
			LOG.debug("result: {}", result);
			return result;
		} catch (ResourceNotFoundException e) {
			ProfileRequest profileRequest = ControllerUtils.prepareRequest(customPagesRequest, formInputBean,
				(ProfileResponse) null);
			ProfileResponseWrapper profileResponseWrapper;
			if (profileRequest.hasActions()) {
				try {
					profileResponseWrapper = profileCompositeService.process(customPagesRequest.getAppDetails(), profileRequest);
					if (profileResponseWrapper.isAllSuccess()) {
						return success(profileResponseWrapper, recaptchaResponse);
					}
					result = error(profileResponseWrapper, recaptchaResponse);
					LOG.debug("result: {}", result);
					return result;
				} catch (Exception ex) {
					LOG.error("An error occurred!", ex);
					result = error(ex, recaptchaResponse);
					LOG.debug("result: {}", result);
					return result;
				}
			}
			result = noActions();
			LOG.debug("result: {}", result);
			return result;
		} catch (Exception e) {
			LOG.error("An error occurred!", e);
			result = error(e, recaptchaResponse);
			LOG.debug("result: {}", result);
			return result;
		}
	}

	private ResponseEntity<HttpPostResponse> success(ProfileResponseWrapper profileResponseWrapper,
													 RecaptchaResponse recaptchaResponse) {
		ProcessingAction processingAction = profileResponseWrapper.getProfileRequestResult().getProfileAction();
		HttpStatus status = processingAction == ProcessingAction.CREATE_PROFILE ? HttpStatus.CREATED : HttpStatus.OK;
		HttpPostResponse response = HttpPostResponse.builder().withMessage("Success")
			.withRecaptchaResponse(recaptchaResponse).build();
		return ResponseEntity.status(status).body(response);
	}

	private ResponseEntity<HttpPostResponse> error(Throwable ex, RecaptchaResponse recaptchaResponse) {
		String message = Throwables.getRootCause(ex).getMessage();
		HttpPostResponse response = HttpPostResponse.builder().withMessage(message)
			.withRecaptchaResponse(recaptchaResponse).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private ResponseEntity<HttpPostResponse> error(ProfileResponseWrapper profileResponseWrapper,
												   RecaptchaResponse recaptchaResponse) {
		String msg = profileResponseWrapper.getProfileRequestResult().getErrorMessage();
		LOG.error("An error occurred! {}", msg);
		HttpPostResponse response = HttpPostResponse.builder().withMessage(msg)
			.withRecaptchaResponse(recaptchaResponse).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private ResponseEntity<HttpPostResponse> noActions() {
		HttpPostResponse response = HttpPostResponse.builder().withMessage("No actions performed!").build();
		return ResponseEntity.ok().body(response);
	}

	@Value
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class PostValidationResult {

		String fieldName;

		ErrorCode errorCode;

		String value;

		protected static PostValidationResult of(ValidationResult result) {
			return new PostValidationResult(result.getFieldName(), result.getErrorCode(), result.getValue());
		}
	}

	@Value
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class HttpPostResponse {

		String message;

		List<PostValidationResult> errors;

		RecaptchaResponse recaptcha;

		private HttpPostResponse(Builder builder) {
			this.message = builder.message;
			this.errors = builder.errors;
			this.recaptcha = builder.recaptchaResponse;
		}

		protected static Builder builder() { return new Builder(); }

		protected static class Builder {
			private Builder() {}

			private String message;

			private final List<PostValidationResult> errors = new ArrayList<>();

			private RecaptchaResponse recaptchaResponse;

			protected Builder withMessage(String message) {
				this.message = message;
				return this;
			}

			protected Builder withErrors(List<PostValidationResult> errors) {
				this.errors.addAll(errors);
				return this;
			}

			protected Builder withError(PostValidationResult error) {
				this.errors.add(error);
				return this;
			}

			protected Builder withRecaptchaResponse(RecaptchaResponse recaptchaResponse) {
				this.recaptchaResponse = recaptchaResponse;
				return this;
			}

			protected HttpPostResponse build() {
				return new HttpPostResponse(this);
			}
		}
	}

	@Value
	public static class RecaptchaResponse {
		boolean success;

		String action;

		double score;

		List<String> errorCodes;

		public static RecaptchaResponse of(ValidationResults validationResults) {
			Optional<ValidationResult> resultOptional = validationResults.getResults().values().stream()
				.filter(result -> result.getFieldName().equals(RecaptchaService.RECAPTCHA_FIELD_NAME))
				.findFirst();

			if (resultOptional.isEmpty()) {
				return null;
			}
			ValidationResult result = resultOptional.get();
			if (result.getErrorCode() == ErrorCode.RECAPTCHA_INVALID_TOKEN) {
				return new RecaptchaResponse(false, "", 0, Collections.singletonList("invalid-token"));
			}
			String[] parts = resultOptional.get().getValue().split(";");
			List<String> errorCodes;
			if (parts.length > 3) {
				errorCodes = Arrays.asList(parts[3].split(","));
			} else {
				errorCodes = Collections.emptyList();
			}
			if (result.isNotValid()) {
				return new RecaptchaResponse(false, parts[1], Double.parseDouble(parts[2]),
					List.of(result.getErrorCode().toString()));
			} else {
				return new RecaptchaResponse(Boolean.parseBoolean(parts[0]), parts[1], Double.parseDouble(parts[2]), errorCodes);
			}

		}
	}
}
