package com.yesmarketing.ptsacs.services.resource;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yesmail.api.common.config.MessageSourceConfiguration;
import com.yesmail.api.common.config.MessageSourceProperties;
import com.yesmarketing.ptsacs.common.authentication.AuthenticationConfigurationForControllerTests;
import com.yesmarketing.ptsacs.common.exception.PtsResponseEntityExceptionHandler;
import com.yesmarketing.ptsacs.services.configuration.ResourceTestsConfiguration;
import com.yesmarketing.ptsacs.services.configuration.ProfileResourceConfiguration;
import com.yesmarketing.ptsacs.services.configuration.WithMockFormConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith({SpringExtension.class})
@WebMvcTest(controllers = ProfileResource.class)
@ContextConfiguration(
	classes = { ResourceTestsConfiguration.class, MessageSourceConfiguration.class,
		ProfileResourceConfiguration.class, MessageSourceProperties.class}
)
@ImportAutoConfiguration({ PtsResponseEntityExceptionHandler.class, AuthenticationConfigurationForControllerTests.class})
public class TestProfileResource {

	private static final Logger LOG = LoggerFactory.getLogger(TestProfileResource.class);

	private static final String BASE_URI = "/v1/services/profiles";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockFormConfig()
	void getSubscriberByEncryptedId() throws Exception {

		String l = "B4B7556A4E1F782C7988E734295E0BE15FC8532E84188359C8E29375DAE1641250F2630BBCFF801BD78F09E5150B4F0FF65C94A85476D4E9B56F98BD8C829273";
		String s = "b9c5ac88-6c3f-4d5b-a107-50f3d652703e";

		String uri = String.format("%s?l=%s&s=%s", BASE_URI, l, s);
		LOG.info("Calling URI: {}", uri);
		mockMvc.perform(get(uri)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.profile.PKey", notNullValue()))
			.andExpect(jsonPath("$.profile.age", equalTo(51)))
			.andExpect(jsonPath("$.profile.birthDate", equalTo("1969-06-03")))
			.andExpect(jsonPath("$.profile.created", notNullValue()))
			.andExpect(jsonPath("$.profile.cryptedId", notNullValue()))
			.andExpect(jsonPath("$.profile.customFields.cusCustomerId", notNullValue()))
			.andExpect(jsonPath("$.profile.customFields.cusCustomerUUID", equalTo(s)))
			.andExpect(jsonPath("$.profile.customFields.cusEmailSha", notNullValue()))
			.andExpect(jsonPath("$.profile.domain", equalTo("yesmarketing.com")))
			.andExpect(jsonPath("$.profile.email", equalTo("charles.berger@yesmarketing.com")))
			.andExpect(jsonPath("$.profile.firstName", equalTo("Charles")))
			.andExpect(jsonPath("$.profile.gender", equalTo("male")))
			.andExpect(jsonPath("$.profile.href", notNullValue()))
			.andExpect(jsonPath("$.profile.isExternal", equalTo(false)))
			.andExpect(jsonPath("$.profile.lastModified", notNullValue()))
			.andExpect(jsonPath("$.profile.lastName", equalTo("Berger")))
			.andExpect(jsonPath("$.profile.location.address1", equalTo("Address Line 1")))
			.andExpect(jsonPath("$.profile.location.address2", equalTo("Address Line 2")))
			.andExpect(jsonPath("$.profile.location.address3", equalTo("Address Line 3")))
			.andExpect(jsonPath("$.profile.location.address4", equalTo("Address Line 4")))
			.andExpect(jsonPath("$.profile.location.city", equalTo("City")))
			.andExpect(jsonPath("$.profile.location.countryCode", equalTo("US")))
			.andExpect(jsonPath("$.profile.location.stateCode", equalTo("NY")))
			.andExpect(jsonPath("$.profile.location.zipCode", equalTo("54321")))
			.andExpect(jsonPath("$.profile.mobilePhone", equalTo("+447904736935")))
			.andExpect(jsonPath("$.profile.postalAddress.line1", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.line2", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.line3", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.line4", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.line5", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.line6", notNullValue()))
			.andExpect(jsonPath("$.profile.postalAddress.serialized", notNullValue()))
			.andExpect(jsonPath("$.profile.phone", equalTo("")))
			.andExpect(jsonPath("$.profile.subscriptions.href", notNullValue()))
			.andExpect(jsonPath("$.profile.title", notNullValue()))
			.andExpect(jsonPath("$.currentServices").doesNotExist())
			.andExpect(jsonPath("$.historicServices").doesNotExist());

	}

	@Test
	@WithMockFormConfig()
	void getSubscriberByEncryptedId_services() throws Exception {

		String l = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";
		String s = "be643fa5-6459-4771-8569-f3cebdd76187";

		String uri = String.format("%s?l=%s&s=%s&view=services", BASE_URI, l, s);
		LOG.info("Calling URI: {}", uri);
		mockMvc.perform(get(uri)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.profile.PKey", notNullValue()))
				.andExpect(jsonPath("$.profile.created", notNullValue()))
				.andExpect(jsonPath("$.profile.cryptedId", notNullValue()))
				.andExpect(jsonPath("$.profile.customFields.cusCustomerId", notNullValue()))
				.andExpect(jsonPath("$.profile.customFields.cusCustomerUUID", equalTo(s)))
				.andExpect(jsonPath("$.profile.customFields.cusEmailSha", notNullValue()))
				.andExpect(jsonPath("$.profile.domain", equalTo("yesmail.com")))
				.andExpect(jsonPath("$.profile.email", equalTo("charlesb+11@yesmail.com")))
				.andExpect(jsonPath("$.profile.firstName", equalTo("Charles")))
				.andExpect(jsonPath("$.profile.href", notNullValue()))
				.andExpect(jsonPath("$.profile.isExternal", equalTo(false)))
				.andExpect(jsonPath("$.profile.lastModified", notNullValue()))
				.andExpect(jsonPath("$.profile.lastName", equalTo("Berger")))
				.andExpect(jsonPath("$.profile.subscriptions.href", notNullValue()))
				.andExpect(jsonPath("$.profile.title", notNullValue()))
				.andExpect(jsonPath("$.currentServices[?(@.name == 'yesMarketingAService')].name", hasItem("yesMarketingAService")))
				.andExpect(jsonPath("$.currentServices[?(@.label == 'Marketing A')].label", hasItem("Marketing A")))
				.andExpect(jsonPath("$.currentServices[*].created", notNullValue()))
				.andExpect(jsonPath("$.historicServices").doesNotExist());
	}

	@Test
	@WithMockFormConfig()
	void getSubscriberByEncryptedId_unknown_view() throws Exception {
		String l = "XXX";
		String s = "bdb64076-2052-4762-90da-8ab56c35c30c";

		String uri = String.format("%s?l=%s&s=%s&view=foo", BASE_URI, l, s);
		mockMvc.perform(get(uri)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("The request URL contained one or more invalid parameters.")))
				.andExpect(jsonPath("$.moreInfo", equalTo("Check the parameters and try again.")));
	}

	@Test
	@WithMockFormConfig()
	void getSubscriberByEncryptedId_notFound() throws Exception {
		String l = "XXX";
		String s = "bdb64076-2052-4762-90da-8ab56c35c30c";

		String uri = String.format("%s?l=%s&s=%s", BASE_URI, l, s);
		mockMvc.perform(get(uri)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.status", equalTo(404)))
			.andExpect(jsonPath("$.message", equalTo("No profile was found with the supplied parameters.")))
			.andExpect(jsonPath("$.moreInfo", equalTo("Check the parameters and try again.")));
	}

	@Test
	@WithMockFormConfig()
	void createProfile() throws Exception {
		String body = "{\"email\":\"charlesb@yesmail.com\",\"firstName\":\"Charles\",\"lastName\":\"Berger (Ym)\"," +
						  "\"cusCustomerId\": \"cb-ym\"}";

		String uri = String.format("%s/create", BASE_URI);
		mockMvc.perform(post(uri)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.with(csrf())
			.content(body))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.PKey", notNullValue()))
			.andExpect(jsonPath("$.created", notNullValue()))
			.andExpect(jsonPath("$.cryptedId", notNullValue()))
			.andExpect(jsonPath("$.domain", equalTo("yesmail.com")))
			.andExpect(jsonPath("$.email", equalTo("charlesb@yesmail.com")))
			.andExpect(jsonPath("$.firstName", equalTo("Charles")))
			.andExpect(jsonPath("$.gender", equalTo("unknown")))
			.andExpect(jsonPath("$.href", notNullValue()))
			.andExpect(jsonPath("$.isExternal", equalTo(false)))
			.andExpect(jsonPath("$.lastModified", notNullValue()))
			.andExpect(jsonPath("$.lastName", equalTo("Berger (Ym)")))
			.andExpect(jsonPath("$.mobilePhone", equalTo("")))
			.andExpect(jsonPath("$.phone", equalTo("")))
			.andExpect(jsonPath("$.subscriptions.href", notNullValue()))
			.andExpect(jsonPath("$.title", notNullValue()));
	}
}
