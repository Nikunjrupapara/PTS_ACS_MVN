package com.dataaxle.pts.acscustompages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestCustomPagesRequest {

	private String method;

	private String domain;

	private String uri;

	private Map<String, Object> headers;

	private CustomPagesRequest customPagesRequest;

	private DynamicFormBean dynamicFormBean;

	private String expected;

	@BeforeEach
	void setup() {
		headers = new HashMap<>();
	}

	@Test
	void get_no_query_params() {
		method = "GET";
		uri = "/p/profile/search/emailCheck/index";
		domain = "test.resideo.com";
		headers.put(CustomPagesConstants.FORWARDED_HDR, domain);
		customPagesRequest = TestUtils.getCustomPagesRequest(method, uri, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		expected = String.format("https://%s%s", domain, uri);
		assertEquals(expected, customPagesRequest.getRequestUrl());
		assertEquals("emailCheck", dynamicFormBean.getAppPath());
	}

	@Test
	void get_with_query_params() {
		method = "GET";
		uri = "/p/profile/secure/preferences/lookup?l=abc&s=def&view=services";
		domain = "test.resideo.com";
		headers.put(CustomPagesConstants.FORWARDED_HDR, domain);
		customPagesRequest = TestUtils.getCustomPagesRequest(method, uri, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		expected = String.format("https://%s%s", domain, uri);
		assertEquals(expected, customPagesRequest.getRequestUrl());
		assertEquals("preferences", dynamicFormBean.getAppPath());
	}

	@Test
	void get_with_empty_query_param() {
		method = "GET";
		uri = "/p/profile/secure/preferences/lookup?l=abc&s=&view=services";
		domain = "test.resideo.com";
		headers.put(CustomPagesConstants.FORWARDED_HDR, domain);
		customPagesRequest = TestUtils.getCustomPagesRequest(method, uri, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		expected = String.format("https://%s%s", domain, uri);
		assertEquals(expected, customPagesRequest.getRequestUrl());
		assertEquals("preferences", dynamicFormBean.getAppPath());
	}

	@Test
	void get_with_missing_query_param_value() {
		method = "GET";
		uri = "/p/profile/secure/preferences/lookup?l=abc&s&view=services";
		domain = "test.resideo.com";
		headers.put(CustomPagesConstants.FORWARDED_HDR, domain);
		customPagesRequest = TestUtils.getCustomPagesRequest(method, uri, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		expected = String.format("https://%s%s", domain, uri);
		assertEquals(expected, customPagesRequest.getRequestUrl());
		assertEquals("preferences", dynamicFormBean.getAppPath());
	}

	@Test
	void get_with_multiple_values_for_param() {
		method = "GET";
		uri = "/p/profile/secure/preferences/lookup?l=abc&s=1&s=2&view=services";
		domain = "test.resideo.com";
		headers.put(CustomPagesConstants.FORWARDED_HDR, domain);
		customPagesRequest = TestUtils.getCustomPagesRequest(method, uri, headers);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		expected = String.format("https://%s%s", domain, uri);
		assertEquals(expected, customPagesRequest.getRequestUrl());
		assertEquals("preferences", dynamicFormBean.getAppPath());
	}

	@ParameterizedTest
	@MethodSource("devData")
	void get_environment_dev(HttpMethod method, String url) {
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		assertEquals("development", dynamicFormBean.getEnvironmentName());
	}

	private static Stream<Arguments> devData() {
		return Stream.of(
			arguments(HttpMethod.GET, "http://test.resideo.com/p/profile/search/emailCheck/index"),
			arguments(HttpMethod.POST, "http://resideo.testlocal.com/p/profile/search/emailCheck/index"),
			arguments(HttpMethod.GET, "http://test.mybmwcreditcard.com/p/profile/search/preferences/signup")
		);
	}

	@ParameterizedTest
	@MethodSource("qaData")
	void get_environment_qa(HttpMethod method, String url) {
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		assertEquals("qa", dynamicFormBean.getEnvironmentName());
	}

	private static Stream<Arguments> qaData() {
		return Stream.of(
			arguments(HttpMethod.GET, "https://resideo-qa.ptsacs.data-axle.com/p/profile/search/emailCheck/index"),
			arguments(HttpMethod.GET, "http://usb-bmw.ptsacs.data-axle.com/p/profile/search/preferences/signup")
		);
	}

	@ParameterizedTest
	@MethodSource("prodData")
	void get_environment_prod(HttpMethod method, String url) {
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url);
		dynamicFormBean = new DynamicFormBean(customPagesRequest);
		assertEquals("production", dynamicFormBean.getEnvironmentName());
	}

	private static Stream<Arguments> prodData() {
		return Stream.of(
			arguments(HttpMethod.GET, "https://email.resideo.com/p/profile/search/emailCheck/index"),
			arguments(HttpMethod.GET, "http://email.mybmwcreditcard.com/p/profile/search/preferences/signup")
		);
	}
}
