package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class DomainNotFoundException extends CustomPagesException {

	private static final long serialVersionUID = 7456943932139129573L;

	String domain;

	public DomainNotFoundException(String domain) {
		super();
		this.domain = domain;
	}

	@Override
	public String getMessage() {
		return String.format("Domain %s not found", domain);
	}
}
