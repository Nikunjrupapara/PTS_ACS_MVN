package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.GetProfileCustomerIdService;
import com.dataaxle.pts.acscustompages.stubs.StubbedGetProfileByCustomerIdService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UsBankPreferences extends TestActionProcessing {

	private GetProfileCustomerIdService getProfileCustomerIdService;

	@BeforeEach
	void setup_local() {
		getProfileCustomerIdService = new StubbedGetProfileByCustomerIdService();
	}

	@Test
	void signup_not_found() {
		url = "/acscustompages/p/profile/create/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"cusAcctL4", "",
				"cusPartner", "",
				"email", "",
				"confirmEmail", ""
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		String email = "x@y.com";
		String acctL4 = "1234";
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("confirmEmail", email);
		dynamicFormBean.initialiseField("cusAcctL4", acctL4);

		try {
			GetProfileRequest getProfileRequest = new GetProfileRequest();
			getProfileRequest.addParameter("email", email);
			getProfileRequest.addParameter("cusAcctL4", acctL4);
			getProfileRequest.addParameter("cusPartner", "BMW");
			profileResponseWrapper = getProfileCustomerIdService.getProfile(appDetails, getProfileRequest);
			fail("Should not be found!");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (ProfileResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertNotNull(profileRequest);
			assertFalse(profileRequest.hasActions());
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/create/preferences/signup-new", viewName);
	}

	@Test
	void signup_found() {
		url = "/acscustompages/p/profile/create/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"cusAcctL4", "",
				"cusPartner", "",
				"email", "",
				"confirmEmail", ""
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		String email = "charlesb@yesmail.com";
		String acctL4 = "1234";
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("confirmEmail", email);
		dynamicFormBean.initialiseField("cusAcctL4", acctL4);

		try {
			GetProfileRequest getProfileRequest = new GetProfileRequest();
			getProfileRequest.addParameter("email", email);
			getProfileRequest.addParameter("cusAcctL4", acctL4);
			getProfileRequest.addParameter("cusPartner", "BMW");
			profileResponseWrapper = getProfileCustomerIdService.getProfile(appDetails, getProfileRequest);
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponseWrapper.getProfileResponse());
		} catch (ResourceNotFoundException e) {
			fail("Should not be found!");
		}

		assertAll("Form submission assertions", () -> {
			assertNotNull(profileRequest);
			assertFalse(profileRequest.hasActions());
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean);
		assertEquals("redirect:/p/services/update/preferences/preferences", viewName);
	}

	@Test
	void signup_new_create_profile() {
		url = "/acscustompages/p/profile/create/preferences/signup-new";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		String acctL4 = "1234";
		String partnerId = "BMW";
		DynamicFormBean fromPrevPage = new DynamicFormBean(customPagesRequest);
		fromPrevPage.setItems(Map.of(
			"email", "test@test.com",
			"cusAcctL4", acctL4,
			"cusPartner", partnerId
		));

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		ControllerUtils.initialiseForm(customPagesRequest, fromPrevPage, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"cusAcctL4", acctL4,
				"cusOver18", false,
				"cusPartner", partnerId,
				"cusProfileKey", "",
				"cusZipCode", "",
				"email", "test@test.com",
				"firstName", "",
				"lastName", ""
			), dynamicFormBean.getItems()
		));

		String profileKey = String.format("%s|%s|%s", email, acctL4, partnerId);

		// Simulate form input
		dynamicFormBean.initialiseField("firstName", "Test");
		dynamicFormBean.initialiseField("lastName", "Test");
		dynamicFormBean.initialiseField("cusProfileKey", profileKey);
		dynamicFormBean.initialiseField("cusZipCode", "12345");
		dynamicFormBean.initialiseField("over18", "");

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId());
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					"cusAcctL4", acctL4,
					"cusPartner", partnerId,
					"cusPostalCode", "12345",
					"cusProfileKey", profileKey,
					"cusSignUpHostedPage", "Yes",
					"email", "test@test.com",
					"firstName", "Test",
					"lastName", "Test"
				), profileRequest.getProfile().getFields()
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"marketing", ServiceAction.ADD,
					"service", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices());
			assertFalse(profileRequest.isCustomResourcesRequest(), "Has custom resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
/*
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			assertNotNull(emailData, "emailData");
			assertAll("Trigger Email data", () -> {
				assertEquals("EVTsign_uppageconfirmation_bmw", emailData.getEventId(), "eventId");
				assertEquals("test@test.com", emailData.getEmail());
				validateMap(
					Map.of(
						"customerIdHash", ""
					), emailData.getContext()
				);
			});
*/
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName, "View name");
	}

	@Test
	void preferences_subscribe() {
		url = "/acscustompages/p/profile/create/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		DynamicFormBean fromPrevPage = new DynamicFormBean(customPagesRequest);
		fromPrevPage.setItems(Map.of(
			"email", "test@test.com",
			"cusAcctL4", "1234",
			"cusPartner", "BMW"
		));

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		customerIdHash = "cca25f773bc049ec72639017bc4556d6ade94c175f771e534b6361e68e9fc3d1";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"cusAcctL4", "4321",
				"cusPartner", "BMW",
				"email", "charles.berger@yesmail.com",
				"cusCustomerIdHash", customerIdHash,
				"radios", "N"
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("radios", "Y");

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean/*, profileResponse*/);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId(), "CustomerIdHash");
			assertFalse(profileRequest.isUpdateProfile(), "Update Profile");
			assertTrue(profileRequest.isUpdateServices(), "Update Services");
			validateMap(
				Map.of(
					"marketing", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
/*
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			assertNotNull(emailData, "emailData");
			assertAll("Trigger Email data", () -> {
				assertEquals("EVTsign_uppageconfirmation_bmw", emailData.getEventId(), "eventId");
				assertEquals("charles.berger@yesmail.com", emailData.getEmail());
				validateMap(
					Map.of(
						"customerIdHash", customerIdHash
					), emailData.getContext()
				);
			});
 */
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName);
	}

	@Test
	void preferences_unsubscribe() {
		url = "/acscustompages/p/profile/create/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		DynamicFormBean fromPrevPage = new DynamicFormBean(customPagesRequest);
		fromPrevPage.setItems(Map.of(
			"email", "test@test.com",
			"cusAcctL4", "1234",
			"cusPartner", "BMW"
		));

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		customerIdHash = "87af32991d7ece5695cdf7177b3c058053c44d097f67cbe6f0364323153f141c";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"cusAcctL4", "1234",
				"cusPartner", "BMW",
				"email", "charlesb@yesmail.com",
				"cusCustomerIdHash", customerIdHash,
				"radios", "Y"
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("radios", "N");

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId(), "CustomerIdHash");
			assertFalse(profileRequest.isUpdateProfile(), "Update Profile");
			assertTrue(profileRequest.isUpdateServices(), "Update Services");
			validateMap(
				Map.of(
					"marketing", ServiceAction.REMOVE
				), profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/update/preferences/unsub-survey", viewName);
	}
}
