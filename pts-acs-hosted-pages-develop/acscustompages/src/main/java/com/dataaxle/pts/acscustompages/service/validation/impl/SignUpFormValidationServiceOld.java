package com.dataaxle.pts.acscustompages.service.validation.impl;

import com.dataaxle.pts.acscustompages.model.PageField;
import com.dataaxle.pts.acscustompages.model.form.ProfileDetails;
import com.dataaxle.pts.acscustompages.model.form.SignUpFormBean;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.validation.ValidationServiceOld;
import com.dataaxle.pts.acscustompages.utils.MessageCodes;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;

@Deprecated
public class SignUpFormValidationServiceOld implements ValidationServiceOld<SignUpFormBean> {

	private final List<Validator> validators;

	public SignUpFormValidationServiceOld(List<Validator> validators) {
		this.validators = validators;
	}

	@Override
	public void validate(SignUpFormBean formBean, Errors errors) {
		ProfileDetails subject = formBean.getSubject();
		validators.forEach(validator -> validator.validate(subject, errors));
	}

	@Override
	public void validate(Map<String, Object> fields, Errors errors) {
		validators.forEach(validator -> validator.validate(fields, errors));
		if (errors.hasFieldErrors()) {
			errors.reject(MessageCodes.ERRORS_FOUND, "Please correct the highlighted errors.");
		}
	}

	//@Override
	public ValidationResults validate(List<PageField> fields, Map<String, Object> fieldValues) {
		return null;
	}
}
