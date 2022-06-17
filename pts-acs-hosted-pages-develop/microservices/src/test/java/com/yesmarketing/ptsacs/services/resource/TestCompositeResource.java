package com.yesmarketing.ptsacs.services.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesmail.api.common.config.MessageSourceConfiguration;
import com.yesmail.api.common.config.MessageSourceProperties;
import com.yesmarketing.ptsacs.common.authentication.AuthenticationConfigurationForControllerTests;
import com.yesmarketing.ptsacs.common.configuration.JacksonConfiguration;
import com.yesmarketing.ptsacs.common.exception.PtsResponseEntityExceptionHandler;
import com.yesmarketing.ptsacs.services.configuration.CompositeResourceConfiguration;
import com.yesmarketing.ptsacs.services.configuration.ResourceTestsConfiguration;
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
@WebMvcTest(controllers = CompositeResource.class)
@ContextConfiguration(
	classes = { ResourceTestsConfiguration.class, MessageSourceConfiguration.class,
		CompositeResourceConfiguration.class, MessageSourceProperties.class, JacksonConfiguration.class }
)
@ImportAutoConfiguration({ PtsResponseEntityExceptionHandler.class, AuthenticationConfigurationForControllerTests.class})
public class TestCompositeResource {

	private static final Logger LOG = LoggerFactory.getLogger(TestCompositeResource.class);

	private static final String BASE_URI = "/v1/services/composite";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockFormConfig()
	void deserialization_error()  throws Exception {
		String body = "{\"profile\":{\"email\":\"charlesb4321@yesmail.com\",\"firstName\":\"Charles\",\"lastName\":\"Berger\"" +
						  ",\"mobileNumber\":\"1112223333\",\"smsFlag\":true,\"source\":\"honeywellProdReg\"}," +
						  "\"services\":{\"add\":[\"connectedhome\",\"honeywellhomeemails\",\"honeywellhomepresentedoffers\"," +
						  "\"honeywellhomeasks\"],\"remove\":[]},\"customResources\":[{\"name\":\"cusHosted_pages_logs\"," +
						  "\"records\":[{\"exported\":\"2021-02-11 12:03:17.340Z\",\"zip\":\"32145\",\"lastName\":\"Berger\"," +
						  "\"country\":\"US\",\"exportStatus\":\"N\",\"city\":\"A City\",\"smsFlag\":true,\"mobile\":\"1112223333\"," +
						  "\"source\":\"honeywellProdReg\",\"productName\":\"TrueDRY� DR65 Dehumidification System\"," +
						  "\"firstName\":\"Charles\",\"customerId\":\"charlesb4321@yesmail.com\",\"modelNumber\":\"5\"," +
						  "\"state\":\"UT\",\"email\":\"charlesb4321@yesmail.com\"}]}]," +
						  "\"emails\":[{\"eventId\":\"EVTproductRegistrationConfirmationHoneywell\",\"email\":\"charlesb4321@yesmail.com\"," +
						  "\"ctx\":{\"customerIdHash\":\"\"}}]}";


		mockMvc.perform(
			post(BASE_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.with(csrf())
				.content(body))
			.andDo(print())
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.status", equalTo(400)))
			.andExpect(jsonPath("$.message", equalTo("There was an error deserializing the request body")))
			.andExpect(jsonPath("$.moreInfo", equalTo("Unknown property name: mobileNumber")))
			.andReturn();
	}
}
