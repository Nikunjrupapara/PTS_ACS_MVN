package com.dataaxle.pts.acscustompages.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

public class CustomPagesRequestAuthentication implements Authentication {

	private static final long serialVersionUID = -6574894318132398365L;

	private final HttpServletRequest request;

	public CustomPagesRequestAuthentication(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return request;
	}

	@Override
	public Object getDetails() {
		return request;
	}

	@Override
	public Object getPrincipal() {
		return request;
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {
		// do nothing here to make the authentication immutable
	}

	@Override
	public String getName() {
		return this.getClass().getCanonicalName();
	}
}
