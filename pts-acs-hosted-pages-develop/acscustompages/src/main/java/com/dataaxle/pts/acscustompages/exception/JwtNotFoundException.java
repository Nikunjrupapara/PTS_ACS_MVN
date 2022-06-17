package com.dataaxle.pts.acscustompages.exception;


import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class JwtNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 4290565684441594473L;

	String company;

	String contextPath;

	@Override
	public String getMessage() {
		return String.format("No JWT found for company %s and contextPath %s", company, contextPath);
	}
}
