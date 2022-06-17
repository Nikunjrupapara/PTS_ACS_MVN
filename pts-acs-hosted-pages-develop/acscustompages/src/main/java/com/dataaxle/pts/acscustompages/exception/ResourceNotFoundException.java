package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ResourceNotFoundException extends CustomPagesException {
	private static final long serialVersionUID = -9104883370135618296L;

	String uri;

	public ResourceNotFoundException(String uri) {
		super();
		this.uri = uri;
	}
}
