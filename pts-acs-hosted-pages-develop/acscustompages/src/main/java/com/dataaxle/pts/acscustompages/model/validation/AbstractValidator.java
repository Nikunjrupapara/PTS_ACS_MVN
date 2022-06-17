package com.dataaxle.pts.acscustompages.model.validation;

import com.dataaxle.pts.acscustompages.service.validation.ErrorCode;
import com.dataaxle.pts.acscustompages.utils.MessageCodes;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.io.Serializable;

public abstract class AbstractValidator implements ValidationRule, Serializable {

	private static final long serialVersionUID = 4988377915370818091L;

	protected final String fieldName;

	protected final boolean required;

	protected final Integer minSize;

	protected final Integer maxSize;

	protected final Object[] errorArgs;

	public AbstractValidator(String fieldName, boolean required, Integer minSize, Integer maxSize) {
		this.fieldName = fieldName;
		this.required = required;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.errorArgs = new Object[] { fieldName };
	}

	@Deprecated
	protected void recordError(Errors errors, String messageCode) {
		String defaultMessage = messageCode.replace("message", fieldName).replaceAll("\\.", " ");
		errors.rejectValue(fieldName, messageCode, errorArgs, defaultMessage);
	}

	@Deprecated
	protected void checkRequired(Errors errors, String value) {
		if (required) {
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", messageCode("IsRequired"));
			if (StringUtils.isEmpty(value)) {
				recordError(errors, MessageCodes.FIELD_IS_REQUIRED);
			}
		}
	}

	protected ErrorCode checkRequired(String value) {
		if (required) {
			if (StringUtils.isEmpty(value)) {
				return ErrorCode.REQUIRED;
			}
		}
		return ErrorCode.VALID;
	}

	@Deprecated
	protected void checkMinSize(Errors errors, String value) {
		if (minSize != null && minSize > 0) {
			if (value.length() < minSize) {
				recordError(errors, MessageCodes.FIELD_TOO_SHORT);
			}
		}
	}

	protected ErrorCode checkMinSize(String value) {
		if (minSize != null && minSize > 0) {
			if (value.length() < minSize) {
				return ErrorCode.TOO_SHORT;
			}
		}
		return ErrorCode.VALID;
	}

	@Deprecated
	protected void checkMaxSize(Errors errors, String value) {
		if (maxSize != null && maxSize > 0) {
			if (value.length() > maxSize) {
				recordError(errors, MessageCodes.FIELD_TOO_LONG);
			}
		}
	}

	protected ErrorCode checkMaxSize(String value) {
		if (maxSize != null && maxSize > 0) {
			if (value.length() > maxSize) {
				return ErrorCode.TOO_LONG;
			}
		}
		return ErrorCode.VALID;
	}

	public String getFieldName() {
		return fieldName;
	}

	protected ValidationResult getResult(String value, ErrorCode errorCode) {
		return getResult(fieldName, value, errorCode);
	}

	protected ValidationResult getResult(String fieldName, String value, ErrorCode errorCode) {
		return new ValidationResult(fieldName, value, errorCode == ErrorCode.VALID, errorCode);
	}

	protected boolean isValid(ErrorCode errorCode) {
		return errorCode == ErrorCode.VALID;
	}
}
