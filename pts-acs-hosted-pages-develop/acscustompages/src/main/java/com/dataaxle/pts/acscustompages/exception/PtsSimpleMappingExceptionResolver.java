package com.dataaxle.pts.acscustompages.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class PtsSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		LOG.error("An error occurred for URI {}", request.getRequestURI(), ex);
	}
}
