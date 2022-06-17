package com.dataaxle.pts.acscustompages.service.actions;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

public class ResideoPreferences extends TestActionProcessing {

	private static final String DELIVERY_ID_NM = "deliveryId";
	private static final String DELIVERY_ID_VAL = "DM456";

	@Test
	void service_fields_not_linked_to_profile() {
		url = String.format("http://test.resideo.com:8081/acscustompages/p/services/update/preferences/preferences?%s=%s",
			DELIVERY_ID_NM, DELIVERY_ID_VAL);
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		appDetails = customPagesRequest.getAppDetails();
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		// Seed DynamicFormBean with profile data
		email = "charlesb@yesmail.com";
		customerIdHash = "b1c8b9cb9eccec7ea8f9e03293b3523b6acdc5b530af1d886db24c1790185027";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		// Assert that dynamicFormBean is set up correctly to display the form
		assertAll("Display form assertions", () -> {
			validateMap(
				Map.ofEntries(
					new AbstractMap.SimpleEntry<>("asks", ""),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID, email),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID_HASH, customerIdHash),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_UUID, profileResponseWrapper.getProfileResponse().getField(CUS_CUSTOMER_UUID)),
					new AbstractMap.SimpleEntry<>("email", email),
					new AbstractMap.SimpleEntry<>("emails", "emails"),
					new AbstractMap.SimpleEntry<>("offers", ""),
					new AbstractMap.SimpleEntry<>("onlyenergy", ""),
					new AbstractMap.SimpleEntry<>("source", ""),
					new AbstractMap.SimpleEntry<>("support", ""),
					new AbstractMap.SimpleEntry<>("unsuball", ""),
					new AbstractMap.SimpleEntry<>(DELIVERY_ID_NM, DELIVERY_ID_VAL)
				), dynamicFormBean.getItems()
			);
			validateMap(
				Map.of(
					"honeywellhomeemails", true,
					"honeywellhomepresentedoffers", false,
					"honeywellhomeasks", false,
					"connectedhome", false,
					"honeywellhomeenergyreport", false
				), dynamicFormBean.getServices()
			);
		});

		// Simulate some activity on the form
		dynamicFormBean.initialiseField("emails", "");
		dynamicFormBean.initialiseField("offers", "offers");
		dynamicFormBean.initialiseField("asks", "asks");
		// This value is dynamically populated on the form - simulate that
		dynamicFormBean.initialiseField("source", "resideoPrefCenter");

		// Process services - ProfileResponse not available in controller, only form data
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);

		// Assertions
		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId());
			assertFalse(profileRequest.isUpdateProfile());
			assertTrue(profileRequest.isUpdateServices());
			validateMap(
				Map.of(
					"honeywellhomeemails", ServiceAction.REMOVE,
					"honeywellhomeasks", ServiceAction.ADD,
					"honeywellhomepresentedoffers", ServiceAction.ADD,
					"connectedhome", ServiceAction.REMOVE
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
										"optInEmails", false,
										"optInEnergy", false,
										"optInOffers", true,
										"optInSupport", false,
										"source", "resideoPrefCenter",
										"unsubAll", false,
										DELIVERY_ID_NM, DELIVERY_ID_VAL
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertEquals(String.format("redirect:/p/services/update/preferences/preferences?%s=%s", DELIVERY_ID_NM, DELIVERY_ID_VAL), viewName);
	}

	@Test
	void service_field_for_multiple_services() {
		url = String.format("http://test.honeywellhome.com/acscustompages/p/services/update/preferences/preferences?%s=%s",
			DELIVERY_ID_NM, DELIVERY_ID_VAL);
		//headers.put(CustomPagesConstants.FORWARDED_HDR, "");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		appDetails = customPagesRequest.getAppDetails();
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		// Seed DynamicFormBean with profile data
		email = "charlesb@yesmail.com";
		customerIdHash = "b1c8b9cb9eccec7ea8f9e03293b3523b6acdc5b530af1d886db24c1790185027";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		// Assert that dynamicFormBean is set up correctly to display the form
		assertAll("Display form assertions", () -> {
			validateMap(
				Map.ofEntries(
					new AbstractMap.SimpleEntry<>("asks", ""),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID, email),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID_HASH, customerIdHash),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_UUID, profileResponseWrapper.getProfileResponse().getField(CUS_CUSTOMER_UUID)),
					new AbstractMap.SimpleEntry<>("email", "charlesb@yesmail.com"),
					new AbstractMap.SimpleEntry<>("emails", "emails"),
					new AbstractMap.SimpleEntry<>("offers", ""),
					new AbstractMap.SimpleEntry<>("onlyenergy", ""),
					new AbstractMap.SimpleEntry<>("source", ""),
					new AbstractMap.SimpleEntry<>("support", ""),
					new AbstractMap.SimpleEntry<>("unsuball", ""),
					new AbstractMap.SimpleEntry<>(DELIVERY_ID_NM, DELIVERY_ID_VAL)
				), dynamicFormBean.getItems()
			);
			validateMap(
				Map.of(
					"honeywellhomeemails", true,
					"honeywellhomepresentedoffers", false,
					"honeywellhomeasks", false,
					"connectedhome", false,
					"honeywellhomeenergyreport", false
				), dynamicFormBean.getServices()
			);
		});

		// Simulate some activity on the form
		dynamicFormBean.initialiseField("emails", "");
		dynamicFormBean.initialiseField("onlyenergy", "onlyenergy");
		// This value is dynamically populated on the form - simulate that
		dynamicFormBean.initialiseField("source", "honeywellPrefCenter");

		// Process services - ProfileResponse not available in controller, only form data
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);

		// Assertions
		assertAll("Form submit assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId());
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"honeywellhomeemails", ServiceAction.REMOVE,
					"honeywellhomeasks", ServiceAction.REMOVE,
					"honeywellhomepresentedoffers", ServiceAction.REMOVE,
					"connectedhome", ServiceAction.REMOVE,
					"honeywellhomeenergyreport", ServiceAction.ADD
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
										"optInAsks", false,
										"optInEmails", false,
										"optInEnergy", true,
										"optInOffers", false,
										"optInSupport", false,
										"source", "honeywellPrefCenter",
										"unsubAll", false,
										DELIVERY_ID_NM, DELIVERY_ID_VAL
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
			assertFalse(profileRequest.isTriggerEmail(), "Trigger email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertEquals(String.format("redirect:/p/services/update/preferences/preferences?%s=%s", DELIVERY_ID_NM, DELIVERY_ID_VAL), viewName);
	}

	@Test
	void service_field_unsub_all() {
		url = String.format("http://test.honeywellhome.com:8081/acscustompages/p/services/update/preferences/preferences?%s=%s",
			DELIVERY_ID_NM, DELIVERY_ID_VAL);
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		appDetails = customPagesRequest.getAppDetails();
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		// Seed DynamicFormBean with profile data
		email = "charlesb@yesmail.com";
		customerIdHash = "b1c8b9cb9eccec7ea8f9e03293b3523b6acdc5b530af1d886db24c1790185027";
		getProfileRequest.addParameter("l", customerIdHash);
		profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);

		// Assert that dynamicFormBean is set up correctly to display the form
		assertAll("Display form assertions", () -> {
			validateMap(
				Map.ofEntries(
					new AbstractMap.SimpleEntry<>("asks", ""),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID, email),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_ID_HASH, customerIdHash),
					new AbstractMap.SimpleEntry<>(CUS_CUSTOMER_UUID, dynamicFormBean.getItem(CUS_CUSTOMER_UUID)),
					new AbstractMap.SimpleEntry<>("email", email),
					new AbstractMap.SimpleEntry<>("emails", "emails"),
					new AbstractMap.SimpleEntry<>("offers", ""),
					new AbstractMap.SimpleEntry<>("onlyenergy", ""),
					new AbstractMap.SimpleEntry<>("source", ""),
					new AbstractMap.SimpleEntry<>("support", ""),
					new AbstractMap.SimpleEntry<>("unsuball", ""),
					new AbstractMap.SimpleEntry<>(DELIVERY_ID_NM, DELIVERY_ID_VAL)
				), dynamicFormBean.getItems()
			);
			validateMap(
				Map.of(
					"honeywellhomeemails", true,
					"honeywellhomepresentedoffers", false,
					"honeywellhomeasks", false,
					"connectedhome", false,
					"honeywellhomeenergyreport", false
				), dynamicFormBean.getServices()
			);
		});

		// Simulate some activity on the form
		dynamicFormBean.initialiseField("emails", "");
		dynamicFormBean.initialiseField("unsuball", "unsuball");
		// This value is dynamically populated on the form - simulate that
		dynamicFormBean.initialiseField("source", "honeywellPrefCenter");

		// Process services - ProfileResponse not available in controller, only form data
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);

		// Assertions
		assertAll("Form submit assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId());
			assertFalse(profileRequest.isUpdateProfile(), "Update profile");
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"honeywellhomeemails", ServiceAction.REMOVE,
					"honeywellhomeasks", ServiceAction.REMOVE,
					"honeywellhomepresentedoffers", ServiceAction.REMOVE,
					"connectedhome", ServiceAction.REMOVE,
					"honeywellhomeenergyreport", ServiceAction.REMOVE
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
										"customerId", email,
										"email", email,
										"optInAsks", false,
										"optInEmails", false,
										"optInEnergy", false,
										"optInOffers", false,
										"optInSupport", false,
										"source", "honeywellPrefCenter",
										"unsubAll", true,
										DELIVERY_ID_NM, DELIVERY_ID_VAL
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
			assertFalse(profileRequest.isTriggerEmail(), "Trigger email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertEquals(String.format("redirect:/p/services/update/preferences/preferences?%s=%s",
			DELIVERY_ID_NM, DELIVERY_ID_VAL), viewName);
	}
}
