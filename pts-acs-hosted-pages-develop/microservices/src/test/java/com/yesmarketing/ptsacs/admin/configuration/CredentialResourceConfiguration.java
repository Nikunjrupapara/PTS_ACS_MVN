package com.yesmarketing.ptsacs.admin.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.auth.service.CredentialManagementService;
import com.yesmarketing.ptsacs.admin.resource.CredentialResource;
import com.yesmarketing.ptsacs.admin.service.PtsCredentialManagementService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

public class CredentialResourceConfiguration {

	@Bean
	public CredentialResource credentialResource() {
		return new CredentialResource();
	}

	@Bean
	public CredentialManagementService credentialService() {
		CredentialManagementService cms = Mockito.mock(CredentialManagementService.class);

		Mockito.when(cms.updateCredential(anyString(), any(CredentialModel.class)))
			.thenAnswer(invocation -> {
				CredentialModel cm = invocation.getArgument(1, CredentialModel.class);
				cm.setLastUpdated(LocalDateTime.now());
				return cm;
			});

		return cms;
	}

	@Bean
	public PtsCredentialManagementService ptsCredentialManagementService() {
		return Mockito.mock(PtsCredentialManagementService.class);
	}
}
