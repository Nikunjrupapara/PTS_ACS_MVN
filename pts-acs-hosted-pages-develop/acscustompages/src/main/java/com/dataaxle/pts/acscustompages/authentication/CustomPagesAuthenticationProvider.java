package com.dataaxle.pts.acscustompages.authentication;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.FORWARDED_HDR;

import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesAuthenticationException;
import com.dataaxle.pts.acscustompages.service.CustomPagesRequestService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class CustomPagesAuthenticationProvider  implements AuthenticationProvider {

	private final CustomPagesRequestService customPagesRequestService;

	@Autowired
	public CustomPagesAuthenticationProvider(CustomPagesRequestService customPagesRequestService) {
		this.customPagesRequestService = customPagesRequestService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = (HttpServletRequest) authentication.getPrincipal();
		try {
			CustomPagesRequest cpr = customPagesRequestService.parseRequest(request);
			if (!cpr.isAuthenticated()) {
				throw new CustomPagesAuthenticationException(cpr);
			}
			cpr.checkPageName();
			return new CustomPagesAppAuthentication(cpr);
		} catch (AppDetailsNotFoundException e) {
			StringBuilder message = new StringBuilder();
			message.append("Unable to derive appDetails from URL: ").append(request.getRequestURL().toString());
			if (request.getHeader(FORWARDED_HDR) != null) {
				message
					.append(String.format(", %s: ", FORWARDED_HDR))
					.append(request.getHeader(CustomPagesConstants.FORWARDED_HDR));
			}
			LOG.error("{}", message, e);
			throw e;
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return CustomPagesRequestAuthentication.class.isAssignableFrom(aClass);
	}
}
