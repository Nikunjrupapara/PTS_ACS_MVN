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
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Map;

public class KalmbachmediaDiscoverBlogOptIn extends TestActionProcessing {

	private static final String COMPANY = "kalmbachmedia";
	private static final String EMAIL_NEW_PROFILE = "newprofile@kalmbachmedia.com";
	private static final String EMAIL_UNSUBSCRIBED_SCIENCE = "unsubscribed-science@kalmbachmedia.com";
	private static final String EMAIL_NEVER_SUBSCRIBED_SCIENCE = "never-subscribed-science@kalmbachmedia.com";
	private static final String EMAIL_SUBSCRIBED_SCIENCE_CWDSC_YES = "subscribed-science-cuscwdsc-yes@kalmbachmedia.com";
	private static final String EMAIL_SUBSCRIBED_SCIENCE_CWDSC_EMPTY = "subscribed-science-cuscwdsc-empty@kalmbachmedia.com";
	private static final String EMAIL_SUBSCRIBED_SCIENCE_CWDSC_NO = "subscribed-science-cuscwdsc-no@kalmbachmedia.com";

	private static final String CUS_EVER_SUB_SCIENCE = "cusEverSubScience";
	private static final String CUS_CWDSC = "cusCwdsc";
	private static final String CUS_SCDSC = "cusScdsc";
	private static final String CUS_DADSC = "cusDadsc";
	private static final String CUS_OODSC = "cusOodsc";

	@BeforeAll
	static void setupBeforeAll() {
		createTestProfiles();
	}

	@BeforeEach
	void setup() {
		super.setup();
		url = "http://test.kalmbachmedia.com:8080/acscustompages/p/profile/search/discoverblog/optin/index";
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);
	}
	@Test
	@DisplayName("New profile")
	void newProfile() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""), dynamicFormBean.getItems()));

		email = EMAIL_NEW_PROFILE;

		dynamicFormBean.initialiseField("email", email);

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile!");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, (ProfileResponse) null);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New profile");
			validateMap(
				Map.of(
					"email", email,
					CUS_CWDSC, "Yes",
					CUS_SCDSC, "DSC_Blogs",
					CUS_EVER_SUB_SCIENCE, true
				), profileRequest.getProfile().getFields(), Collections.singletonList(CUS_DADSC)
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("science", ServiceAction.ADD),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/discoverblog/optin/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Existing profile currently unsubscribed from Science service")
	void existingUnsubscribedProfile() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""), dynamicFormBean.getItems()));

		email = EMAIL_UNSUBSCRIBED_SCIENCE;

		dynamicFormBean.initialiseField("email", email);

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					CUS_CWDSC, "Yes",
					CUS_SCDSC, "DSC_Blogs",
					CUS_OODSC, ""
				), profileRequest.getProfile().getFields(),
				Collections.singletonList(CUS_DADSC)
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("science", ServiceAction.ADD),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/discoverblog/optin/confirmation", viewName, "View Name");
	}

	@Test
	@DisplayName("Existing profile never subscribed to Science service")
	void existingNeverSubscribedScience() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""), dynamicFormBean.getItems()));

		email = EMAIL_NEVER_SUBSCRIBED_SCIENCE;

		dynamicFormBean.initialiseField("email", email);

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					CUS_CWDSC, "Yes",
					CUS_SCDSC, "DSC_Blogs",
					CUS_EVER_SUB_SCIENCE, true
				), profileRequest.getProfile().getFields(),
				Collections.singletonList(CUS_DADSC)
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("science", ServiceAction.ADD),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/discoverblog/optin/confirmation", viewName, "View Name");
	}

	//@Disabled
	@Test
	@DisplayName("Existing profile currently subscribed to Science service and cusCwdsc = 'Yes'")
	void existingSubscribedToScienceCusCwdscYes() {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""), dynamicFormBean.getItems()));

		email = EMAIL_SUBSCRIBED_SCIENCE_CWDSC_YES;

		dynamicFormBean.initialiseField("email", email);

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertFalse(profileRequest.hasActions(), "ProfileRequest has actions");
			/*assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					"cusCwdsc", "Yes",
					"cusScdsc", "DSC_Blogs",
					"cusOodsc", ""
				), profileRequest.getProfile().getFields(),
				Collections.singletonList("cusDadsc")
			);
			assertTrue(profileRequest.isUpdateServices(), "Update services");
			validateMap(
				Map.of("science", ServiceAction.ADD),
				profileRequest.getServicesRequest().getServices()
			);
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");*/
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/discoverblog/optin/confirmation", viewName, "View Name");

	}

	@ParameterizedTest(name = "{index} => {0}")
	@CsvSource({"subscribed-science-cuscwdsc-empty@kalmbachmedia.com", "subscribed-science-cuscwdsc-no@kalmbachmedia.com"})
	@DisplayName("Existing profile currently subscribed to Science service and cusCwdsc not set")
	//@Disabled
	void existingSubscribedToScienceCusCwdscNo(String emailParam) {
		assertAll("Display form assertions",
			() -> validateMap(Map.of("email", ""), dynamicFormBean.getItems()));

		email = emailParam;

		dynamicFormBean.initialiseField("email", email);

		ProfileResponseWrapper profileResponseWrapper = lookupProfile(customPagesRequest, dynamicFormBean);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();
		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean, profileResponse);

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals(profileResponse.getField("cusCustomerIdHash"), profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isUpdateProfile(), "Update profile");
			validateMap(
				Map.of(
					CUS_CWDSC, "Yes",
					CUS_SCDSC, "DSC_Blogs"
				), profileRequest.getProfile().getFields(),
				Collections.singletonList(CUS_DADSC)
			);
			assertFalse(profileRequest.isUpdateServices(), "Update services");
			/*validateMap(
				Map.of("science", ServiceAction.ADD),
				profileRequest.getServicesRequest().getServices()
			);*/
			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
			assertFalse(profileRequest.isTriggerEmail(), "Trigger Email");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/discoverblog/optin/confirmation", viewName, "View Name");
	}

	static void createTestProfiles() {
		// Existing profile currently unsubscribed from Science service
		StubbedProfileRepository.addProfile(COMPANY,
			StubbedProfileRepository.createProfile(COMPANY,
				Map.of(
					"email", EMAIL_UNSUBSCRIBED_SCIENCE,
					CUS_EVER_SUB_SCIENCE, true
				)
			)
		);
		// Existing profile never subscribed to Science service
		StubbedProfileRepository.addProfile(COMPANY,
			StubbedProfileRepository.createProfile(COMPANY,
				Map.of(
					"email", EMAIL_NEVER_SUBSCRIBED_SCIENCE,
					CUS_EVER_SUB_SCIENCE, false
				),
				Collections.singletonList("hobby")
			)
		);
		// Existing subscriber subscribed to Science & cusCwdsc = "Yes"
		StubbedProfileRepository.addProfile(COMPANY,
			StubbedProfileRepository.createProfile(COMPANY,
				Map.of(
					"email", EMAIL_SUBSCRIBED_SCIENCE_CWDSC_YES,
					CUS_EVER_SUB_SCIENCE, true,
					CUS_CWDSC, "Yes"
				),
				Collections.singletonList("science")
			)
		);
		// Existing subscriber subscribed to Science & cusCwdsc = ""
		StubbedProfileRepository.addProfile(COMPANY,
			StubbedProfileRepository.createProfile(COMPANY,
				Map.of(
					"email", EMAIL_SUBSCRIBED_SCIENCE_CWDSC_EMPTY,
					CUS_EVER_SUB_SCIENCE, true,
					CUS_CWDSC, ""
				),
				Collections.singletonList("science")
			)
		);
		// Existing subscriber subscribed to Science & cusCwdsc = "No"
		StubbedProfileRepository.addProfile(COMPANY,
			StubbedProfileRepository.createProfile(COMPANY,
				Map.of(
					"email", EMAIL_SUBSCRIBED_SCIENCE_CWDSC_NO,
					CUS_EVER_SUB_SCIENCE, true,
					CUS_CWDSC, "No"
				),
				Collections.singletonList("science")
			)
		);
	}
}
