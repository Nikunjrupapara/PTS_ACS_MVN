package com.dataaxle.pts.acscustompages.exception;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class CustomPagesRequestException extends CustomPagesException {

	private static final long serialVersionUID = -6366087759938358375L;

	CustomPagesRequest customPagesRequest;

	public CustomPagesRequestException(CustomPagesRequest customPagesRequest, Throwable cause) {
		super(cause.getMessage(), cause);
		this.customPagesRequest = customPagesRequest;
	}
}
