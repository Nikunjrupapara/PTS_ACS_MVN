package com.yesmarketing.ptsacs.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HealthCheckException extends RuntimeException {

	public HealthCheckException(String message) {
		super(message);
	}

	public HealthCheckException(String message, Throwable cause) {
		super(message, cause);
	}
}
