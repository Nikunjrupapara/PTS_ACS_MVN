package com.yesmarketing.ptsacs.admin.service.impl;

import com.yesmarketing.ptsacs.admin.service.PtsCredentialManagementService;
import com.yesmarketing.ptsacs.admin.service.PtsCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PtsCredentialManagementServiceImpl implements PtsCredentialManagementService {

	private final PtsCredentialService ptsCredentialService;

	@Autowired
	public PtsCredentialManagementServiceImpl(PtsCredentialService ptsCredentialService) {
		this.ptsCredentialService = ptsCredentialService;
	}

	@Override
	public void delete(String company) {
		ptsCredentialService.delete(company);
	}
}
