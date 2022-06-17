package com.dataaxle.pts.acscustompages.service.actions;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_ID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.dataaxle.pts.acscustompages.controller.ControllerUtils;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.RequestResult;
import com.dataaxle.pts.acscustompages.model.RequestType;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultViewNameService;
import com.dataaxle.pts.acscustompages.stubs.StubbedCustomResourceService;
import com.dataaxle.pts.acscustompages.stubs.StubbedGetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import com.google.common.hash.Hashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BoscovsCouponPage extends TestActionProcessing {

	private String url, lValue, sValue, barcode;

	private GetCustomResourceRequest getCustomResourceRequest;

	private GetCustomResourceByIdService getCustomResourceService;

	private CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper;

	private CustomResourceRequest updateRequest;

	@BeforeEach
	void setup() {
		String email = "charlesb@yesmail.com";
		lValue = Hashing.sha256().hashString(email, StandardCharsets.UTF_8).toString();
		sValue = "2e4bbe0d-00a3-4f30-b703-20a9a4ae6bb1";
		getCustomResourceService = new StubbedGetCustomResourceByIdService();
		viewNameService = new DefaultViewNameService();
	}

	@Test
	@DisplayName("Coupon Found")
	void couponFound() {
		barcode = "49300224010";
		url = formatUrl(lValue, sValue, barcode);
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		appDetails = customPagesRequest.getAppDetails();

		getCustomResourceRequest = ControllerUtils.getCustomResourceRequest(customPagesRequest);
		assertAll("Get Custom Resource Request assertions", () -> {
			validateMap(getCustomResourceRequest.getParameters(),
				Map.of(
					"customerIdHash", lValue,
					"customerUuid", sValue,
					"barcode", barcode
				));
		});
		try {
			customResourceResponseWrapper = getCustomResourceService.getCustomResource(appDetails, getCustomResourceRequest);
		} catch (ResourceNotFoundException e) {
			fail("Should have found the custom resource record!");
		}
		CustomResourceRecord retrieved = customResourceResponseWrapper.getCustomResourceResponse().getRecord();
		final String acsId = (String)retrieved.getValue(ACS_ID).orElse("");
		final String expireDate = (String) retrieved.getValue("expireDate").orElse("");
		final String image = (String)retrieved.getValue("image").orElse("");
		final String instructions = (String) retrieved.getValue("instructions").orElse("");
		final String legal = (String) retrieved.getValue("legal").orElse("");
		final String onlineCode = (String) retrieved.getValue("onlineCode").orElse("");

		updateRequest = ControllerUtils.updateCustomResourceRequest(customPagesRequest, null,
			customResourceResponseWrapper.getCustomResourceResponse());

		assertAll("Check actions", () -> {
			assertTrue(updateRequest.hasActions(), "Update request has actions");
			assertEquals("cusCouponLog", updateRequest.getResourceName(), "Custom Resource name");
			assertEquals(1, updateRequest.getRecords().size(), "Request is for 1 record");
			CustomResourceRecord record = updateRequest.getRecords().get(0);
			validateMap(
				Map.of(
					ACS_ID, acsId,
					"accessedCount", 1
				), record.getValues(),List.of("lastModified")
			);
		});

		CustomResourceService customResourceService = new StubbedCustomResourceService();
		CustomResourceResponseWrapper<GetCustomResourceResponse> updateResponse = customResourceService.update(
			customPagesRequest.getAppDetails(), updateRequest);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);

		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean, updateResponse, null);

		assertAll("Display form assertions", () -> {
			validateMap(Map.of(
			"accessedCount", 1, // this will be a string in real Microservices API requests
			"barcode", barcode,
			"expireDate", expireDate,
			"image", image,
			"instructions", instructions,
			"legal", legal,
			"limit", "5",
			"onlineCode", onlineCode
			), dynamicFormBean.getItems());

			RequestResult requestResult = dynamicFormBean.getRequestResult();
			assertSame(requestResult.getRequestType(), RequestType.CUSTOM_RESOURCE, "Check RequestResult type");
			assertTrue(requestResult.isUpdate(), "Result is an update");
			assertTrue(requestResult.isCustomResourcesIncluded(), "CustomResource included");
			assertTrue(requestResult.isCustomResourcesSuccess(), "Custom Resource success");
		});

		viewName = viewNameService.deriveSuccessView(customPagesRequest, dynamicFormBean,
			customResourceResponseWrapper.getCustomResourceResponse());
		assertEquals("/companies/boscovs/coupon/index", viewName, "Check view name");
	}

	@Test
	@DisplayName("No Coupon Found")
	void couponNotFound() {
		barcode = "12345678901";
		url = formatUrl(lValue, sValue, barcode);
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		appDetails = customPagesRequest.getAppDetails();

		getCustomResourceRequest = ControllerUtils.getCustomResourceRequest(customPagesRequest);
		assertAll("Get Custom Resource Request assertions", () -> {
			validateMap(getCustomResourceRequest.getParameters(),
				Map.of(
					"customerIdHash", lValue,
					"customerUuid", sValue,
					"barcode", barcode
				));
		});
		try {
			customResourceResponseWrapper = getCustomResourceService.getCustomResource(appDetails, getCustomResourceRequest);
			fail("Should not have found the custom resource record!");
		} catch (ResourceNotFoundException e) {
			customResourceResponseWrapper = new CustomResourceResponseWrapper<>(getCustomResourceRequest, true, false);
			updateRequest = ControllerUtils.updateCustomResourceRequest(customPagesRequest, null, null);
		}

		assertFalse(updateRequest.hasActions(), "Update request has no actions");

		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ControllerUtils.initialiseForm(customPagesRequest, dynamicFormBean, customResourceResponseWrapper, null);

		assertAll("Display form assertions", () -> {
			validateMap(Map.of(
				"accessedCount", 0,
				"barcode", "12345678901",
				"expireDate", "",
				"image", "",
				"instructions", "",
				"legal", "",
				"limit", 0,
				"onlineCode", ""
			), dynamicFormBean.getItems());

			RequestResult requestResult = dynamicFormBean.getRequestResult();
			assertSame(requestResult.getRequestType(), RequestType.CUSTOM_RESOURCE, "Check RequestResult type");
			assertTrue(requestResult.isLookup(), "CustomResource lookup");
			assertFalse(requestResult.isFound(), "Custom Resource lookup failure");
		});

		viewName = viewNameService.deriveFailureView(customPagesRequest, dynamicFormBean,
			customResourceResponseWrapper.getCustomResourceResponse());
		assertEquals("/companies/boscovs/coupon/index", viewName, "Check view name");

	}

	private String formatUrl(String lValue, String sValue, String barcode) {
		return String.format("http://test.boscovs.com:8080/acscustompages/p/customResource/secure/coupon/index?l=%s&s=%s&barcode=%s",
			lValue, sValue, barcode);
	}


}
