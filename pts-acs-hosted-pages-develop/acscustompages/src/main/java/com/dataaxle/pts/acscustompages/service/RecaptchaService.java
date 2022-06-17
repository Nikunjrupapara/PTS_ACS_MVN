package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;

public interface RecaptchaService {

	String RECAPTCHA_FIELD_NAME = "recaptcha";

	ValidationResult processResponse(CustomPagesRequest customPagesRequest);

}
