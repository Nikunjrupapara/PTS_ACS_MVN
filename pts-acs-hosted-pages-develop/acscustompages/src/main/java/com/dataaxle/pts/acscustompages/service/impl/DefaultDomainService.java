package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.domains", havingValue = "false", matchIfMissing = true)
public class DefaultDomainService implements DomainService {
	@Override
	public Domain getDomain(String name) {
		return null;
	}
}
