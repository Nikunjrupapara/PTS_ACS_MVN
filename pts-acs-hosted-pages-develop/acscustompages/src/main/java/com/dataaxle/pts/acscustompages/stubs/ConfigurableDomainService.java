package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.configuration.DomainConfiguration;
import com.dataaxle.pts.acscustompages.exception.DomainNotFoundException;
import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.domains", havingValue = "true")
public class ConfigurableDomainService implements DomainService {

	private final DomainConfiguration domainConfiguration;

	@Autowired
	public ConfigurableDomainService(DomainConfiguration domainConfiguration) {
		this.domainConfiguration = domainConfiguration;
	}

	@Override
	public Domain getDomain(String name) {
		return domainConfiguration.getDomains().stream()
				   .filter(domain -> domain.getName().equals(name))
				   .findFirst()
				   .orElseThrow(() -> new DomainNotFoundException(name));
	}
}
