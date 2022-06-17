package com.yesmarketing.ptsacs.services.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ServicesTokenAuthentication implements Authentication {

	private static final long serialVersionUID = -485206746338739698L;

	private final String servicesToken;

	private final String path;

	public ServicesTokenAuthentication(String servicesToken,String path) {
		this.servicesToken = servicesToken;
		this.path = path;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return servicesToken;
	}

	@Override
	public Object getDetails() {
		return path;
	}

	@Override
	public Object getPrincipal() {
		return servicesToken;
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// do nothing here to make the authentication immutable
	}

	@Override
	public String getName() {
		return servicesToken;
	}
}
