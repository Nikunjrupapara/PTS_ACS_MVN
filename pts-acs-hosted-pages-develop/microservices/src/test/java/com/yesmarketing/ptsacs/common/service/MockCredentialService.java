package com.yesmarketing.ptsacs.common.service;

import com.yesmarketing.acsapi.auth.model.AccessToken;
import com.yesmarketing.acsapi.auth.model.ApiKey;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.auth.service.CredentialService;
import com.yesmarketing.ptsacs.common.util.CredentialModelTestHelper;

import java.util.Collection;

public class MockCredentialService implements CredentialService {

	@Override
	public CredentialModel getCredential(String company) {
		return CredentialModelTestHelper.getCredential(company);
	}

	@Override
	public CredentialModel createCredential(CredentialModel model) {
		return null;
	}

	@Override
	public CredentialModel updateCredential(CredentialModel model) {
		return null;
	}

	@Override
	public CredentialModel updateCredentialStatus(CredentialModel model) {
		return null;
	}

	@Override
	public ApiKey addApiKey(String company, ApiKey apiKey, CredentialModel model) {
		return null;
	}

	@Override
	public ApiKey updateApiKeyEnabled(CredentialModel model, ApiKey apiKey) {
		return null;
	}

	@Override
	public Collection<CredentialModel> getCredentials() {
		return null;
	}

	@Override
	public AccessToken testCredential(CredentialModel cm) {
		return null;
	}
}
