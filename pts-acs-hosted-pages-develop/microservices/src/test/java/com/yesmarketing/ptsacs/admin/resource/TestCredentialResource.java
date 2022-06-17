package com.yesmarketing.ptsacs.admin.resource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yesmarketing.ptsacs.admin.configuration.CredentialResourceConfiguration;
import com.yesmarketing.ptsacs.common.authentication.AuthenticationConfigurationForControllerTests;
import com.yesmarketing.ptsacs.common.exception.PtsResponseEntityExceptionHandler;
import com.yesmarketing.ptsacs.common.util.CredentialModelTestHelper;
import com.yesmarketing.ptsacs.services.configuration.HelloControllerConfiguration;
import com.yesmarketing.ptsacs.services.configuration.ResourceTestsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CredentialResource.class)
@ContextConfiguration(classes = { ResourceTestsConfiguration.class,	CredentialResourceConfiguration.class})
@ImportAutoConfiguration({ AuthenticationConfigurationForControllerTests.class, PtsResponseEntityExceptionHandler.class })
public class TestCredentialResource {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void putCredential() throws Exception {
		String payload = CredentialModelTestHelper.getPayloadFromFile("update-credential");
		mockMvc.perform(put("/api/credentials/testco1")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.characterEncoding("UTF-8")
		.with(csrf())
		.content(payload))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.company").exists());
	}
}
