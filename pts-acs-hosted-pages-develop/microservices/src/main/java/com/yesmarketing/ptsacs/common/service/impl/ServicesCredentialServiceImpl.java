package com.yesmarketing.ptsacs.common.service.impl;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmail.api.common.util.CommonMessageKeys;
import com.yesmarketing.acsapi.auth.model.AccessToken;
import com.yesmarketing.acsapi.auth.model.ApiKey;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.auth.service.Authenticator;
import com.yesmarketing.acsapi.auth.service.CredentialService;
import com.yesmarketing.ptsacs.admin.service.PtsCredentialService;
import com.yesmarketing.ptsacs.common.exception.ApiKeyNotSupportedException;
import com.yesmarketing.ptsacs.common.repository.CredentialRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class ServicesCredentialServiceImpl implements CredentialService, PtsCredentialService {

	private final CredentialRepository credentialRepository;

	private final Authenticator authenticator;

	@Autowired
	public ServicesCredentialServiceImpl(CredentialRepository credentialRepository, Authenticator authenticator) {
		this.credentialRepository = credentialRepository;
		this.authenticator = authenticator;
	}

	@Override
	public CredentialModel getCredential(String company) {
		return credentialRepository.findByCompany(company)
				.orElseThrow(() -> new ObjectNotFoundException(CommonMessageKeys.RESOURCE_NOT_FOUND_MESSAGE, new Object[] {},
						CommonMessageKeys.RESOURCE_NOT_FOUND_INFO, new Object[] { company }));
	}

	@Override
	public CredentialModel createCredential(CredentialModel model) {
		model.setCreated(LocalDateTime.now());
		model.setLastUpdated(LocalDateTime.now());
		return credentialRepository.insert(model);
	}

	@Override
	public CredentialModel updateCredential(CredentialModel model) {
		CredentialModel existing = credentialRepository.findByCompany(model.getCompany())
				.orElseThrow(() -> new ObjectNotFoundException(CommonMessageKeys.RESOURCE_NOT_FOUND_MESSAGE,
						CommonMessageKeys.RESOURCE_NOT_FOUND_INFO));
		model.setCompany(existing.getCompany());
		model.setCreated(existing.getCreated());
		model.setLastUpdated(LocalDateTime.now());
		return credentialRepository.save(model);
	}

	@Override
	public CredentialModel updateCredentialStatus(CredentialModel model) {
		model.setLastUpdated(LocalDateTime.now());
		return credentialRepository.save(model);
	}

	@Override
	public ApiKey addApiKey(String company, ApiKey apiKey, CredentialModel model) {
		throw new ApiKeyNotSupportedException();
	}

	@Override
	public ApiKey updateApiKeyEnabled(CredentialModel model, ApiKey apiKey) {
		throw new ApiKeyNotSupportedException();
	}

	@Override
	public Collection<CredentialModel> getCredentials() {
		return credentialRepository.findAll();
	}

	@Override
	public AccessToken testCredential(CredentialModel cm) {
		return authenticator.authenticae(cm);
	}

	@Override
	public void delete(String company) {
		CredentialModel model = credentialRepository.findByCompany(company)
									   .orElseThrow(() -> new ObjectNotFoundException(CommonMessageKeys.RESOURCE_NOT_FOUND_MESSAGE,
										   CommonMessageKeys.RESOURCE_NOT_FOUND_INFO));
		credentialRepository.delete(model);
	}
}
