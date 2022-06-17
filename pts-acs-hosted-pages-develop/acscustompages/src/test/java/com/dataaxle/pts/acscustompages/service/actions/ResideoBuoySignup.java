package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class ResideoBuoySignup extends TestActionProcessing {

	@Test
	void signup_existing_subbed_user() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"email", "",
				"firstName", "",
				"lastName", "",
				"usCitizen", false
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", "charlesb@yesmail.com");
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("usCitizen", "");

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					"cusBuoyPage", "Y"
				), profileRequest.getProfile().getFields()
			);
			assertFalse(profileRequest.isUpdateServices(), "Update services");
			assertFalse(profileRequest.isCustomResourcesRequest(), "Has Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/buoySignup/subscribed", viewName);
	}

	@Test
	void signup_existing_unsubbed_user() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"email", "",
				"firstName", "",
				"lastName", "",
				"usCitizen", false
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", "charles.berger@yesmail.com");
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("usCitizen", "");

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions());
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/email/buoySignup/unsubscribed", viewName);
	}

	@Test
	void signup_new_user() {
		url = "/acscustompages/p/profile/search/buoySignup/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"email", "",
				"firstName", "",
				"lastName", "",
				"usCitizen", false
			), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", "charles.berger@data-axle.com");
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("usCitizen", "");

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile!");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (ProfileResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
				Map.of(
					"email", "charles.berger@data-axle.com",
					"firstName", "Charles",
					"lastName", "Berger"
				), profileRequest.getProfile().getFields()
			);
			assertFalse(profileRequest.isUpdateServices(), "Update services");
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertTrue(profileRequest.isTriggerEmail(), "Trigger Email");
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			assertNotNull(emailData, "emailData");
			assertAll("Trigger Email data", () -> {
				assertNotNull(emailData.getEventId(), "eventId");
				assertEquals("charles.berger@data-axle.com", emailData.getEmail());
				validateMap(
					Map.of(
						"Source", "buoyEmailSignup",
						"customerIdHash", ""
					), emailData.getContext()
				);
			});
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/buoySignup/almostDone", viewName);
	}

	@Test
	void confirmation() {
		url = "/acscustompages/p/profile/secure/buoySignup/confirmation?l=XXX&s=YYY";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		customerIdHash = "20ec7cd2de2c5ee230659c953aec33b78bb9d791ccf566ceb6bc4e120a241913";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		email = (String)profileResponse.getField("email");
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Page processing assertions", () -> {
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"cusBuoyPage", "Y"
				), profileRequest.getProfile().getFields()
			);
			validateMap(Map.of(
				"honeywellhomeasks", ServiceAction.ADD,
				"honeywellhomeemails", ServiceAction.ADD,
				"honeywellhomepresentedoffers", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertTrue(profileRequest.isCustomResourcesRequest(), "Has custom resources");
			validateCustomResourceRequests(
				Map.of(
					"cusHosted_pages_logs",
					Collections.singletonList(
						new CustomResourceRequest("cusHosted_pages_logs",
							Collections.singletonList(
								new CustomResourceRecord(
									Map.of(
										"email", email,
										"customerId", email,
										"optInAsks", true,
										"optInEmails", true,
										"optInEnergy", false,
										"optInOffers", true,
										"optInSupport", false,
										"source", "buoyEmailSignup",
										"unsubAll", false
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"email", "charles.berger@yesmarketing.com"
			), dynamicFormBean.getItems()
		));

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		//assertEquals("/p/profile/secure/buoySignup/confirmation", viewName);
		assertEquals("/companies/resideo/buoySignup/confirmation", viewName);
	}
}
