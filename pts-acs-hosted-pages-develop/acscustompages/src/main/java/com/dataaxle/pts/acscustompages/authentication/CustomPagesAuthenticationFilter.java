package com.dataaxle.pts.acscustompages.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CustomPagesAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	@Autowired
	public CustomPagesAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		StringBuilder uri = new StringBuilder(request.getRequestURL());
		if (StringUtils.hasText(request.getQueryString())) {
			uri.append("?").append(request.getQueryString());
		}
		LOG.info("{} {}", request.getMethod(), uri);
		Authentication requestAuthentication = new CustomPagesRequestAuthentication(request);
		Authentication appAuthentication = authenticationManager.authenticate(requestAuthentication);
		SecurityContextHolder.getContext().setAuthentication(appAuthentication);
		chain.doFilter(req, res);
		HttpServletResponse response = (HttpServletResponse)res;
		CustomPagesRequest customPagesRequest = (CustomPagesRequest)appAuthentication.getPrincipal();
		customPagesRequest.setResponseHeaders(response);
	}
}
