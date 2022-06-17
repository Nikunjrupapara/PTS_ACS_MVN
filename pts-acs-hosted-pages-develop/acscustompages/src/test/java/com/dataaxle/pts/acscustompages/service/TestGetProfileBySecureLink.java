package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.service.impl.GetProfileBySecureLinkServiceImpl;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestGetProfileBySecureLink extends BaseServiceTest {

	private String lookupValue;

	private String securityValue;

	private String view;

	private GetProfileBySecureLinkService getProfileBySecureLinkService;

	private GetProfileRequest getProfileRequest;

	@BeforeEach
	void setup() throws Exception {
		super.globalSetup();
		getProfileBySecureLinkService = new GetProfileBySecureLinkServiceImpl(restTemplateRequestManager,
			jacksonConfiguration.objectMapper());

		getProfileRequest = new GetProfileRequest();
	}

	@Test
	void ymnewsolutions_charlesb11() {
		lookupValue = "1FAC74C1F1A03F4B095DC7703E5BBD1096AF6B624EAC91592008F35C263DB46AEF59EB6E1FFDD1B94DD9977A2218932225EDCDE7AA6357BEC88CCF2F795BE372";
		securityValue = "be643fa5-6459-4771-8569-f3cebdd76187";
		view = "services";
		url = "/acscustompages/p/profile/update/marketing/preferences/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		getProfileRequest.addParameter("l", lookupValue);
		getProfileRequest.addParameter("s", securityValue);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, view);
		assertTrue(profileResponseWrapper.getProfileRequestResult().isProfileFound(), "profile found");
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		assertNotNull(profileResponse, "profile response not null");
		assertAll("profileResponse assertions", () -> {
			assertNotNull(profileResponse.getField("acsId"));
			assertEquals("charlesb+11@yesmail.com", profileResponse.getField("email"), "email");
			Arrays.asList("cusCbCustomerProfileLink.href", "geoUnit.pkey", "location.address1", "orgUnit.pkey", "postalAddress.line1")
				.forEach(name -> assertNotNull(profileResponse.getField(name), String.format("%s is not null", name)));
			assertEquals(1, profileResponse.getCurrentServices().size(), "currentServices.size()");
			CurrentService service = profileResponse.getCurrentServices().get(0);
			assertNotNull(service.getCreated(), "created");
			assertNotNull(service.getLabel(), "label");
			assertNotNull(service.getName(), "name");
		});
	}

	@Test
	void ymnewsolutions_notfound() {
		lookupValue = "XXXc74c1f1a03f4b095dc7703e5bbd1096af6b624eac91592008f35c263db46aef59eb6e1ffdd1b94dd9977a2218932225edcde7aa6357bec88ccf2f795be372";
		securityValue = "bdb64076-2052-4762-90da-8ab56c35c30c";
		view = "services";
		url = "/acscustompages/p/profile/update/marketing/preferences/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		getProfileRequest.addParameter("l", lookupValue);
		getProfileRequest.addParameter("s", securityValue);
		assertThrows(ResourceNotFoundException.class, () -> getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, view));
	}
}
