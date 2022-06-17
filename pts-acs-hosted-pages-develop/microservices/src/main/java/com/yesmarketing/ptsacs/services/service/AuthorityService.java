package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;

import java.util.List;

public interface AuthorityService {

	List<ServicesGrantedAuthority> getAuthorities();

	ServicesGrantedAuthority getAuthority(String authority);
}
