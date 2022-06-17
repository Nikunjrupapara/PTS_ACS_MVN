package com.dataaxle.pts.acscustompages.model;

public enum Environment {
	DEVELOPMENT("development"),
	PRODUCTION("production"),
	QA("qa");

	private final String name;

	Environment(String name) {
		this.name = name;
	}

	public static Environment getInstance(Domain domain) {
		String domainName = domain.getName();
		if (domainName.startsWith("test.") || domainName.contains(".testlocal.")) {
			return DEVELOPMENT;
		}
		if (domainName.contains(".ptsacs.data-axle.com")) {
			return QA;
		}
		return PRODUCTION;
	}

	public String getName() {
		return name;
	}

}
