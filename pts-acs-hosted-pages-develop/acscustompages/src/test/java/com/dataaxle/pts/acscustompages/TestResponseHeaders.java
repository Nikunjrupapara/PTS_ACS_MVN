package com.dataaxle.pts.acscustompages;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dataaxle.pts.acscustompages.exception.CustomPagesAuthenticationException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { "development" })
@AutoConfigureMockMvc
public class TestResponseHeaders {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AppDetailsRepository appDetailsRepository;

	private String uri;

	private ResultActions result;

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
	@DisplayName("US Bank Preferences Signup")
	void usbank_preferences_signup() throws Exception {
		uri = String.format("https://localhost:%d/p/profile/search/preferences/signup", port);
		result = mockMvc.perform(
			get(uri)
			.header(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com")
		)
			.andDo(print());

		checkDefaultHeaders(result);
		result
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'self'")))
			.andExpect(header().string("X-Frame-Options", "SAMEORIGIN"));
	}

	@Test
	@DisplayName("Resideo Buoy Signup")
	@Disabled("Test will fail because this configuration is no longer enabled")
	void resideo_buoy_signup() throws Exception {
		uri = String.format("https://localhost:%d/p/profile/search/buoySignup/signup", port);
		result = mockMvc.perform(
			get(uri)
				.header(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com")
		)
			.andDo(print());

		checkDefaultHeaders(result);
		result
			.andExpect(status().isOk());
	}

	private void checkDefaultHeaders(ResultActions result) throws Exception {
		result
			.andExpect(status().isOk())
			.andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
			.andExpect(header().string("Content-Security-Policy", containsString("report-to /acscustompages/p/page/csp/reporting; report-uri /acscustompages/p/page/csp/reporting")))
			.andExpect(header().string("Expires", "0"))
			.andExpect(header().string("Pragma", "no-cache"))
			.andExpect(header().string("Set-Cookie", containsString("HttpOnly")))
			.andExpect(header().string("Set-Cookie", containsString("Secure")))
			.andExpect(header().string("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains ; preload"))
			.andExpect(header().string("X-Content-Type-Options", "nosniff"))
			.andExpect(header().string("X-XSS-Protection", "1; mode=block"));
	}
}
