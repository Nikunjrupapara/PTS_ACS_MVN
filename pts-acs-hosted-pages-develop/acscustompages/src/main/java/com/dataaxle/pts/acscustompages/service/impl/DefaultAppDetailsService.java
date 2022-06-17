package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.configuration.ConfigurationParameters;
import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.service.AppDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.appDetails", havingValue = "false", matchIfMissing = true)
public class DefaultAppDetailsService implements AppDetailsService {

	private final AppDetailsRepository appDetailsRepository;

	public DefaultAppDetailsService(AppDetailsRepository appDetailsRepository) {
		this.appDetailsRepository = appDetailsRepository;
	}

	@Override
	public AppDetails getAppDetails(String company, String contextPath) {
		return searchAppDetails(company, contextPath);
	}

	protected AppDetails searchAppDetails(String company, String contextPath) {
		AppDetails.AppDetailsId id = new AppDetails.AppDetailsId(company, contextPath);
		return appDetailsRepository.findById(id)
				   .orElseThrow(() -> new AppDetailsNotFoundException(company, contextPath));
	}

}
