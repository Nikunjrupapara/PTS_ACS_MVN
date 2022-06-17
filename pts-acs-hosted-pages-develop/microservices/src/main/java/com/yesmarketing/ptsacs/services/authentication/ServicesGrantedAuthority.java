package com.yesmarketing.ptsacs.services.authentication;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.StringJoiner;

public class ServicesGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 5034451207283485827L;

	private String authority;

	private String description;

	public ServicesGrantedAuthority() {
	}

	public ServicesGrantedAuthority(String authority, String description) {
		this.authority = authority.toUpperCase();
		this.description = description;
	}

	public void setAuthority(String authority) {
		this.authority = authority.toUpperCase();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServicesGrantedAuthority authority1 = (ServicesGrantedAuthority) o;
		return authority.equals(authority1.authority);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authority);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ServicesGrantedAuthority.class.getSimpleName() + "[", "]")
				   .add("authority='" + authority + "'")
				   .add("description='" + description + "'")
				   .toString();
	}
}
