package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Map;

public class KodakMomentsMemberUnsub extends TestActionProcessing {

	@BeforeEach
	void setup() {
		super.setup();
		url = "http://test.kodakmoments.com:8080/acscustompages/p/profile/search/member-unsub/index";
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);
	}

	@Test
	@DisplayName("Non existent profile")
	void profileNotFound() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""/*, "all", "", "one", ""*/), dynamicFormBean.getItems()));

		email = "doesnotexist@yesmail.com";

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile!");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (ProfileResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New profile");
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertFalse(profileRequest.isUpdateServices(), "Update services");
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/member-unsub/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Profile exists and no checkboxes ticked")
	void noCheckboxesTicked() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""/*, "all", "", "one", ""*/), dynamicFormBean.getItems()));

		email = "charlesb@yesmail.com";

		dynamicFormBean.initialiseField("email", email);

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			//assertTrue(profileRequest.getProfile().getFields().isEmpty(), "Profile fields is empty");
			assertFalse(profileRequest.isUpdateServices(), "Update services");
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/member-unsub/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Profile Exists and 'I no longer want to receive emails' checkbox ticked")
	void unsubMarketingOnly() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""/*, "all", "", "one", ""*/), dynamicFormBean.getItems()));

		email = "charlesb@yesmail.com";

		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("all", "1");

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.getProfile().getFields().isEmpty(), "Profile fields is empty");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("marketing", ServiceAction.REMOVE, "kmart-au", ServiceAction.REMOVE),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/member-unsub/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Profile exists and 'I no longer want to receive 10% off all orders' checkbox ticked")
	void unsubMemberOnly() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""/*, "all", "", "one", ""*/), dynamicFormBean.getItems()));

		email = "charlesb@yesmail.com";

		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("one", "1");

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.getProfile().getFields().isEmpty(), "Profile fields is empty");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("Member", ServiceAction.REMOVE),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/member-unsub/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Profile exists and both check boxes ticked")
	void unsubBothMarketingAndMember() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""/*, "all", "", "one", ""*/), dynamicFormBean.getItems()));

		email = "charlesb@yesmail.com";

		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("all", "1");
		dynamicFormBean.initialiseField("one", "1");

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.getProfile().getFields().isEmpty(), "Profile fields is empty");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("marketing", ServiceAction.REMOVE, "kmart-au", ServiceAction.REMOVE, "Member", ServiceAction.REMOVE),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/member-unsub/confirmation", viewName, "View Name");
	}
}
