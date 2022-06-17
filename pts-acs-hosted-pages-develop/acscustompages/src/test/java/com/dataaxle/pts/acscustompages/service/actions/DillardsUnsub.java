package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.http.HttpMethod;
import java.util.List;
import java.util.Map;

public class DillardsUnsub extends TestActionProcessing {

	@Test
	void unsub_adhoc_user() {

		url = "http://test.web.mktgdillards.com:8081/acscustompages/p/services/update/unsubadhoc/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.web.mktgdillards.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		getProfileRequest.addParameter("l", "f81b2410d85949cad82e80d242404b423a0bdbcf9a619743b6edac27c4cb2078");
		getProfileRequest.addParameter("s","10024");

		try {
			profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		} catch (ResourceNotFoundException e) {
			fail("Should find a profile");
		}

		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);
		dynamicFormBean.initialiseField("processUnsub", "Y");
		dynamicFormBean.initialiseField("processEmailChange", "N");

		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();

		assertTrue(profileResponseWrapper.getProfileResponse().hasCurrentService("adhoc"),"Profile needs to be signup to adhoc before the test");

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,profileResponse);

		assertTrue(profileRequest.hasActions(), "ProfileRequest should have actions");
		assertFalse(profileRequest.isUpdateProfile(),"update profile is not expected");
		assertFalse(profileRequest.isTriggerEmail(), "Should not trigger email");
		assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
		assertTrue(profileRequest.isUpdateServices(), "Should update service");

		validateMap(
				Map.of(
						"adhoc", ServiceAction.REMOVE
				), profileRequest.getServicesRequest().getServices()
		);
		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertTrue(viewName.equals("redirect:/p/profile/confirm/unsubadhoc/unsubscribe-confirm"),"view name is not resubscribe");
	}

	@Test
	void unsub_user() {

		url = "http://test.web.mktgdillards.com:8081/acscustompages/p/services/update/unsubscribe/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.web.mktgdillards.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		getProfileRequest.addParameter("l", "fd45965376c54af6f6cebac4a9be15a03c98714540d2347ddff32ed2b04fd92b");
		getProfileRequest.addParameter("s","10025");

		try {
			profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		} catch (ResourceNotFoundException e) {
			fail("Should find a profile");
		}

		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);
		dynamicFormBean.initialiseField("processUnsub", "Y");
		dynamicFormBean.initialiseField("processEmailChange", "N");
		dynamicFormBean.initialiseField("cusUnsubTooManyEmails","Y");
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();

		assertTrue(profileResponseWrapper.getProfileResponse().hasCurrentService("marketing"),"Profile needs to be signup to marketing before the test");

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,profileResponse);

		assertTrue(profileRequest.hasActions(), "ProfileRequest should have actions");
		assertTrue(profileRequest.isUpdateProfile(),"update profile is expected");
		assertFalse(profileRequest.isTriggerEmail(), "Should not trigger email");
		assertFalse(profileRequest.isCustomResourcesRequest(),"No custom resource should be set");
		assertTrue(profileRequest.isUpdateServices(), "Should update service");

		validateMap(
				Map.of(
						"marketing", ServiceAction.REMOVE
				), profileRequest.getServicesRequest().getServices()
		);
		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertTrue(viewName.equals("redirect:/p/profile/confirm/unsubscribe/unsubscribe-confirm"),"view name is not resubscribe");
	}

	@Test
	void change_email_user() {
		String newEmail = "andreyk2@yesmail.com";

		url = "http://test.web.mktgdillards.com:8081/acscustompages/p/services/update/unsubadhoc/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.web.mktgdillards.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		appDetails = customPagesRequest.getAppDetails();

		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		getProfileRequest.addParameter("l", "f81b2410d85949cad82e80d242404b423a0bdbcf9a619743b6edac27c4cb2078");
		getProfileRequest.addParameter("s","10024");

		try {
			profileResponseWrapper = getProfileBySecureLinkService.getProfile(appDetails, getProfileRequest, null);
		} catch (ResourceNotFoundException e) {
			fail("Should find a profile");
		}

		ControllerUtils.initialiseForm(customPagesRequest, profileResponseWrapper, dynamicFormBean);
		dynamicFormBean.initialiseField("processUnsub", "N");
		dynamicFormBean.initialiseField("processEmailChange", "Y");
		dynamicFormBean.initialiseField("emailNew", newEmail);
		ProfileResponse profileResponse = profileResponseWrapper.getProfileResponse();

		profileRequest = ControllerUtils.prepareRequest(customPagesRequest, dynamicFormBean,profileResponse);

		assertTrue(profileRequest.hasActions(), "ProfileRequest should have actions");
		assertFalse(profileRequest.isUpdateProfile(),"update profile is not expected");
		assertFalse(profileRequest.isTriggerEmail(), "Should not trigger email");
		assertTrue(profileRequest.isCustomResourcesRequest(),"Should be custom resource request");
		assertFalse(profileRequest.isUpdateServices(), "Should not update service");


		// check if new email is in custom resource request
		CustomResourceRequest customResources = profileRequest.getCustomResourceRequests().getCustomResourceRequests().get("cusEcoa").get(0);
		assertTrue(customResources.getResourceName().equals("cusEcoa"),"customResources is not cusEcoa");

		CustomResourceRecord record = customResources.getRecords().get(0);
		assertTrue(record.getValue("newEmail").get().equals(newEmail));

		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponseWrapper.getProfileResponse(), dynamicFormBean);
		assertTrue(viewName.equals("redirect:/p/profile/confirm/unsubadhoc/change-email-confirm"),"view name is not resubscribe");
	}

}
