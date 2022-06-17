package com.dataaxle.pts.acscustompages.exception;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.AuthenticationException;

@EqualsAndHashCode(callSuper = true)
@Value
public class CustomPagesAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 4635787229420239769L;

	CustomPagesRequest customPagesRequest;

	public CustomPagesAuthenticationException(CustomPagesRequest customPagesRequest) {
		super(customPagesRequest.getAppDetails().getAppId().toString());
		this.customPagesRequest = customPagesRequest;
	}
}
