package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class AppPageNotFoundException extends CustomPagesException {
	private static final long serialVersionUID = -3886079716415928983L;

	String name;

	public AppPageNotFoundException(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getMessage() {
		return String.format("Page %s not found!", name);
	}
}
