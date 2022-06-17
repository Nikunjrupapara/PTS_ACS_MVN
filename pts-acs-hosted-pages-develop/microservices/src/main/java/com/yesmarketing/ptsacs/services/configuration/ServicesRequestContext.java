package com.yesmarketing.ptsacs.services.configuration;

@Deprecated
public class ServicesRequestContext {

	private String company;

	public void clear() {
		this.company = null;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
