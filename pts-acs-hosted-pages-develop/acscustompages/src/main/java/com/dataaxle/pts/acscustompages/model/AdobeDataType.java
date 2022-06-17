package com.dataaxle.pts.acscustompages.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AdobeDataType {

	BOOLEAN("boolean"),
	BYTE("byte"),
	DATE("date"),
	DATETIME("datetime"),
	DOUBLE("double"),
	FLOAT("float"),
	INT64("int64"),
	LONG("long"),
	MEMO("memo"),
	SHORT("short"),
	STRING("string"),
	UUID("uuid");

	private final String displayName;

	AdobeDataType(String displayName) {
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

	@JsonCreator
	public static AdobeDataType fromValue(String name) {
		for (AdobeDataType type : AdobeDataType.values()) {
			if (type.getDisplayName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
