package com.yesmarketing.ptsacs.services.resource;

import com.yesmarketing.acsapi.auth.service.CredentialManagementService;
import com.yesmarketing.ptsacs.services.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Endpoint(id = "acs-health")
public class StatusResource {

	private final StatusService statusService;

	@Autowired
	public StatusResource(StatusService statusService) {
		this.statusService = statusService;
	}

	// GET /actuator/acs-health/{company}?l={customerIdHash}
	@ReadOperation
	public Health getProfile(@Selector String company,String l) {
		return statusService.getStatus(company, l);
	}


	@ReadOperation
	public List getCompanies(){
		return statusService.getCompanies();
	}
}
