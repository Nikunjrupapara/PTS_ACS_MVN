package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KalmbachmediaPreference  extends TestActionProcessing{


	/*
		Given a ValueGenerator for a field
		And ValueGenerator is configured as ignoreWhenEmpty
		And the source value is empty
		When the form is submitted
		Then the field is excluded from the request payload
	*/

	@Test
	void signupTestEmpty() {
		url = "http://test.kalmbachmedia.com:8081/acscustompages/p/profile/create/preferences/test_signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCat_aircraft", "",
						"cusCat_armors","",
						"cusCwctr", "",
						"cusDactr", "",
						"cusScctr", "",
						"cusCwctt","",
						"cusDactt","",
						"cusScctt",""
						//,"scienceOptIn",""
				), dynamicFormBean.getItems()
		));

		email = "anreyk@yesmail.com";

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("cat_aircraft", "");
		dynamicFormBean.initialiseField("cat_armors","");
		dynamicFormBean.initialiseField("cwctr", "");
		dynamicFormBean.initialiseField("dactr", "");
		dynamicFormBean.initialiseField("scctr", "");
		dynamicFormBean.initialiseField("scienceOptIn", "Y");

		dynamicFormBean.initialiseField("cwctt","");
		dynamicFormBean.initialiseField("dactt","");
		dynamicFormBean.initialiseField("scctt","");

		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
					Map.of(
							"email", email
					), profileRequest.getProfile().getFields()
			);

			validateNotInMap(
					Set.of(
							"cusCat_aircraft",
							"cusScctr",
							"cusCwctr",
							"cusDactr",
							"cusCwctt",
							"cusDactt",
							"cusScctt",
							"cusCat_armors"
							), profileRequest.getProfile().getFields()
			);

			assertTrue(profileRequest.isUpdateServices(), "Update services");

			validateMap(
					Map.of(
							"science", ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

			validateNotInMap(
					Set.of(
							"hobby"
					), profileRequest.getServicesRequest().getServices()
			);

			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName);
	}


	/*
		Given a ValueGenerator for a field
		And ValueGenerator is configured as ignoreWhenEmpty
		And the source value is not empty
		When the form is submitted
		Then the field is included in the request payload
	*/

	@Test
	void signupTestWithValues() {
		url = "http://test.kalmbachmedia.com:8081/acscustompages/p/profile/create/preferences/test_signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCat_aircraft", "",
						"cusCat_armors","",
						"cusCwctr", "",
						"cusDactr", "",
						"cusScctr", "",
						"cusCwctt","",
						"cusDactt","",
						"cusScctt",""
						//,"scienceOptIn",""
				), dynamicFormBean.getItems()
		));

		email = "anreyk@yesmail.com";

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("cusCat_aircraft", "Yes");
		dynamicFormBean.initialiseField("cusCat_armors","Yes");
		dynamicFormBean.initialiseField("cusCwctr", "Yes");
		dynamicFormBean.initialiseField("cusDactr", "2021-06-03'T'12:12:00");
		dynamicFormBean.initialiseField("cusScctr", "prefcenter");
		dynamicFormBean.initialiseField("scienceOptIn", "Y");
		dynamicFormBean.initialiseField("hobbyOptIn", "Y");

		dynamicFormBean.initialiseField("cusCwctt","Yes");
		dynamicFormBean.initialiseField("cusDactt","2021-06-03'T'12:12:00");
		dynamicFormBean.initialiseField("cusScctt","prefcenter");




		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
					Map.of(
							"email", email,
							"cusCat_aircraft", "Yes",
							"cusScctr","prefcenter",
							"cusCwctr", "Yes",
							"cusDactr", "2021-06-03'T'12:12:00",
							"cusCwctt","Yes",
							"cusDactt","2021-06-03'T'12:12:00",
							"cusScctt","prefcenter",
							"cusCat_armors","Yes"
					), profileRequest.getProfile().getFields()
			);

			assertTrue(profileRequest.isUpdateServices(), "Update services");

			validateMap(
					Map.of(
							"science", ServiceAction.ADD,
							"hobby", ServiceAction.ADD
					), profileRequest.getServicesRequest().getServices()
			);

//			validateNotInMap(
//					Set.of(
//							"hobby"
//					), profileRequest.getServicesRequest().getServices()
//			);

			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName);
	}



	/*
		Given a ValueGenerator for a field
		And ValueGenerator is not configured as ignoreWhenEmpty
		And the source value is empty
		When the form is submitted
		Then the field is included in the request payload
	*/

	@Test
	void prefTestEmpty() {
		url = "http://test.kalmbachmedia.com:8081/acscustompages/p/profile/create/preferences/test_pref";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCat_aircraft", "",
						"cusCat_armors","",
						"cusCwctr", "",
						"cusDactr", "",
						"cusScctr", "",
						"cusCwctt","",
						"cusDactt","",
						"cusScctt",""
//						,"cusScienceOptIn",""
				), dynamicFormBean.getItems()
		));

		email = "anreyk@yesmail.com";

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("cusCat_aircraft", "");
		dynamicFormBean.initialiseField("cusCat_armors","");
		dynamicFormBean.initialiseField("cusCwctr", "");
		dynamicFormBean.initialiseField("cusDactt", "");
		dynamicFormBean.initialiseField("cusScctt", "");


		dynamicFormBean.initialiseField("scienceOptIn", "");
		dynamicFormBean.initialiseField("hobbyOptIn", "");

		dynamicFormBean.initialiseField("cusCwctt","");
		dynamicFormBean.initialiseField("cusDactt","");
		dynamicFormBean.initialiseField("cusScctt","");



		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
					Map.of(
							"email", email,
							"cusCat_aircraft", "",
							"cusScctr","",
							"cusCwctr", "",
							"cusDactr", "",
							"cusCwctt","",
							"cusDactt","",
							"cusScctt","",
							"cusCat_armors",""
					), profileRequest.getProfile().getFields()
			);

			assertFalse(profileRequest.isUpdateServices(), "Update services");


//			validateMap(
//					Map.of(
//							"science", ServiceAction.ADD
////							,"hobby", ServiceAction.REMOVE
//					), profileRequest.getServicesRequest().getServices()
//			);

			validateNotInMap(
					Set.of(
							"hobby",
							"science"
					), profileRequest.getServicesRequest().getServices()
			);

			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName);
	}


	/*
		Given a ValueGenerator for a field
		And ValueGenerator is not configured as ignoreWhenEmpty
		And the source value is not empty
		When the form is submitted
		Then the field is included in the request payload
	*/

	@Test
	void prefTestWithValues() {
		url = "http://test.kalmbachmedia.com:8081/acscustompages/p/profile/create/preferences/test_pref";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.kalmbachmedia.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean);

		assertAll("Display form assertions", () -> validateMap(
				Map.of(
						"email", "",
						"cusCat_aircraft", "",
						"cusCat_armors","",
						"cusCwctr", "",
						"cusDactr", "",
						"cusScctr", "",
						"cusCwctt","",
						"cusDactt","",
						"cusScctt",""
						//,"scienceOptIn",""
				), dynamicFormBean.getItems()
		));

		email = "anreyk@yesmail.com";

		// Simulate form input
		dynamicFormBean.initialiseField("email", email);
		dynamicFormBean.initialiseField("cusCat_aircraft", "Yes");
		dynamicFormBean.initialiseField("cusCat_armors","Yes");
		dynamicFormBean.initialiseField("cusCwctr", "Yes");
		dynamicFormBean.initialiseField("cusDactr", "2021-06-03'T'12:12:00");
		dynamicFormBean.initialiseField("cusScctr", "prefcenter");
		dynamicFormBean.initialiseField("scienceOptIn", "Y");

		dynamicFormBean.initialiseField("cusCwctt","Yes");
		dynamicFormBean.initialiseField("cusDactt","2021-06-03'T'12:12:00");
		dynamicFormBean.initialiseField("cusScctt","prefcenter");




		try {
			lookupProfile(customPagesRequest, dynamicFormBean);
			fail("Should not find a profile");
		} catch (ResourceNotFoundException e) {
			profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean);
		}

		assertAll("Form submission assertions", () -> {
			assertTrue(profileRequest.hasActions(), "ProfileRequest has actions");
			assertEquals("", profileRequest.getCustomerUniqueId(), "Unique Id");
			assertTrue(profileRequest.isNewProfile(), "New Profile");
			validateMap(
					Map.of(
							"email", email,
							"cusCat_aircraft", "Yes",
							"cusScctr","prefcenter",
							"cusCwctr", "Yes",
							"cusDactr", "2021-06-03'T'12:12:00",
							"cusCwctt","Yes",
							"cusDactt","2021-06-03'T'12:12:00",
							"cusScctt","prefcenter",
							"cusCat_armors","Yes"
					), profileRequest.getProfile().getFields()
			);


			assertTrue(profileRequest.isUpdateServices(), "Update services");

			validateMap(
					Map.of(
							"science", ServiceAction.ADD
//							,"hobby", ServiceAction.REMOVE
					), profileRequest.getServicesRequest().getServices()
			);

			validateNotInMap(
					Set.of(
							"hobby"
					), profileRequest.getServicesRequest().getServices()
			);

			assertFalse(profileRequest.isCustomResourcesRequest(), "Custom Resources");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, null, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/preferences/confirm", viewName);
	}
}
