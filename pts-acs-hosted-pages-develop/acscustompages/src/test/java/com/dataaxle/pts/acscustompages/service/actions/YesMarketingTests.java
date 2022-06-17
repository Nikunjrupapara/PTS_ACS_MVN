package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

public class YesMarketingTests extends TestActionProcessing {

	@Test
	void service_fields_linked_to_profile_create() {
		url = "/acscustompages/p/profile/create/marketing/signup/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		// Simulate some activity on the form
		dynamicFormBean.initialiseField("email", "charlesb@yesmail.com");
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("rewardsActive", "Y");
		dynamicFormBean.initialiseField("marketingOptIn", "");

		customerIdHash = "";
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);
		// Assertions
		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId());
			assertTrue(profileRequest.isUpdateProfile(), "Updates profile");
			assertTrue(profileRequest.isUpdateServices(), "Updates services");
			validateMap(
				Map.of(
					"email", "charlesb@yesmail.com",
					"firstName", "Charles",
					"lastName", "Berger",
					"cusCustomerId", "charlesb@yesmail.com",
					"cusRewardsActive", "Y"
				), profileRequest.getProfile().getFields()
			);
			validateMap(
				Map.of(
					"yesMarketingTransactionalA", ServiceAction.ADD,
					"yesMarketingAService", ServiceAction.REMOVE,
					"yesMarketingBService",ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Has Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Has Triggered Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/marketing/signup/thankyou", viewName);
	}

	@Test
	void service_fields_linked_to_profile_update() {
		url = "/acscustompages/p/profile/update/marketing/preferences/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com");
		customPagesRequest = TestUtils.getCustomPagesRequest("GET", url, headers);
		appDetails = customPagesRequest.getAppDetails();
		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		customerIdHash = "20ec7cd2de2c5ee230659c953aec33b78bb9d791ccf566ceb6bc4e120a241913";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		// Assert that dynamicFormBean is set up correctly to display the form
		assertAll("Display form assertions", () -> {
			validateMap(
				Map.of(
					"cusCustomerIdHash", customerIdHash,
					"cusPurchaseOrderFlag", "",
					"cusRewardsActive", "Y",
					"email", "charles.berger@yesmarketing.com",
					"firstName", "Charles",
					"lastName", "Berger",
					"unsuball", "0"
				), dynamicFormBean.getItems()
			);
			validateMap(
				Map.of(
					"yesMarketingAService", true,
					"yesMarketingBService", true,
					"yesMarketingTransactionalA", false
				), dynamicFormBean.getServices()
			);
		});

		// Simulate some activity on the form
		dynamicFormBean.initialiseField("firstName", "Chuck");
		dynamicFormBean.initialiseField("cusRewardsActive", "N");
		dynamicFormBean.initialiseField("cusPurchaseOrderFlag", "transactional");

		// Process form input
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponseWrapper.getProfileResponse());

		// Assert on profile request
		assertAll("Process form assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId());
			assertTrue(profileRequest.isUpdateProfile(), "Has Profile");
			validateMap(
				Map.of(
					"cusPurchaseOrderFlag", true,
					"cusRewardsActive", "N",
					"email", "charles.berger@yesmarketing.com",
					"firstName", "Chuck",
					"lastName", "Berger",
					"unsuball", (short)0
				), profileRequest.getProfile().getFields()
			);
			assertTrue(profileRequest.isUpdateServices(), "Has Services");
			validateMap(
				Map.of(
					"yesMarketingAService", ServiceAction.REMOVE,
					"yesMarketingTransactionalA", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Has Custom Resources");
			assertTrue(profileRequest.isTriggerEmail(), "Has Triggered Email");
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			LocalDateTime now = LocalDateTime.now();
			assertAll("Trigger Email data", () -> {
				assertNotNull(emailData.getEventId(), "TestEventId");
				assertEquals("charles.berger@yesmarketing.com", emailData.getEmail());
				assertTrue(emailData.getScheduled().isBefore(now), "Scheduled");
				assertTrue(emailData.getExpiration().isAfter(now), "Expiration");
				validateMap(
					Map.of(
						"email", "charles.berger@yesmarketing.com",
						"firstName", "Chuck"
					), emailData.getContext()
				);
			});
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertEquals("redirect:/p/profile/update/marketing/preferences/index", viewName);
	}
}
