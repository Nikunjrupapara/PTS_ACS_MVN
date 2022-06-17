package com.dataaxle.pts.acscustompages.exception;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import lombok.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomPagesException extends RuntimeException {
	private static final long serialVersionUID = -5711253898142626607L;

	CustomPagesException() {

	}

	CustomPagesException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
