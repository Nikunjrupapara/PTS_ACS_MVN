package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	NOVALUE(""),
	FEMALE("FEMALE"),
	MALE("MALE"),
	UNKNOWN("UNKNOWN");

	String name;

	Gender(String name) {
		this.name = name;
	}

	@JsonValue
	public String getName() { return name; }

	@JsonCreator
	public static Gender fromString(String name) {
		for (Gender gender : values()) {
			if (gender.name().equals(name)) {
				return gender;
			}
		}
		return null;
	}
}
