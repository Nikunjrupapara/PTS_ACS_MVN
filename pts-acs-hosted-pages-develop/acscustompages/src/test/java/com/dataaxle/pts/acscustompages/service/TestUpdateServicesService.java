package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileRequestResult;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.impl.UpdateServicesServiceImpl;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestUpdateServicesService extends BaseServiceTest {

	private UpdateServicesService updateServicesService;

	@BeforeEach
	void setup() throws Exception {
		super.globalSetup();
		updateServicesService = new UpdateServicesServiceImpl(restTemplateRequestManager, jacksonConfiguration.objectMapper());
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
			"honeywellhomeemails", ServiceAction.ADD,
			"honeywellhomepresentedoffers", ServiceAction.ADD,
			"honeywellhomeasks", ServiceAction.ADD,
			"connectedhome", ServiceAction.REMOVE,
			"honeywellhomeenergyreport", ServiceAction.REMOVE
		));

		profileResponseWrapper = updateServicesService.updateServices(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertFalse(result.isProfileIncluded(), "Profile included");
			assertFalse(result.isProfileSuccess(), "Profile Success");
			assertTrue(result.isServicesIncluded(), "Services Included");
			assertTrue(result.isServicesSuccess(), "Services Success");
			// TODO: add assertions on custom resources
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
			List.of("honeywellhomeemails", "honeywellhomepresentedoffers", "honeywellhomeasks")
				.forEach(sub -> assertTrue(profileResponse.hasCurrentService(sub), sub));
			List.of("connectedhome", "honeywellhomeenergyreport").forEach(sub -> assertFalse(profileResponse.hasCurrentService(sub), sub));
		});
	}

	@Test
	void invalid_service_name() {
		url = "/acscustompages/p/profile/services/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("POST", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		ProfileResponse existingProfile = StubbedProfileRepository.search("resideo", Map.of("email", "charles.berger@yesmail.com"));
		ProfileRequest profileRequest = new ProfileRequest(existingProfile.getUniqueId());
		profileRequest.addServiceAction("invalidServiceName", ServiceAction.ADD);

		profileResponseWrapper = updateServicesService.updateServices(appDetails, profileRequest);

		assertNotNull(profileResponseWrapper, "response not null");
		ProfileRequestResult result = profileResponseWrapper.getProfileRequestResult();
		assertAll("ProfileRequestResult assertions", () -> {
			assertFalse(result.isProfileIncluded(), "Profile included");
			assertFalse(result.isProfileSuccess(), "Profile Success");
			assertTrue(result.isServicesIncluded(), "Services Included");
			assertFalse(result.isServicesSuccess(), "Services Success");
			// TODO: add assertions on custom resources
			assertFalse(result.isEmailTriggered(), "Email triggered");
			assertFalse(result.isEmailSuccess(), "Email success");
			assertEquals("Invalid service name: invalidServiceName.", result.getErrorMessage(), "Error Message");
		});
	}
}
