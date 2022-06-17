package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.configuration.JwtConfiguration;
import com.dataaxle.pts.acscustompages.exception.JwtNotFoundException;
import com.dataaxle.pts.acscustompages.service.JwtService;
import org.springframework.stereotype.Service;

@Service
public class ConfigurableJwtService implements JwtService {

	private final JwtConfiguration jwtConfiguration;

	public ConfigurableJwtService(JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}

	@Override
	public String getToken(String company, String contextPath) {
		JwtConfiguration.Jwt jwt = jwtConfiguration.getJwts().stream()
				   .filter(item -> item.getCompany().equals(company) && item.getContextPath().equals(contextPath))
				   .findFirst()
				   .orElseThrow(() -> new JwtNotFoundException(company, contextPath));

		return jwt.getToken();
	}
}
