package com.yesmarketing.ptsacs.services.authentication;

import com.yesmarketing.ptsacs.services.model.FormConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

public class FormUserDetails implements UserDetails {

	private static final long serialVersionUID = 5698277695629820376L;

	private final FormConfig formConfig;

	public FormUserDetails(FormConfig formConfig) {
		this.formConfig = formConfig;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return formConfig.getAuthorities();
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return formConfig.getUuid();
	}

	@Override
	public boolean isAccountNonExpired() {
		LocalDateTime now = LocalDateTime.now();
		return formConfig.getEffectiveFrom().isBefore(now) && formConfig.getEffectiveTo().isAfter(now);
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled() && isAccountNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return formConfig.isEnabled();
	}

	public FormConfig getFormConfig() {
		return formConfig;
	}
}
