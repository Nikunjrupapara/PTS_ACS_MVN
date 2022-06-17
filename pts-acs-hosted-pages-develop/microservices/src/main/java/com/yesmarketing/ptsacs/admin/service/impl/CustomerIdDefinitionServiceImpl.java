package com.yesmarketing.ptsacs.admin.service.impl;

import com.yesmarketing.ptsacs.admin.service.CustomerIdDefinitionService;
import com.yesmarketing.ptsacs.common.exception.NoCustomerIdDefinitionException;
import com.yesmarketing.ptsacs.common.repository.CustomerIdDefinitionRepository;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerIdDefinitionServiceImpl implements CustomerIdDefinitionService {

	private final CustomerIdDefinitionRepository customerIdDefinitionRepository;

	public CustomerIdDefinitionServiceImpl(CustomerIdDefinitionRepository customerIdDefinitionRepository) {
		this.customerIdDefinitionRepository = customerIdDefinitionRepository;
	}

	@Override
	@Cacheable("customerIdDefinition")
	public CustomerIdDefinition get(String company) {
		return customerIdDefinitionRepository.findByCompany(company)
			.orElseThrow(() -> new NoCustomerIdDefinitionException(company));
	}

	@Override
	public CustomerIdDefinition save(CustomerIdDefinition customerIdDefinition) {
		return customerIdDefinitionRepository.save(customerIdDefinition);
	}
}
