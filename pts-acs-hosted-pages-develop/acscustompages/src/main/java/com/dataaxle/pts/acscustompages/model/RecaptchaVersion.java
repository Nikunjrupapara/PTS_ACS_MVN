package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public enum RecaptchaVersion {
	@JsonProperty(RecaptchaVersion.V2_NAME)
	V2(RecaptchaVersion.V2_NAME),
	@JsonProperty(RecaptchaVersion.V3_NAME)
	V3(RecaptchaVersion.V3_NAME);

	private final String name;

	RecaptchaVersion(String name) {
		this.name = name;
	}

	public static RecaptchaVersion of(String name) {
		return Arrays.stream(values())
			.filter(value -> value.name.equals(name))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(String.format("Unknown Recaptcha Version '%s'", name)));
	}

	@Override
	public String toString() {
		return name;
	}

	public static final String V2_NAME = "v2";

	public static final String V3_NAME = "v3";
}
