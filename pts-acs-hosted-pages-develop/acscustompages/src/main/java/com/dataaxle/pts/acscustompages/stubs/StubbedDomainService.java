package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.exception.DomainNotFoundException;
import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.service.DomainService;

public class StubbedDomainService implements DomainService {

	@Override
	public Domain getDomain(String name) {
		return StubbedDomainRepository.findById(name).orElseThrow(() -> new DomainNotFoundException(name));
	}
}
