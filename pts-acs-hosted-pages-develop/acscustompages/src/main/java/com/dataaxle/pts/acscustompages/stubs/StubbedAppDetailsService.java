package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.service.JwtService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultAppDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.appDetails", havingValue = "true")
public class StubbedAppDetailsService extends DefaultAppDetailsService {

	private final StubbedAppDetailsRepository sard = new StubbedAppDetailsRepository();

	private final JwtService jwtService;

	public StubbedAppDetailsService(AppDetailsRepository appDetailsRepository, JwtService jwtService) {
		super(appDetailsRepository);
		this.jwtService = jwtService;
	}

	@Override
	protected AppDetails searchAppDetails(String company, String contextPath) {
		AppDetails appDetails = sard.getAppDetails(company, contextPath);
		String jwt = jwtService.getToken(company, contextPath);
		appDetails.setJwt(jwt);
		return appDetails;
	}
}
