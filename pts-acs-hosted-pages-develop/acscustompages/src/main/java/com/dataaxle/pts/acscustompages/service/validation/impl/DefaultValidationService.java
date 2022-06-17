package com.dataaxle.pts.acscustompages.service.validation.impl;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.RecaptchaService;
import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.dataaxle.pts.acscustompages.service.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultValidationService implements ValidationService {

	private final RecaptchaService recaptchaService;

	public DefaultValidationService(RecaptchaService recaptchaService) {
		this.recaptchaService = recaptchaService;
	}

	@Override
	public ValidationResults validate(List<PageField<?>> fields, Map<String, Object> fieldValues) {
		ValidationResults validationResults = new ValidationResults();
		Map<String, ValidationResult> results = fields.stream()
													.filter(PageField::isValidated)
													.map(pageField -> pageField.getValidationRule().validate(fieldValues))
													.collect(Collectors.toMap(ValidationResult::getFieldName,
														validationResult -> validationResult, (a, b) -> a));
		validationResults.addResult(results);
		if (validationResults.hasErrors()) {
			validationResults.addResult(GLOBAL_ERROR, new ValidationResult(GLOBAL_ERROR, "", false,
				ErrorCode.ERRORS_FOUND));
		}
		return validationResults;
	}

	@Override
	public ValidationResults validate(CustomPagesRequest customPagesRequest, DynamicFormBean dynamicFormBean) {
		ValidationResults validationResults = new ValidationResults();
		List<PageField<?>> fields = customPagesRequest.getFieldsOnPage();
		Map<String, Object> fieldValues = dynamicFormBean.getItems();
		Map<String, ValidationResult> results = fields.stream()
													.filter(PageField::isValidated)
													.map(pageField -> pageField.getValidationRule().validate(fieldValues))
													.collect(Collectors.toMap(ValidationResult::getFieldName,
														validationResult -> validationResult, (a, b) -> a));
		validationResults.addResult(results);
		if (customPagesRequest.useRecaptcha()) {
			validationResults.addResult(recaptchaService.processResponse(customPagesRequest));
		}
		if (validationResults.hasErrors()) {
			validationResults.addResult(GLOBAL_ERROR, new ValidationResult(GLOBAL_ERROR, "", false,
				ErrorCode.ERRORS_FOUND));
		}
		return validationResults;
	}
}
