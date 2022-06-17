package com.dataaxle.pts.acscustompages;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.exception.AppPageNotFoundException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesAuthenticationException;
import com.dataaxle.pts.acscustompages.exception.CustomPagesRequestException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { "development" })
@AutoConfigureMockMvc
public class TestAuthentication {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AppDetailsRepository appDetailsRepository;

	private String uri;

	private CustomPagesAuthenticationException exception;

	@BeforeEach
	void setup() {
		Mockito
			.when(appDetailsRepository.findById(any(AppDetails.AppDetailsId.class)))
			.thenAnswer(invocationOnMock -> {
				AppDetails.AppDetailsId id = invocationOnMock.getArgument(0, AppDetails.AppDetailsId.class);
				return StubbedAppDetailsRepository.findById(id);
			});
	}

	@Test
	@DisplayName("Render a page")
	void render_form() throws Exception {
		uri = String.format("http://localhost:%d/p/profile/search/preferences/signup", port);
		mockMvc.perform(
			get(uri)
			.header(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com")
		)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect((content().string(containsString("To sign up for emails or change your preferences"))));
	}

	@Test
	@DisplayName("AppDetails not found for domain & context path so throw exception")
	void not_found() throws Exception {
		uri = String.format("http://localhost:%d/p/profile/search/authtest/notfound/index", port);
		assertThrows(AppDetailsNotFoundException.class, () ->
			mockMvc.perform(
				get(uri)
					.header(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com")
			)
		);
	}

	@Test
	@DisplayName("AppDetails not enabled for domain & context path so throw exception")
	void not_enabled() throws Exception {
		uri = String.format("http://localhost:%d/p/profile/search/authtest/notenabled/index", port);
		exception = assertThrows(CustomPagesAuthenticationException.class,
			() -> mockMvc.perform(
				get(uri)
					.header(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com")
			)
		);
		CustomPagesRequest customPagesRequest = exception.getCustomPagesRequest();
		assertEquals("ymnewsolutions", customPagesRequest.getCompany());
		assertEquals(AppDetails.AuthenticationStatus.DISABLED, customPagesRequest.getAuthenticationStatus());
	}

	@Test
	@DisplayName("AppDetails not effective for domain & context path so throw exception")
	void not_effective_yet()throws Exception {
		uri = String.format("http://localhost:%d/p/profile/search/authtest/noteffective/index", port);
		exception = assertThrows(CustomPagesAuthenticationException.class,
			() -> mockMvc.perform(
				get(uri)
					.header(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com")
			)
		);
		CustomPagesRequest customPagesRequest = exception.getCustomPagesRequest();
		assertEquals("ymnewsolutions", customPagesRequest.getCompany());
		assertEquals(AppDetails.AuthenticationStatus.NOT_EFFECTIVE, customPagesRequest.getAuthenticationStatus());
	}

	@Test
	@DisplayName("AppDetails no longer effective for domain & context path so throw an exception")
	void no_longer_effective() throws Exception {
		uri = String.format("http://localhost:%d/p/profile/search/authtest/expired/index", port);
		exception = assertThrows(CustomPagesAuthenticationException.class,
			() -> mockMvc.perform(
				get(uri)
					.header(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com")
			)
		);
		CustomPagesRequest customPagesRequest = exception.getCustomPagesRequest();
		assertEquals("ymnewsolutions", customPagesRequest.getCompany());
		assertEquals(AppDetails.AuthenticationStatus.EXPIRED, customPagesRequest.getAuthenticationStatus());
	}

	@Test
	@DisplayName("AppDetails found but pageName not found")
	void app_page_not_found() throws Exception {
		uri = String.format("http://localhost:%d/p/profile/create/register/retail/index1", port);
		CustomPagesRequestException exception = assertThrows(CustomPagesRequestException.class,
			() -> mockMvc.perform(
				get(uri)
					.header(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com")
			)
		);
		Throwable cause = exception.getCause();
		assertTrue(cause instanceof AppPageNotFoundException);
		assertEquals("index1", ((AppPageNotFoundException) cause).getName());
		CustomPagesRequest customPagesRequest = exception.getCustomPagesRequest();
		assertEquals("ymnewsolutions", customPagesRequest.getCompany());
		assertEquals(AppDetails.AuthenticationStatus.AUTHENTICATED, customPagesRequest.getAuthenticationStatus());
	}
}
