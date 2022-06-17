package com.dataaxle.pts.acscustompages.utils;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.MDC_REQUEST_ID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class MdcLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		HttpSession session = httpServletRequest.getSession();
		String requestId = (String)session.getAttribute(MDC_REQUEST_ID);
		if (StringUtils.isEmpty(requestId)) {
			requestId = UUID.randomUUID().toString();
			session.setAttribute(MDC_REQUEST_ID, requestId);
			LOG.debug("Generated requestId {}", requestId);
		} else {
			LOG.debug("Retrieved requestId {} from session", requestId);
		}
		LOG.debug("RequestId {} added to MDC", requestId);
		MDC.put(CustomPagesConstants.MDC_REQUEST_ID, requestId);
		try {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} finally {
			MDC.clear();
		}
	}
}
