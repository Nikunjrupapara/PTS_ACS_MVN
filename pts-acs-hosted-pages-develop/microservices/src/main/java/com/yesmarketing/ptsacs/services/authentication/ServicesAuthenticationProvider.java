package com.yesmarketing.ptsacs.services.authentication;

import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.model.FormJsonWebToken;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class ServicesAuthenticationProvider implements AuthenticationProvider {

	private final FormJwtTokenService formJwtTokenService;

	private final FormUserDetailsService formUserDetailsService;

	public ServicesAuthenticationProvider(FormJwtTokenService formJwtTokenService, FormUserDetailsService formUserDetailsService) {
		this.formJwtTokenService = formJwtTokenService;
		this.formUserDetailsService = formUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String servicesToken = authentication.getName();
		LOG.debug("Authenticating token: {}", servicesToken);
		try {
			TokenDetail tokenDetail = formJwtTokenService.validateToken(servicesToken);
			LOG.debug("Validated token - TokenDetail: {}", tokenDetail);
			FormClaim formClaim = tokenDetail.getFormClaim();
			String formUuid = formClaim.getUuid();
			UserDetails userDetails = formUserDetailsService.loadUserByUsername(formUuid);
			if (userDetails instanceof FormUserDetails) {
				FormUserDetails fud = (FormUserDetails) userDetails;
				FormConfig formConfig = fud.getFormConfig();
				LOG.debug("Form Details: uuid: {}, code: {}, effectiveFrom: {}, effectiveTo: {}, enabled: {}, domains: {}, authorities: {}",
						formConfig.getUuid(), formConfig.getCode(), formConfig.getEffectiveFrom(),
						formConfig.getEffectiveTo(), formConfig.isEnabled(), formConfig.getDomains(),
						formConfig.getAuthorities());
				if (!formClaim.getCompany().equals(formConfig.getCompany())) {
					String msg = String.format("FormClaim.company (%s) is not the same as FormConfig.company (%s) - rejecting request!",
							formClaim.getCompany(), formConfig.getCompany());
					LOG.error(msg);
					throw new BadCredentialsException(msg);
				}
				String tokenId = tokenDetail.getJti();
				Optional<FormJsonWebToken> formJsonWebTokenOpt = formConfig.getToken(tokenId);
				if (!formJsonWebTokenOpt.isPresent()) {
					String msg = String.format("tokenId %s does not belong to FormConfig with UUID %s",
						tokenDetail.getJti(), formConfig.getUuid());
					LOG.error(msg);
					throw new BadCredentialsException(msg);
				}
				FormJsonWebToken formJsonWebToken = formJsonWebTokenOpt.get();
				if (!formJsonWebToken.isEnabled()) {
					String msg = String.format("JWT with tokenId %s is disabled!", tokenId);
					LOG.error(msg);
					throw new BadCredentialsException(msg);
				}
				ServicesFormConfigAuthentication sfca = new ServicesFormConfigAuthentication(fud);
				if (!sfca.isAuthenticated()) {
					String msg = String.format("FormConfig with UUID %s failed authentication check!", formUuid);
					LOG.error(msg);
					throw new BadCredentialsException(msg);
				}
				authenticateSpecific((String)authentication.getDetails(), formConfig.getDescription());
				return sfca;
			}
			throw new InternalAuthenticationServiceException("UserDetails is not an instance of FormUserDetails!");
		} catch (JwtException e) {
			String msg = String.format("Error parsing JWT %s", servicesToken);
			LOG.error(msg, e);
			throw new BadCredentialsException(msg, e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return ServicesTokenAuthentication.class.isAssignableFrom(authentication);
	}

	private void authenticateSpecific(String path, String conStrList) throws BadCredentialsException{
		if(!conStrList.startsWith("|"))return;
		if(Arrays.stream(conStrList.split("\\|")).anyMatch(pass->{
			if(!pass.equals("") && path.contains(pass))return true;
			return false;
		}))return;
		throw new BadCredentialsException(String.format("Specific request path %s is invalid for %s",path,conStrList));
	}
}
