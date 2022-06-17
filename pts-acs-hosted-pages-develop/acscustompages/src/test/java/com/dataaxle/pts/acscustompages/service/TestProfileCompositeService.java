package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileRequestResult;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.impl.ProfileCompositeServiceImpl;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestProfileCompositeService extends BaseServiceTest {

	private ProfileCompositeService profileCompositeService;

	@BeforeEach
	void setup() throws Exception {
		super.globalSetup();
		profileCompositeService = new ProfileCompositeServiceImpl(restTemplateRequestManager, jacksonConfiguration.objectMapper());
	}

	@Test
	void resideo_buoy_new_profile() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		ProfileRequest profileRequest = new ProfileRequest("");
		String email = "charlesb@yesmail.com";
		profileRequest.addProfileField("email", email);
		profileRequest.addProfileField("firstName", "Charles");
		profileRequest.addProfileField("lastName", "Berger");
		profileRequest.setTriggeredEventId("EVTcbBuoySignupConfirmation");
		profileRequest.setTriggeredEventRecipient(email);
		profileRequest.addPersonalizationField("Source", "buoySignup");
		profileRequest.addPersonalizationField("customerIdHash", "");

		profileResponseWrapper = profileCompositeService.process(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertTrue(result.isAllSuccess(), "isAllSuccess");
			assertTrue(result.isProfileIncluded(), "Profile included");
			assertTrue(result.isProfileSuccess(), "Profile Success");
			assertFalse(result.isServicesIncluded(), "Services Included");
			assertFalse(result.isServicesSuccess(), "Services Success");
			assertFalse(result.isCustomResourcesIncluded());
			assertFalse(result.isCustomResourcesSuccess());
			assertTrue(result.isEmailTriggered(), "Email triggered");
			assertTrue(result.isEmailSuccess(), "Email success");
			assertEquals("", result.getErrorMessage(), "Error Message");
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

	@Test
	void resideo_buoy_existing_subscribed_profile() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("POST", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		ProfileResponse existingProfile = StubbedProfileRepository.search("resideo", Map.of("email", "charlesb@yesmail.com"));
		ProfileRequest profileRequest = new ProfileRequest(existingProfile.getUniqueId());
		profileRequest.addProfileField("cusBuoyPage", "Y");

		profileResponseWrapper = profileCompositeService.process(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertTrue(result.isAllSuccess(), "isAllSuccess");
			assertTrue(result.isProfileIncluded(), "Profile included");
			assertTrue(result.isProfileSuccess(), "Profile Success");
			assertFalse(result.isServicesIncluded(), "Services Included");
			assertFalse(result.isServicesSuccess(), "Services Success");
			assertFalse(result.isCustomResourcesIncluded());
			assertFalse(result.isCustomResourcesSuccess());
			assertFalse(result.isEmailTriggered(), "Email triggered");
			assertFalse(result.isEmailSuccess(), "Email success");
			assertEquals("", result.getErrorMessage(), "Error Message");
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
			assertEquals("Y", profile.getField("cusBuoyPage").orElse(""), "cusBuoyPage");
		});
	}

	@Test
	void resideo_preference_centre() {
		url = "/acscustompages/p/profile/services/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("POST", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		ProfileResponse existingProfile = StubbedProfileRepository.search("resideo", Map.of("email", "charlesb@yesmail.com"));
		ProfileRequest profileRequest = new ProfileRequest(existingProfile.getUniqueId());
		profileRequest.addServiceAction(Map.of(
			"honeywellhomeenergyreport", ServiceAction.ADD,
			"honeywellhomeasks", ServiceAction.REMOVE,
			"honeywellhomepresentedoffers", ServiceAction.REMOVE,
			"honeywellhomeemails", ServiceAction.REMOVE,
			"connectedhome", ServiceAction.REMOVE
		));
		profileRequest.addCustomResourceRequest(new CustomResourceRequest("cusHosted_pages_logs", Collections.singletonList(
			new CustomResourceRecord(Map.of(
				"email", "charlesb@yesmail.com",
				"customerId", "charlesb@yesmail.com",
				"optInAsks", false,
				"optInEmails", false,
				"optInEnergy", true,
				"optInOffers", false,
				"optInSupport", false,
				"source", "honeywellPrefCenter",
				"unsubAll", false
			))
		)));

		profileResponseWrapper = profileCompositeService.process(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertTrue(result.isAllSuccess(), "isAllSuccess");
			assertFalse(result.isProfileIncluded(), "Profile included");
			assertFalse(result.isProfileSuccess(), "Profile Success");
			assertTrue(result.isServicesIncluded(), "Services Included");
			assertTrue(result.isServicesSuccess(), "Services Success");
			assertTrue(result.isCustomResourcesIncluded());
			assertTrue(result.isCustomResourcesSuccess());
			assertFalse(result.isEmailTriggered(), "Email triggered");
			assertFalse(result.isEmailSuccess(), "Email success");
			assertEquals("", result.getErrorMessage(), "Error Message");
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
			assertTrue(profileResponse.hasCurrentService("honeywellhomeenergyreport"));
			List.of("honeywellhomeasks", "honeywellhomepresentedoffers", "honeywellhomeemails", "connectedhome")
				.forEach(name -> assertFalse(profileResponse.hasCurrentService(name), name));
		});
	}
}
