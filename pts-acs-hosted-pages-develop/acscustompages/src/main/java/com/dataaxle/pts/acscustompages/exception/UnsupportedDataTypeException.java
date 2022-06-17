package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class UnsupportedDataTypeException extends CustomPagesException {

	private static final long serialVersionUID = -6562127186319343960L;

	String displayName;

	public UnsupportedDataTypeException(String displayName) {
		super();
		this.displayName = displayName;
	}

	@Override
	public String getMessage() {
		return displayName;
	}
}
