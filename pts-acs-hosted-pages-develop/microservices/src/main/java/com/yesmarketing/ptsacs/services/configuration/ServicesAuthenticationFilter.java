package com.yesmarketing.ptsacs.services.configuration;

import static org.springframework.util.StringUtils.isEmpty;

import com.yesmarketing.ptsacs.services.authentication.ServicesTokenAuthentication;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public final class ServicesAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final  AuthenticationManager authenticationManager;

	public ServicesAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)req;
		String servicesToken = httpRequest.getHeader(ServicesConstants.SERVICES_TOKEN_HDR_NAME);
		if (isEmpty(servicesToken)) {
			String msg = "No services authentication token found in request headers";
			LOG.error(msg);
			throw new BadCredentialsException(msg);
		}
		// only needed for debugging Authentication integration tests.  Can be removed when that is complete.
		if (httpRequest.getHeader("x-token-state") != null) {
			String tokenState = httpRequest.getHeader("x-token-state");
			LOG.info("x-token-state={}", tokenState);
		}
		Authentication tokenAuthentication = new ServicesTokenAuthentication(servicesToken,httpRequest.getRequestURI());
		Authentication formAuthentication = authenticationManager.authenticate(tokenAuthentication);
		FormConfig formConfig = (FormConfig) formAuthentication.getPrincipal();
		validateDomain(httpRequest, formConfig);
		SecurityContextHolder.getContext().setAuthentication(formAuthentication);
		chain.doFilter(req, res);
	}

	private void validateDomain(HttpServletRequest request, FormConfig formConfig) {
		final String requestHost = getRemoteHost(request);
		boolean matched = false;
		if (requestHost.equals(ServicesConstants.LOCALHOST_IPV4) || requestHost.equals(ServicesConstants.LOCALHOST_IPV6)) {
			matched = formConfig
						  .getDomains()
						  .stream()
						  .anyMatch(domain ->
										domain.equals(ServicesConstants.LOCALHOST_IPV4) ||
											domain.equals(ServicesConstants.LOCALHOST_IPV6) ||
											domain.equals(ServicesConstants.LOCALHOST_NAME));
		} else {
			matched = formConfig.getDomains().stream().anyMatch(requestHost::equalsIgnoreCase);
		}
		if (!matched) {
			String msg = String.format("Request domain %s does not match form config domains: %s", requestHost,
				formConfig.getDomains());
			LOG.error(msg);
//			throw new BadCredentialsException(msg);
		}
	}

	private String getRemoteHost(HttpServletRequest request) {
		if (request.getHeader(ServicesConstants.FORWARDED_FROM_HDR) != null) {
			return request.getHeader(ServicesConstants.FORWARDED_FROM_HDR);
		}
		return request.getRemoteHost();
	}
}
