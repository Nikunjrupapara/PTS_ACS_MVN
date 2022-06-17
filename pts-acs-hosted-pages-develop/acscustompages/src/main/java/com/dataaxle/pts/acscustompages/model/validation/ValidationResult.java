package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import lombok.Value;

import java.io.Serializable;

@Value
public class ValidationResult implements Serializable {

	private static final long serialVersionUID = -712627503590860898L;

	String fieldName;

	String value;

	boolean valid;

	ErrorCode errorCode;

	public boolean isNotValid() {
		return !valid;
	}

	public static ValidationResult valid(String fieldName, String value) {
		return new ValidationResult(fieldName, value, true, ErrorCode.VALID);
	}

	public static ValidationResult invalid(String fieldName, String value, ErrorCode errorCode) {
		return new ValidationResult(fieldName, value, false, errorCode);
	}
}
