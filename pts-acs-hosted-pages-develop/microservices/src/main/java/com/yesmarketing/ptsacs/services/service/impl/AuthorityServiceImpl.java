package com.yesmarketing.ptsacs.services.service.impl;

import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;
import com.yesmarketing.ptsacs.services.service.AuthorityService;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	private static final List<ServicesGrantedAuthority> authorities = ServicesConstants.SERVICES_AUTHORITIES;

	@Override
	public List<ServicesGrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public ServicesGrantedAuthority getAuthority(String authority) {
		return authorities
				   .stream()
				   .filter(item -> item.getAuthority().equals(authority))
				   .findFirst()
				   .orElse(null);
	}
}
