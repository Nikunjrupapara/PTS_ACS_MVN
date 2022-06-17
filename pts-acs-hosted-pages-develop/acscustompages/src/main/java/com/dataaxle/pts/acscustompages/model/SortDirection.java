package com.dataaxle.pts.acscustompages.model;

import java.util.Arrays;

public enum SortDirection {
	ASCENDING("ascending", ""),
	DESCENDING("descending", " desc");

	final String code;

	final String acsValue;

	SortDirection(String code, String acsValue) {
		this.code = code;
		this.acsValue = acsValue;
	}

	public String value(String name) { return String.format("%s %s", name, acsValue); }

	public static SortDirection of(String name) {
		return Arrays.stream(values())
			.filter(val -> val.code.equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(String.format("Unknown sort direction '%s'", name)));
	}
}
