package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.service.GetProfileBySecureLinkService;
import com.dataaxle.pts.acscustompages.service.GetProfileCustomerIdService;
import com.dataaxle.pts.acscustompages.service.ViewNameService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultViewNameService;
import com.dataaxle.pts.acscustompages.stubs.StubbedGetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.stubs.StubbedGetProfileByCustomerIdService;
import com.dataaxle.pts.acscustompages.stubs.StubbedGetProfileBySecureLinkService;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public abstract class TestActionProcessing {

	protected String url;

	protected Map<String, Object> headers;

	protected CustomPagesRequest customPagesRequest;

	protected AppDetails appDetails;

	protected String customerIdHash;

	protected String email;

	protected GetProfileRequest getProfileRequest;

	protected DynamicFormBean dynamicFormBean;

	protected ProfileRequest profileRequest;

	protected GetProfileBySecureLinkService getProfileBySecureLinkService;

	protected ViewNameService viewNameService;

	protected ProfileResponseWrapper profileResponseWrapper;

	protected String viewName;

	protected CustomResourceResponseWrapper<GetCustomResourceResponse> customResourceResponseWrapper;

	@BeforeEach
	void setup() {
		headers = new HashMap<>();
		getProfileBySecureLinkService = new StubbedGetProfileBySecureLinkService();
		viewNameService = new DefaultViewNameService();
		getProfileRequest = new GetProfileRequest();
	}

	protected void validateMap(Map<String, ?> expected, Map<String, ?> actual) {
		validateMap(expected, actual, Collections.emptyList());
	}

	protected void validateMap(Map<String, ?> expected, Map<String, ?> actual, List<String> equalityExclusions) {
		/*
		 * equalityExclusions is a list of fields that should appear in the map but have a dynamically generated value
		 * that is difficult to predict, e.g. the current date.  Instead of testing that the actual maps contains the
		 * specific value in the expected map, we just test that the key exists in the actual map.
		 */
		expected.forEach((key, value) -> assertEquals(value, actual.get(key), key));
		actual.keySet().stream()
			.filter(key -> !actual.containsKey(key))
			.forEach(key -> assertTrue(expected.containsKey(key), String.format("expected contains %s", key)));
		equalityExclusions.forEach(name -> assertTrue(actual.containsKey(name), String.format("actual contains %s", name)));
	}

	protected void validateNotInMap(Set<String> notExpected, Map<String, ?> actual) {
		notExpected.forEach((key) -> assertFalse(actual.containsKey(key), String.format("value of %s is still in profile", key)));
	}

	protected void validateCustomResourceRequests(Map<String, List<CustomResourceRequest>> expected,
												Map<String, List<CustomResourceRequest>> actual) {
		validateCustomResourceRequests(expected, actual, Collections.emptyList());
	}

	protected void validateCustomResourceRequests(Map<String, List<CustomResourceRequest>> expected,
												  Map<String, List<CustomResourceRequest>> actual,
												  List<String> equalityExclusions) {
		expected.forEach((expResName, expRecs) -> {
			assertTrue(actual.containsKey(expResName), String.format("Found customResource.name %s", expResName));
			validateCustomResourceRecords(expResName, expRecs, actual.get(expResName), equalityExclusions);
		});
	}

	protected void validateCustomResourceRecords(String name, List<CustomResourceRequest> expected,
												 List<CustomResourceRequest> actual,
												 List<String> equalityExclusions) {
		IntStream.range(0, expected.size()).forEach(index -> {
			CustomResourceRequest exp = expected.get(index);
			CustomResourceRequest act = actual.get(index);
			assertEquals(exp.getResourceName(), act.getResourceName(), String.format("%s row %d", name, index));
			IntStream.range(0, exp.getRecords().size()).forEach(ind2 -> {
				CustomResourceRecord expRec = exp.getRecords().get(ind2);
				CustomResourceRecord actRec = act.getRecords().get(ind2);
				validateMap(expRec.getValues(), actRec.getValues(), equalityExclusions);
			});
		});
	}

	protected ProfileResponseWrapper lookupProfile(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean) {
		List<String> fieldsToSubmit = customPagesRequest.getFieldsToSubmit();
		GetProfileRequest getProfileRequest = new GetProfileRequest();
		formInputBean.getItems().entrySet().stream()
			.filter(entry -> fieldsToSubmit.contains(entry.getKey()))
			.forEach(entry -> {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				getProfileRequest.addParameter(key, value);
			});
		GetProfileCustomerIdService getProfileCustomerIdService = new StubbedGetProfileByCustomerIdService();
		return getProfileCustomerIdService.getProfile(customPagesRequest.getAppDetails(), getProfileRequest);
	}

	protected CustomResourceResponseWrapper<GetCustomResourceResponse> lookupCustomResource(CustomPagesRequest customPagesRequest, DynamicFormBean formInputBean) {
		List<String> fieldsToSubmit = customPagesRequest.getFieldsToSubmit();
		GetCustomResourceRequest request = new GetCustomResourceRequest(customPagesRequest.getCurrentPage().getCustomResource());
		formInputBean.getItems().entrySet().stream()
			.filter(entry -> fieldsToSubmit.contains(entry.getKey()))
			.forEach(entry -> request.addParameter(entry.getKey(), (String)entry.getValue()));
		GetCustomResourceByIdService service = new StubbedGetCustomResourceByIdService();
		return service.getCustomResource(customPagesRequest.getAppDetails(), request);
	}
}
