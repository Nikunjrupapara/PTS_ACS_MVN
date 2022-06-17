package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import lombok.Data;
import lombok.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class KalmbachmediaBeginner extends TestActionProcessing{



	@DisplayName("Profile does not exist case")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileDoesNotExists(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum-1)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("cusCustomerIdHash", "");

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,(ProfileResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");

			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateNotInMap(
					Set.of(
							"cusBgn_asy"
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}



	@DisplayName("Profile exists but was never subscribed")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileExistsNotNeverSubscribed(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),false
						), profResponse.getProfileResponse().getProfile().getFields()
				);
				validateNotInMap(
						Set.of(
								brandAssociations.getBeginFlagName(),
								brandAssociations.getDoneFlagName()
						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");

			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateNotInMap(
					Set.of(
							brandAssociations.getDoneFlagName()
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile unsubscribed, no initial flags")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileUnsubscribed(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+1)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true
						), profResponse.getProfileResponse().getProfile().getFields()
				);
				validateNotInMap(
						Set.of(
								brandAssociations.getBeginFlagName(),
								brandAssociations.getDoneFlagName()
						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(!profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is not currently subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);


		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile unsubscribed, beginner Yes and done No")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileUnsubscribed2(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+2)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),"No"
						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(!profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is not currently subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);


		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile unsubscribed, beginner Yes and done Yes")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileUnsubscribed3(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+3)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),"Yes"
						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(!profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is not currently subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);


		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile subscribed and done Yes")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileSubscribedAndDoneYes(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+4)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),"Yes"
						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile subscribed and done empty")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileSubscribedAndDoneEmpty(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+5)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),""

						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile subscribed and done null")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileSubscribedAndDoneNull(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+6)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes"
						), profResponse.getProfileResponse().getProfile().getFields()
				);
				validateNotInMap(
						Set.of(brandAssociations.getDoneFlagName()),profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertTrue(profileRequest.isTriggerEmail(), "Should trigger email");
			assertTrue(profileRequest.getTriggeredEventId().equals(brandAssociations.getTriggerEventId()));
			assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
			assertTrue(profileRequest.isUpdateServices(), "Should update service");
			validateMap(
					Map.of(
							brandAssociations.getBeginFlagName(), "Yes",
							brandAssociations.getDoneFlagName(), "No",
							"email", email,
							brandAssociations.getEverSubFlagName(),true
					), profileRequest.getProfile().getFields()
			);
			validateMap(
					Map.of(
							brandAssociations.getService(), ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile subscribed and done No")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileSubscribedAndDoneNo(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+7)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),"No"

						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(!profileRequest.hasActions(), "Should not have actions as user is not Done");
		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	@DisplayName("Profile subscribed and done no")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("testArgs")
	void profileSubscribedAndDoneNo2(String brand, int brandUniqueNum) {
		email = brand+"_kalmbach_"+(brandUniqueNum+8)+"@testc.com";
		BrandAssociations brandAssociations = new BrandAssociations(brand);

		url = brandAssociations.getUrl();
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCustomerIdHash", ""
				), dynamicFormBean.getItems()
		));

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
//		dynamicFormBean.initialiseField("cusCustomerIdHash", cusCustomerIdHash);
		try {
			ProfileResponseWrapper profResponse = lookupProfile(customPagesRequest, dynamicFormBean);

			assertAll("Validate if initial profile is correct", () -> {
				validateMap(
						Map.of(
								"email", email,
								brandAssociations.getEverSubFlagName(),true,
								brandAssociations.getBeginFlagName(),"Yes",
								brandAssociations.getDoneFlagName(),"no"

						), profResponse.getProfileResponse().getProfile().getFields()
				);
			});
			assertTrue(profResponse.getProfileResponse().hasCurrentService(brandAssociations.getService()),"Precondition for this case that profile is subscribed");

			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profResponse.getProfileResponse());

		} catch (ResourceNotFoundException e) {
			fail("Profile should exist");
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(!profileRequest.hasActions(), "Should not have actions as user is not Done");
		});
		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals(brandAssociations.getSuccessUrl(), viewName);
	}

	static Stream<Arguments> testArgs() {
		return Stream.of(
				arguments("asy", 100),
				arguments("ctt", 200),
				arguments("fsm", 300),
				arguments("mrr", 400)
		);
	}


	@Value
	private class BrandAssociations{

		 String service;
		 String beginFlagName;
		 String doneFlagName;
		 String triggerEventId;
		 String successUrl;
		 String url;
		 String everSubFlagName;

		public BrandAssociations(String brand){
			successUrl = "redirect:/p/profile/confirm/beginner/"+brand+"/confirm";
			url = "http://test.kalmbachmedia.com:8081/acscustompages/p/profile/search/beginner/"+brand+"/signup";
			switch(brand){
				case "asy":
					triggerEventId = "EVTBGN00_ASY_000000_0STEP1";
					service = "science";
					beginFlagName = "cusBgn"+brand;
					doneFlagName = "cusBgn_"+brand;
					everSubFlagName = "cusEverSubScience";
				break;
				case "ctt":
					triggerEventId = "EVTBGN00_CTT_000000_0STEP1";
					service = "hobby";
					beginFlagName = "cusBgn"+brand;
					doneFlagName = "cusBgn_"+brand;
					everSubFlagName = "cusEverSubHobby";
				break;
				case "fsm":
					triggerEventId = "EVTBGN00_FSM_000000_0Step0_THKYOU";
					service = "hobby";
					beginFlagName = "cusBgn"+brand;
					doneFlagName = "cusBgn_"+brand;
					everSubFlagName = "cusEverSubHobby";
					break;
				case "mrr":
					triggerEventId = "EVTBGN00_MRR_000000_0STEP1";
					service = "hobby";
					beginFlagName = "cusBgn"+brand;
					doneFlagName = "cusBgn_"+brand;
					everSubFlagName = "cusEverSubHobby";
					break;
				default:
					triggerEventId = "";
					service = "";
					beginFlagName = "";
					doneFlagName = "";
					everSubFlagName = "";
			}
		}
	}

}
