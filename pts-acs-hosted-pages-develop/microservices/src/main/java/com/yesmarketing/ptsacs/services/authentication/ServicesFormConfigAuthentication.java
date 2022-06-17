package com.yesmarketing.ptsacs.services.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ServicesFormConfigAuthentication implements Authentication {

	private static final long serialVersionUID = -2303563563307943218L;

	private final FormUserDetails formUserDetails;

	private final boolean authenticated;

	public ServicesFormConfigAuthentication(FormUserDetails formUserDetails) {
		this.formUserDetails = formUserDetails;
		this.authenticated = formUserDetails.isAccountNonExpired() && formUserDetails.isAccountNonLocked() &&
								 formUserDetails.isCredentialsNonExpired() && formUserDetails.isEnabled();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return formUserDetails.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return formUserDetails.getPassword();
	}

	@Override
	public Object getDetails() {
		return formUserDetails;
	}

	@Override
	public Object getPrincipal() {
		return formUserDetails.getFormConfig();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// do nothing here to make the Authentication immutable
	}

	@Override
	public String getName() {
		return formUserDetails.getFormConfig().getCode();
	}
}
