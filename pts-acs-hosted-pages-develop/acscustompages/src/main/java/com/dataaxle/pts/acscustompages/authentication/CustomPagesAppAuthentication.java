package com.dataaxle.pts.acscustompages.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomPagesAppAuthentication implements Authentication {

	private static final long serialVersionUID = -4769277956949798466L;

	private final CustomPagesRequest customPagesRequest;

	public CustomPagesAppAuthentication(CustomPagesRequest customPagesRequest) {
		this.customPagesRequest = customPagesRequest;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return customPagesRequest;
	}

	@Override
	public Object getDetails() {
		return customPagesRequest;
	}

	@Override
	public Object getPrincipal() {
		return customPagesRequest;
	}

	@Override
	public boolean isAuthenticated() {
		return customPagesRequest.isAuthenticated();
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {
		// do nothing here so that the Authentication object is immutable
	}

	@Override
	public String getName() {
		return null;
	}
}
