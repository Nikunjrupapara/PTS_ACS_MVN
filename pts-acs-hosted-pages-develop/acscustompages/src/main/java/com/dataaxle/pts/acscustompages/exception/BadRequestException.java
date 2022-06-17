package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class BadRequestException extends CustomPagesException {
	private static final long serialVersionUID = 8138018223435233547L;

	String responseMessage;

	public BadRequestException(String responseMessage, Throwable cause) {
		super(cause.getMessage(), cause);
		this.responseMessage = responseMessage;
	}

	@Override
	public String getMessage() {
		return responseMessage;
	}
}
