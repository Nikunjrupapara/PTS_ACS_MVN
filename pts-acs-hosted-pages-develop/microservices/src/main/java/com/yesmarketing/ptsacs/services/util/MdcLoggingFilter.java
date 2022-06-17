package com.yesmarketing.ptsacs.services.util;

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

@Slf4j
public class MdcLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestId, message;
		if (StringUtils.hasText(request.getHeader(ServicesConstants.REQUEST_ID_HEADER))) {
			requestId = request.getHeader(ServicesConstants.REQUEST_ID_HEADER);
			message = String.format("Retrieved requestId %s from request and added to MDC.", requestId);
		} else {
			requestId = UUID.randomUUID().toString();
			message = String.format("Generated requestId %s and added to MDC.", requestId);
		}
		MDC.put(ServicesConstants.MDC_REQUEST_ID, requestId);
		LOG.debug(message);
		try {
			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}

	}
}
