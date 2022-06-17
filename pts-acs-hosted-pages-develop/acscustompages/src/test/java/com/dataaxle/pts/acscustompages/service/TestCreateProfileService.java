package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileRequestResult;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.impl.CreateProfileServiceImpl;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCreateProfileService extends BaseServiceTest {

	private CreateProfileService createProfileService;

	private AppDetails appDetails;

	private ProfileRequest profileRequest;

	@BeforeEach
	void setup() throws Exception {
		super.globalSetup();
		createProfileService = new CreateProfileServiceImpl(restTemplateRequestManager, jacksonConfiguration.objectMapper());
	}

	@Test
	void resideo_create_profile() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		profileRequest = new ProfileRequest("");
		profileRequest.addProfileField("email", "charlesb@yesmail.com");
		profileRequest.addProfileField("firstName", "Charles");
		profileRequest.addProfileField("lastName", "Berger");

		ProfileResponseWrapper profileResponseWrapper = createProfileService.createProfile(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertTrue(result.isProfileSuccess(), "Profile Success");
			assertTrue(result.isCreateProfile(), "Create Profile");
		});
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		Profile profile = profileResponse.getProfile();
		assertAll("ProfileResponse assertions", () -> {
			assertNotNull(profile.getField("email").orElse(null), "Email");
			assertNotNull(profile.getField("firstName").orElse(null), "First Name");
			assertNotNull(profile.getField("lastName").orElse(null), "Last Name");
			assertNotNull(profile.getField("cusCustomerId").orElse(null), "cusCustomerId");
			assertNotNull(profile.getField("cusCustomerIdHash").orElse(null), "cusCustomerIdHash");
			assertNotNull(profile.getField("cusCustomerUUID").orElse(null), "cusCustomerUUID");
		});
	}


}
