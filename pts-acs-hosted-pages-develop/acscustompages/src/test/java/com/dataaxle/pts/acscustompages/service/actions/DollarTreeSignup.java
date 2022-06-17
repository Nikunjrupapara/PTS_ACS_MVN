package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Map;

public class DollarTreeSignup extends TestActionProcessing {

	@Test
	void newProfile() {
		url = "http://test.mailing.dollartree.com:8080/acscustompages/p/profile/search/signup/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mailing.dollartree.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"email", "",
				"zip", "",
				"cusCraftOrg", "",
				"floristOrg", "",
				"educationOrg", "",
				"nonprofitOrg", "",
				"restaurantOrg", "",
				"cateringOrg", "",
				"subscribe", ""
			), dynamicFormBean.getItems()
		));

		email = "charlesb@yesmail.com";

		// Simulate form input
		dynamicFormBean.initialiseField("subscribe", "Y");
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("zip", "24680");
		dynamicFormBean.initialiseField("cusCraftOrg", "");
		dynamicFormBean.initialiseField("floristOrg", "Y");
		dynamicFormBean.initialiseField("educationOrg", "");
		dynamicFormBean.initialiseField("nonprofitOrg", "");
		dynamicFormBean.initialiseField("restaurantOrg", "Y");
		dynamicFormBean.initialiseField("cateringOrg", "Y");

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (ProfileResponse)null);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
				Map.of(
					"email", email,
					"location.zipCode", "24680",
					"cusCraftOrg", "N",
					"cusFloristOrg", "Y",
					"cusEducationOrg", "N",
					"cusNonprofitOrg", "N",
					"cusRestaurantOrg", "Y",
					"cusCateringOrg", "Y",
					"cusNewMerchCategory", "Y"
				), profileRequest.getProfile().getFields()
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
				"DTmarketing", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertTrue(profileRequest.isTriggerEmail(), "Trigger Email");
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			assertNotNull(emailData, "emailData");
			assertAll("Trigger Email data", () -> {
				assertNotNull(emailData.getEventId(), "EVT_signupWelcome");
				assertEquals(email, emailData.getEmail());
				validateMap(
					Map.of(
						"customerIdHash", ""
					), emailData.getContext()
				);
			});
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/signup/confirm", viewName);
	}
}
