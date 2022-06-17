package com.yesmarketing.ptsacs.services.authentication;

import com.yesmarketing.ptsacs.admin.service.FormConfigService;
import com.yesmarketing.ptsacs.common.exception.FormConfigNotFoundException;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FormUserDetailsService implements UserDetailsService {

	private final FormConfigService formConfigService;

	public FormUserDetailsService(FormConfigService formConfigService) {
		this.formConfigService = formConfigService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			FormConfig formConfig = formConfigService.getFormConfig(username);
			return new FormUserDetails(formConfig);
		} catch (FormConfigNotFoundException e) {
			String msg = String.format("No FormConfig found for UUID %s", e.getUuid());
			LOG.error(msg, e);
			throw new BadCredentialsException(msg, e);
		}
	}
}
