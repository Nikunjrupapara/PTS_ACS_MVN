package com.yesmarketing.ptsacs.common.configuration;

import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoRedirectStrategy implements RedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
		// No redirect required
	}
}
