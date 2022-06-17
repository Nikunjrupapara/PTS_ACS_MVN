package com.dataaxle.pts.acscustompages.errorhandling;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { "development" })
@AutoConfigureMockMvc
public class TestDefaultErrorPage {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RequestManager requestManager;

	@Test
	void microservices_not_available_with_redirect() throws Exception {
		Mockito
			.when(requestManager.post(anyString(), any(AppDetails.class), anyString()))
			.thenThrow(new ServerErrorException("Error in POST request",
				new ResourceAccessException("I/O error on POST request for \"http://localhost:8080/v1/services/customer\": Connection refused...",
					new ConnectException("Connection refused"))));

		MvcResult result = mockMvc.perform(post("https://test.ymnewsolutions.com/p/profile/search/register/retail/emailLookup")
							.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
							.accept(MediaType.TEXT_HTML_VALUE)
							.param("items['email']", "charlesb@yesmail.com")
							.param("brand", "ymnewsolutions")
							.param("submit", "VERIFY"))
			.andDo(print())
			.andExpect(status().isOk())
							   .andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains("Something went wrong"), "Heading");
		assertTrue(content.contains("Request URL: <span>https://test.ymnewsolutions.com/p/profile/search/register/retail/emailLookup</span>"),
			"contains URL");
		Pattern checkRequestId = Pattern.compile("Request Id: <span>[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}</span>");
		Matcher matcher = checkRequestId.matcher(content);
		assertTrue(matcher.find(), "Request Id");
		assertTrue(content.contains("name=\"msg\""), "Exception Msg");
		assertTrue(content.contains("name=\"trace\""), "Exception Trace");
	}

	@Test
	void microservices_not_available_no_redirect() throws Exception {
		Mockito
			.when(requestManager.post(anyString(), any(AppDetails.class), anyString()))
			.thenThrow(new ServerErrorException("Error in POST request",
				new ResourceAccessException("I/O error on POST request for \"http://localhost:8080/v1/services/customer\": Connection refused...",
					new ConnectException("Connection refused"))));

		MvcResult result = mockMvc.perform(post("https://test.ymnewsolutions.com/p/profile/create/marketing/signup/index")
											   .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
											   .accept(MediaType.TEXT_HTML_VALUE)
											   .param("items['email']", "charlesb@yesmail.com")
											   .param("items['confirmEmail']", "charlesb@yesmail.com")
											   .param("items['firstName']", "Charles")
											   .param("items['lastName']", "Berger")
											   .param("brand", "ymnewsolutions"))
							   .andDo(print())
							   .andExpect(status().isOk())
							   .andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains("An error occurred while processing your request."), "Error message");
		assertTrue(content.contains("action=\"/p/profile/create/marketing/signup/index\""), "Form method");
		assertTrue(content.contains("Email:"), "Email prompt");
		assertTrue(content.contains("First Name:"), "First Name prompt");
	}
}
