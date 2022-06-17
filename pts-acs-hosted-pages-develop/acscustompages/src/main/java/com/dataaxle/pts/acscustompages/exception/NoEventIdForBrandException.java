package com.dataaxle.pts.acscustompages.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class NoEventIdForBrandException extends CustomPagesException {

	private static final long serialVersionUID = -1219556841026333331L;

	String brand;

	@Override
	public String getMessage() {
		return String.format("No eventId found for brand %s", brand);
	}
}
