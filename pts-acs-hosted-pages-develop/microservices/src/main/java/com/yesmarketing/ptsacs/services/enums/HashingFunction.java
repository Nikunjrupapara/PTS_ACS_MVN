package com.yesmarketing.ptsacs.services.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum HashingFunction {
	MD5("md5"),
	SHA256("sha256"),
	SHA512("sha512");

	@Getter
	@JsonValue
	private final String name;

	@JsonCreator
	public static HashingFunction fromString(String name) {
		return Arrays.stream(values())
			.filter(val -> val.getName().equals(name))
			.findFirst()
			.orElse(null);
	}
}
