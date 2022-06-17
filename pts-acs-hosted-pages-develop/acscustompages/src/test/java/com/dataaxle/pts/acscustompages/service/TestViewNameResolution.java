package com.dataaxle.pts.acscustompages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import com.dataaxle.pts.acscustompages.service.impl.DefaultViewNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestViewNameResolution {

	// TODO: refactor tests that use deprecated methods

	private String method;

	private String url;

	private ViewNameService viewNameService;

	private String viewName;

	private CustomPagesRequest customPagesRequest;

	private Map<String, Object> headers;

	@BeforeEach
	void setup() {
		viewNameService = new DefaultViewNameService();
		headers = new HashMap<>();
	}

	@Test
	void get_entrypoint() {
		method = "GET";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveEntryPage(customPagesRequest);
		assertEquals("/companies/usbank/preferences/signup", viewName);
	}

	@Test
	void get_this_page() {
		method = "GET";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveSuccessView(customPagesRequest);
		assertEquals("/p/services/update/preferences/preferences", viewName);
	}

	@Test
	void get_error() {
		method = "GET";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveErrorView(customPagesRequest);
		assertEquals("redirect:/p/page/error/preferences/error", viewName);
	}

	@Test
	public void post_with_redirect_to_failure() {
		method = "POST";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveFailureView(customPagesRequest, true);
		assertEquals("redirect:/p/profile/create/preferences/signup-new", viewName);
	}

	@Test
	public void post_with_redirect_to_success() {
		method = "POST";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveSuccessView(customPagesRequest, true);
		assertEquals("redirect:/p/services/update/preferences/preferences", viewName);
	}

	@Test
	public void post_with_redirect_to_error() {
		method = "POST";
		url = "/p/profile/search/preferences/signup";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveErrorView(customPagesRequest);
		assertEquals("redirect:/p/page/error/preferences/error", viewName);
	}

	@Test
	public void post_error() {
		method = "POST";
		url = "/p/profile/search/preferences/signup-new";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveErrorView(customPagesRequest);
		assertEquals("redirect:/p/page/error/preferences/error", viewName);
	}

	@Test
	void post_error_stay_on_page() {
		method = "POST";
		url = "/p/profile/search/marketing/signup/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.ymnewsolutions.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveErrorView(customPagesRequest);
		assertEquals("/companies/ymnewsolutions/marketing/signup/index", viewName);
	}

	@Test
	public void usbank_missing_input() {
		method = "GET";
		url = "/p/services/update/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveMissingInputView(customPagesRequest);
		assertEquals("redirect:/p/profile/search/preferences/signup", viewName);
	}

	@Test
	public void missing_input_no_redirect() {
		method = "GET";
		url = "/p/profile/update/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveMissingInputView(customPagesRequest);
		assertEquals("/companies/resideo/preferences/preferences", viewName);
	}

	@Test
	public void post_no_app_error_page() {
		method = "POST";
		url = "/p/profile/search/emailCheck/index";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.resideo.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveErrorView(customPagesRequest);
		assertEquals("/error", viewName);
	}

	@ParameterizedTest
	@MethodSource("usbank_preferences_navigation_data")
	@Disabled("Needs refactoring to use a non deprecated method")
	void usbank_preferences_navigation(List<ProcessingAction> actions, String expectedView) {
		method = "POST";
		url = "/p/services/update/preferences/preferences";
		headers.put(CustomPagesConstants.FORWARDED_HDR, "test.mybmwcreditcard.com");
		customPagesRequest = TestUtils.getCustomPagesRequest(method, url, headers);
		viewName = viewNameService.deriveSuccessView(customPagesRequest, actions, true);
		assertEquals(expectedView, viewName);
	}

	private static Stream<Arguments> usbank_preferences_navigation_data() {
		return Stream.of(
			arguments(Collections.singletonList(ProcessingAction.SERVICE_ADD),
				"redirect:/p/profile/confirm/preferences/confirm"),
			arguments(Collections.singletonList(ProcessingAction.SERVICE_REMOVE),
				"redirect:/p/profile/update/preferences/unsub-survey")
		);
	}

	@Test
	@DisplayName("Resideo Lookup page with Forward Query Parameters")
	void getWithFwdQueryParams() {
		String l = "b1c8b9cb9eccec7ea8f9e03293b3523b6acdc5b530af1d886db24c1790185027";
		String url = String.format("http://test.resideo.com:8081/acscustompages/p/profile/secure/preferences/lookup?l=%s&s=af66a63d-ff29-41cc-8404-2c53f262861f&view=services&deliveryId=DM456", l);
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.GET, url);
		ProfileResponse profileResponse = StubbedProfileRepository.getByCustomerIdHash(
			customPagesRequest.getCompany(), l);
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/services/update/preferences/preferences?deliveryId=DM456", viewName);
	}

	@Test
	@DisplayName("YmNewSolutions Submit Profile Create")
	void postWithForwardQueryParameters() {
		String url = "http://test.ymnewsolutions.com:8081/acscustompages/p/profile/create/marketing/signup/index?p1=foo&p1=bar&p2=foobar";
		customPagesRequest = TestUtils.getCustomPagesRequest(HttpMethod.POST, url);
		DynamicFormBean dynamicFormBean = new DynamicFormBean(customPagesRequest);
		ProfileResponse profileResponse = StubbedProfileRepository.search("ymnewsolutions", Map.of("email", "charles.berger@yesmarketing.com"));
		viewName = viewNameService.deriveSuccessView(customPagesRequest, profileResponse, dynamicFormBean);
		assertEquals("redirect:/p/profile/confirm/marketing/signup/thankyou?p1=bar&p1=foo&p2=foobar", viewName);
	}
}
