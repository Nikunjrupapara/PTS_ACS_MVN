package com.yesmarketing.ptsacs.admin.service;

import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;

public interface CustomerIdDefinitionService {

	CustomerIdDefinition get(String company);

	CustomerIdDefinition save(CustomerIdDefinition customerIdDefinition);
}
