package com.dataaxle.pts.acscustompages.service.validation;

import com.dataaxle.pts.acscustompages.utils.MessageCodes;

public enum ErrorCode {
	ERRORS_FOUND("true", MessageCodes.ERRORS_FOUND),
	INVALID("invalid", MessageCodes.FIELD_HAS_INVALID_FORMAT),
	NO_MATCH("nomatch", MessageCodes.FIELDS_DONT_MATCH),
	RECAPTCHA_ERROR("recaptcha.error", MessageCodes.RECAPTCHA_ERROR),
	RECAPTCHA_INVALID_TOKEN("recaptcha.invalid.token", MessageCodes.RECAPTCHA_INVALID_TOKEN),
	RECAPTCHA_LOW_SCORE("recaptcha.lowScore", MessageCodes.RECAPTCHA_LOW_SCORE),
	RECAPTCHA_WRONG_ACTION("recaptcha.wrongAction", MessageCodes.RECAPTCHA_WRONG_ACTION),
	REQUIRED("required", MessageCodes.FIELD_IS_REQUIRED),
	TOO_HIGH("toohigh", MessageCodes.FIELD_TOO_HIGH),
	TOO_LONG("toolong", MessageCodes.FIELD_TOO_LONG),
	TOO_LOW("toolow", MessageCodes.FIELD_TOO_LOW),
	TOO_SHORT("tooshort", MessageCodes.FIELD_TOO_SHORT),
	VALID("valid", "");

	private final String code;

	private final String messageCode;

	ErrorCode(String code, String messageCode) {
		this.code = code;
		this.messageCode = messageCode;
	}

	public String getCode() {
		return code;
	}

	public String getMessageCode() {
		return messageCode;
	}
}
