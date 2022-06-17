package com.yesmarketing.ptsacs.services.configuration;

import com.yesmarketing.ptsacs.services.authentication.ServicesTokenAuthentication;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Deprecated
@Slf4j
public class ServicesRequestContextFilter implements Filter {

	//@Autowired
	//private ServicesRequestContext servicesRequestContext;
	private final FormJwtTokenService formJwtTokenService;

	//private final AuthenticationManager authenticationManager;

	@Autowired
	public ServicesRequestContextFilter(FormJwtTokenService formJwtTokenService/*, FormConfigService formConfigService*//*AuthenticationManager authenticationManager*/) {
		this.formJwtTokenService = formJwtTokenService;
		//this.formConfigService = formConfigService;
		//this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		// Eventually this filter will get a JSON Web Token (JWT) from the request and decode it to identify the form that submitted
		// the request and identify the company, etc.
		// For now we are just hard coding the company until that infrastructure is built out.
		try {
			String servicesToken = request.getHeader(ServicesConstants.SERVICES_TOKEN_HDR_NAME);
			FormClaim formClaim = formJwtTokenService.validateTokenOld(servicesToken);
			LOG.info("company={}, uuid={}", formClaim.getCompany(), formClaim.getUuid());
			//servicesRequestContext.setCompany(formClaim.getCompany());
			//FormConfig formConfig = formConfigService.getFormConfig(formClaim);
			Authentication authentication = new ServicesTokenAuthentication(servicesToken,request.getRequestURI());
			//Auth
			//SecurityContextHolder.getContext().setAuthentication();
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			// Otherwise when a previously used container thread is used, it will have the old tenant id set and
			// if for some reason this filter is skipped, tenantStore will hold an unreliable value
			//servicesRequestContext.clear();
		}
	}
}
