package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class AppDetailsNotFoundException extends CustomPagesException {
	private static final long serialVersionUID = -3786066720025835277L;

	String company;

	String contextPath;

	public AppDetailsNotFoundException(String company, String contextPath) {
		super();
		this.company = company;
		this.contextPath = contextPath;
	}

	@Override
	public String getMessage() {
		return String.format("AppDetails not found for company %s and path %s", company, contextPath);
	}
}
