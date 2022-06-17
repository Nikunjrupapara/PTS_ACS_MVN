package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileRequestResult;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.impl.TriggerEmailServiceImpl;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestTriggerEmailService extends BaseServiceTest {

	private TriggerEmailService triggerEmailService;

	@BeforeEach
	void setup() throws Exception {
		super.globalSetup();
		triggerEmailService = new TriggerEmailServiceImpl(restTemplateRequestManager, jacksonConfiguration.objectMapper());
	}

	@Test
	void resideo_buoy_unsubscribed() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("POST", url, headers);
		appDetails = customPagesRequest.getAppDetails();

		ProfileResponse profileResponse = StubbedProfileRepository.search("resideo",
			Map.of("email", "charles.berger@yesmail.com"));
		Profile profile = profileResponse.getProfile();
		String email = (String)profile.getField("email").orElse("");
		String customerIdHash = profileResponse.getUniqueId();
		ProfileRequest profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.setTriggeredEventId("EVTcbBuoySignupConfirmation");
		profileRequest.setTriggeredEventRecipient(email);
		profileRequest.addPersonalizationField("Source", "buoySignup");
		profileRequest.addPersonalizationField("customerIdHash", customerIdHash);

		ProfileResponseWrapper profileResponseWrapper = triggerEmailService.process(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertTrue(result.isEmailSuccess(), "Trigger Email Success");
		});

	}
}
