package com.dataaxle.pts.acscustompages.service.actions;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.CUS_CUSTOMER_ID_HASH;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.PKEY;
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
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResideoFreeFilter extends TestActionProcessing {

	private String product, promoCode, firstName, lastName, state, zip, pKey;

	private CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper;

	private CustomResourceResponse customResourceResponse;

	@Test
	@DisplayName("Index Page: Existing profile eligible for promotion")
	void index_existing_profile_eligible() {
		url = "http://test.resideo.com:8080/acscustompages/p/profile/search/freeFilter/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"product", "",
				"promo_code", "",
				"firstName", "",
				"lastName", "",
				"email", "",
				"state", "",
				"zip", "",
				"resident", ""
			), dynamicFormBean.getItems()
		));

		email = "charlesb@yesmail.com";
		product = "E1PRO";
		promoCode = "filter001";
		firstName = "Charles";
		lastName = "Berger";
		state = "NY";
		zip = "54321";

		// Simulate form input
		dynamicFormBean.initialiseField("product", product);
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", firstName);
		dynamicFormBean.initialiseField("lastName", lastName);
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("state", state);
		dynamicFormBean.initialiseField("zip", zip);
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
					"firstName", firstName,
					"lastName", lastName,
					"location.stateCode", state,
					"location.zipCode", zip
				), profileRequest.getProfile().getFields(),
				Collections.singletonList("cusResideo_freefiltercapture_date")
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"honeywellhomeasks", ServiceAction.ADD,
					"honeywellhomeemails", ServiceAction.ADD,
					"honeywellhomepresentedoffers", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertTrue(profileRequest.isCustomResourcesRequest(), "Has Custom Resources");
			validateCustomResourceRequests(
				Map.of(
					"cusHosted_pages_logs",
					Collections.singletonList(
						new CustomResourceRequest("cusHosted_pages_logs",
							Collections.singletonList(
								new CustomResourceRecord(
									Map.ofEntries(
										new AbstractMap.SimpleEntry<>("customerId", email),
										new AbstractMap.SimpleEntry<>("email", email),
										new AbstractMap.SimpleEntry<>("exportStatus", "N"),
										new AbstractMap.SimpleEntry<>("firstName", firstName),
										new AbstractMap.SimpleEntry<>("lastName", lastName),
										new AbstractMap.SimpleEntry<>("optInAsks", true),
										new AbstractMap.SimpleEntry<>("optInEmails", true),
										new AbstractMap.SimpleEntry<>("optInOffers", true),
										new AbstractMap.SimpleEntry<>("productName", product),
										new AbstractMap.SimpleEntry<>("promo_code", promoCode),
										new AbstractMap.SimpleEntry<>("source", "freeFilterSignup"),
										new AbstractMap.SimpleEntry<>("state", state)
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/customResource/search/freeFilter/processCode", viewName);
	}

	@Test
	@DisplayName("Index Page: New profile from ineligible state")
	void index_new_profile_ineligible() {
		url = "http://test.resideo.com:8080/acscustompages/p/profile/search/freeFilter/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
			Map.of(
				"product", "",
				"promo_code", "",
				"firstName", "",
				"lastName", "",
				"email", "",
				"state", "",
				"zip", "",
				"resident", ""
			), dynamicFormBean.getItems()
		));

		email = "charles.berger@data-axle.com";
		product = "E1PRO";
		promoCode = "filter001";
		firstName = "Charles";
		lastName = "Berger";
		state = "AK";
		zip = "54321";

		// Simulate form input
		dynamicFormBean.initialiseField("product", product);
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", firstName);
		dynamicFormBean.initialiseField("lastName", lastName);
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("state", state);
		dynamicFormBean.initialiseField("zip", zip);
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
					"email", email,
					"firstName", firstName,
					"lastName", lastName,
					"location.stateCode", state,
					"location.zipCode", zip
				), profileRequest.getProfile().getFields(),
				Collections.singletonList("cusResideo_freefiltercapture_date")
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of(
					"honeywellhomeasks", ServiceAction.ADD,
					"honeywellhomeemails", ServiceAction.ADD,
					"honeywellhomepresentedoffers", ServiceAction.ADD
				), profileRequest.getServicesRequest().getServices()
			);
			assertTrue(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			validateCustomResourceRequests(
				Map.of(
					"cusHosted_pages_logs",
					Collections.singletonList(
						new CustomResourceRequest("cusHosted_pages_logs",
							Collections.singletonList(
								new CustomResourceRecord(
									Map.ofEntries(
										new AbstractMap.SimpleEntry<>("customerId", email),
										new AbstractMap.SimpleEntry<>("email", email),
										new AbstractMap.SimpleEntry<>("exportStatus", "N"),
										new AbstractMap.SimpleEntry<>("firstName", firstName),
										new AbstractMap.SimpleEntry<>("lastName", lastName),
										new AbstractMap.SimpleEntry<>("optInAsks", true),
										new AbstractMap.SimpleEntry<>("optInEmails", true),
										new AbstractMap.SimpleEntry<>("optInOffers", true),
										new AbstractMap.SimpleEntry<>("productName", product),
										new AbstractMap.SimpleEntry<>("promo_code", promoCode),
										new AbstractMap.SimpleEntry<>("source", "freeFilterSignup"),
										new AbstractMap.SimpleEntry<>("state", state)
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests()
			);
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/page/confirm/freeFilter/confirmation", viewName);

	}

	@Test
	@DisplayName("Code page: code not found")
	void promo_code_not_found() {
		url = "http://test.resideo.com:8080/acscustompages/p/customResource/search/freeFilter/processCode";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		email = "charlesb@yesmail.com";
		customerIdHash = StubbedProfileRepository.getCustomerIdHash(customPagesRequest.getCompany(), Map.of("email", email));
		promoCode = "abcdefghi";

		// Form is preloaded with data from previous page
		dynamicFormBean.initialiseField("product", "E1PRO");
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("country", "US");
		dynamicFormBean.initialiseField("zip", "54321");
		dynamicFormBean.initialiseField("usCitizen", "");
		dynamicFormBean.initialiseField(CUSTOMER_ID_HASH, customerIdHash);

		try {
			lookupCustomResource(customPagesRequest, dynamicFormBean);
			fail("Should not find a record!");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (CustomResourceResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, dynamicFormBean, customResourceResponse);
		assertEquals("redirect:/p/page/display/freeFilter/invalidCode", viewName);

	}

	@Test
	@DisplayName("Code page: code is for incorrect product")
	void promo_code_invalid_product() {
		url = "http://test.resideo.com:8080/acscustompages/p/customResource/search/freeFilter/processCode";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		email = "charlesb@yesmail.com";
		customerIdHash = StubbedProfileRepository.getCustomerIdHash(customPagesRequest.getCompany(), Map.of("email", email));
		promoCode = "ffinvprd1";

		// Form is preloaded with data from previous page
		dynamicFormBean.initialiseField("product", "E1PRO");
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("country", "US");
		dynamicFormBean.initialiseField("zip", "54321");
		dynamicFormBean.initialiseField("usCitizen", "");
		dynamicFormBean.initialiseField(CUSTOMER_ID_HASH, customerIdHash);

		customResourceResponseWrapper = lookupCustomResource(customPagesRequest, dynamicFormBean);

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,
			customResourceResponseWrapper.getCustomResourceResponse());

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean, customResourceResponse);
		assertEquals("redirect:/p/page/display/freeFilter/invalidCode", viewName);
	}

	@Test
	@DisplayName("Code page: code already redeemed")
	void promo_code_redeemed() {
		url = "http://test.resideo.com:8080/acscustompages/p/customResource/search/freeFilter/processCode";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		email = "charlesb@yesmail.com";
		customerIdHash = StubbedProfileRepository.getCustomerIdHash(customPagesRequest.getCompany(), Map.of("email", email));
		promoCode = "ffredeemd";

		// Form is preloaded with data from previous page
		dynamicFormBean.initialiseField("product", "E1PRO");
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", "Charles");
		dynamicFormBean.initialiseField("lastName", "Berger");
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("country", "US");
		dynamicFormBean.initialiseField("zip", "54321");
		dynamicFormBean.initialiseField("usCitizen", "");
		dynamicFormBean.initialiseField(CUSTOMER_ID_HASH, customerIdHash);

		customResourceResponseWrapper = lookupCustomResource(customPagesRequest, dynamicFormBean);

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,
			customResourceResponseWrapper.getCustomResourceResponse());

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean, customResourceResponse);
		assertEquals("redirect:/p/page/display/freeFilter/invalidCode", viewName);
	}

	@Test
	@DisplayName("Code Page: code is valid")
	void promo_code_valid() {
		url = "http://test.resideo.com:8080/acscustompages/p/customResource/search/freeFilter/processCode";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);


		email = "charles.berger@data-axle.com";
		customerIdHash = StubbedProfileRepository.getCustomerIdHash(customPagesRequest.getCompany(), Map.of("email", email));
		product = "E1PRO";
		promoCode = "filter001";
		firstName = "Charles";
		lastName = "Berger";
		state = "AK";
		zip = "54321";

		// Simulate form input
		dynamicFormBean.initialiseField("product", product);
		dynamicFormBean.initialiseField("promo_code", promoCode);
		dynamicFormBean.initialiseField("firstName", firstName);
		dynamicFormBean.initialiseField("lastName", lastName);
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("state", state);
		dynamicFormBean.initialiseField("zip", zip);
		dynamicFormBean.initialiseField("usCitizen", "");
		dynamicFormBean.initialiseField(CUS_CUSTOMER_ID_HASH, customerIdHash);

		customResourceResponseWrapper = lookupCustomResource(customPagesRequest, dynamicFormBean);
		customResourceResponse = customResourceResponseWrapper.getCustomResourceResponse();

		pKey = (String) customResourceResponse.getField(PKEY);

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,
			customResourceResponseWrapper.getCustomResourceResponse());

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(customerIdHash, profileRequest.getCustomerUniqueId(), "Check customerIdHash");
			assertFalse(profileRequest.isUpdateProfile(), "Update Profile");
			assertFalse(profileRequest.isUpdateServices(), "Update Services");
			assertTrue(profileRequest.isCustomResourcesRequest(), "Has Custom Resources");
			validateCustomResourceRequests(
				Map.of(
					"cusCoupon_codes",
					Collections.singletonList(
						new CustomResourceRequest("cusCoupon_codes",
							Collections.singletonList(
								new CustomResourceRecord(
									Map.of(
										PKEY, pKey,
										"customerID", email,
										"first_name", firstName,
										"last_name", lastName,
										"status", "R"
									)
								)
							)
						)
					)
				), profileRequest.getCustomResourceRequests().getCustomResourceRequests(),
				List.of("assigned_date", "redeemed_date")
			);
			assertTrue(profileRequest.isTriggerEmail(), "Trigger Email");
			TriggerEmailData emailData = profileRequest.getTriggerEmailData();
			assertNotNull(emailData, "emailData");
			assertAll("Trigger Email data", () -> {
				assertNotNull(emailData.getEventId(), "eventId");
				assertEquals(email, emailData.getEmail());
				validateMap(
					Map.of(
						CUSTOMER_ID_HASH, customerIdHash
					), emailData.getContext()
				);
			});
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean, customResourceResponse);
		assertEquals("redirect:/p/page/confirm/freeFilter/confirmation", viewName);

	}
}
