package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ServerErrorException extends CustomPagesException {
	private static final long serialVersionUID = 8138018223435233547L;

	public ServerErrorException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
