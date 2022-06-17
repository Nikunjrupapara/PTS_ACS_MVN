package com.yesmarketing.ptsacs.common.exception;

public class NoCustomerIdDefinitionException extends RuntimeException {

	private final String company;

	public NoCustomerIdDefinitionException(String company) {
		super();
		this.company = company;
	}

	public String getCompany() {
		return company;
	}
}
