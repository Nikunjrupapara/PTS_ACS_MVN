package com.dataaxle.pts.acscustompages.service;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.FORWARDED_HDR;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.form.QueryParameters;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.service.impl.DefaultAppDetailsService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultCustomPagesRequestService;
import com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository;
import com.dataaxle.pts.acscustompages.stubs.StubbedDomainService;
import com.dataaxle.pts.acscustompages.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestCustomPagesRequestService {

	private CustomPagesRequestService customPagesRequestService;

	private CustomPagesRequest customPagesRequest;

	private AppDetails appDetails;

	@BeforeEach
	void setup() {
		DomainService domainService = new StubbedDomainService();
		AppDetailsRepository appDetailsRepository = Mockito.mock(AppDetailsRepository.class);
		Mockito
			.when(appDetailsRepository.findById(any(AppDetails.AppDetailsId.class)))
			.thenAnswer(invocationOnMock -> {
				AppDetails.AppDetailsId id = invocationOnMock.getArgument(0, AppDetails.AppDetailsId.class);
				return StubbedAppDetailsRepository.findById(id);
			});
		AppDetailsService appDetailsService = new DefaultAppDetailsService(appDetailsRepository);
		customPagesRequestService = new DefaultCustomPagesRequestService(domainService, appDetailsService, TestUtils.TEST_CONFIG_PARAMS);
	}

	@DisplayName("Miscellaneous Requests")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("requests_data")
	void requests(String name, HttpMethod method, String url, boolean expFound, String expCompany, String expDomain,
				  String expPath, String expBrand, Map<String, List<String>> expQueryParams) {
		MockHttpServletRequest request = TestUtils.getMockHttpServletRequest(method, url);
		boolean found = true;
		try {
			customPagesRequest = customPagesRequestService.parseRequest(request);
		} catch (Exception e) {
			found = false;
		}
		assertEquals(expFound, found, "AppDetails were found");
		if (found) {
			appDetails = customPagesRequest.getAppDetails();
			assertAll("Check results",
				() -> assertEquals(expCompany, customPagesRequest.getCompany(), "company"),
				() -> assertEquals(expPath, customPagesRequest.getContextPath(), "contextPath"),
				() -> assertEquals(expDomain, customPagesRequest.getDomainName(), "domain"),
				() -> assertNotNull(appDetails.getJwt(), "Has a JWT"),
				() -> assertFalse(appDetails.getPages().isEmpty(), "Pages is not empty"),
				() -> assertEquals(expBrand, customPagesRequest.getBrand()),
				() -> {
					if (expQueryParams.isEmpty()) {
						assertTrue(customPagesRequest.getQueryParams().isEmpty(), "Found no Query Parameters");
					} else {
						Map<String, List<String>> actualQp = customPagesRequest.getQueryParams().getParameters();
						expQueryParams.keySet().forEach(key -> {
							assertTrue(customPagesRequest.hasQueryParameter(key), String.format("Expected query parameter %s", key));
							assertEquals(expQueryParams.get(key), customPagesRequest.getQueryParameter(key), key);
						});
						actualQp.keySet().forEach(key -> assertTrue(expQueryParams.containsKey(key), String.format("Expected query parameter %s", key)));
					}
				}
			);
		}
	}

	private static Stream<Arguments> requests_data() {
		return Stream.of(
			arguments("YLM Mkting Prefs", HttpMethod.GET, "http://test.ymnewsolutions.com:8080/p/profile/create/marketing/preferences/index",
				true, "ymnewsolutions", "test.ymnewsolutions.com", "marketing/preferences", "ymnewsolutions", Collections.emptyMap()),
			arguments("YLM Retail Register", HttpMethod.GET, "http://test.ymnewsolutions.com:8080/acscustompages/p/profile/create/register/retail/index",
				true, "ymnewsolutions", "test.ymnewsolutions.com", "register/retail", "ymnewsolutions", Collections.emptyMap()),
			arguments("US Bank Prefs Thankyou", HttpMethod.GET, "http://email-usbank.com/p/profile/preferences/thankyou",
				false, "", "", "", "", Collections.emptyMap()),
			arguments("US Bank Prefs Signup (BMW)", HttpMethod.GET, "http://test.mybmwcreditcard.com:8080/p/profile/search/preferences/signup",
				true, "usbank", "test.mybmwcreditcard.com", "preferences", "BMW", Collections.emptyMap()),
			/*arguments("/acscustompages/p/profile/secure/preferences/index?l=abc&s=xzy", Collections.emptyMap(), true,
				"ymnewsolutions", "localhost", "preferences", "ymnewsolutions"),*/
			arguments("US Bank Prefs Secure (ELA)", HttpMethod.GET, "http://test.myaccountaccess.com/p/profile/secure/preferences/lookup?l=1ab2c3d4&s=9f8e7d&view=services",
				true, "usbank", "test.myaccountaccess.com", "preferences", "ELA",
				Map.of("l", List.of("1ab2c3d4"), "s", List.of("9f8e7d"), "view", List.of("services"))),
			/*arguments("/p/profile/create/preferences/fid/signup-new", Collections.singletonMap(FORWARDED_HDR,
				"email.fidelityrewards.com"), true, "usbank", "test.fidelityrewards.com", "preferences/fid", "FID"),*/
			arguments("Resideo Prefs Update (HH)", HttpMethod.GET, "http://test.honeywellhome.com:8080/p/profile/services/preferences/update",
				true, "resideo", "test.honeywellhome.com", "preferences", "honeywellhome", Collections.emptyMap()),
			arguments("Resideo Prefs Lookup (Resideo)", HttpMethod.GET, "http://test.resideo.com/p/profile/secure/preferences/lookup?l=1ab2c3d4&s=9f8e1a&deliveryId=DM456",
				true, "resideo", "test.resideo.com", "preferences", "resideo",
				Map.of("l", List.of("1ab2c3d4"), "s", List.of("9f8e1a"), "deliveryId", List.of("DM456")))
		);
	}

	private MockHttpServletRequest getMockHttpServletRequest(String method, String uri, Map<String, String> headers) {
		MockHttpServletRequest req = new MockHttpServletRequest(method, uri);
		req.setContextPath(uri);
		req.setServletPath(uri);
		if (!headers.isEmpty()) {
			headers.forEach(req::addHeader);
		}
		return req;
	}

	@Test
	void SingleIPInHeaderTest(){
		MockHttpServletRequest req = TestUtils.getMockHttpServletRequestForIPTest(TestUtils.IpTestType.SINGLE_HEADER);
		String ip = customPagesRequestService.parseRequest(req).getIpAddress();
		Assert.isTrue(ip.equals("199.125.14.2"),String.format("SingleIPInHeaderTest %s does not match %s",ip,"199.125.14.2"));
	}

	@Test
	void MultipleIPInHeaderTest(){
		MockHttpServletRequest req = TestUtils.getMockHttpServletRequestForIPTest(TestUtils.IpTestType.MULTIPLE_HEADER);
		String ip = customPagesRequestService.parseRequest(req).getIpAddress();
		Assert.isTrue(ip.equals("199.125.14.2"),String.format("SingleIPInHeaderTest %s does not match %s",ip,"199.125.14.2"));
	}

	@Test
	void SingleIPInRemoteAddrTest(){
		MockHttpServletRequest req = TestUtils.getMockHttpServletRequestForIPTest(TestUtils.IpTestType.SINGLE_REMOTE_ADDR);
		String ip = customPagesRequestService.parseRequest(req).getIpAddress();
		Assert.isTrue(ip.equals("199.125.14.2"),String.format("SingleIPInHeaderTest %s does not match %s",ip,"199.125.14.2"));
	}

	@Test
	void MultipleIPInRemoteAddrTest(){
		MockHttpServletRequest req = TestUtils.getMockHttpServletRequestForIPTest(TestUtils.IpTestType.MULTIPLE_REMOTE_ADDR);
		String ip = customPagesRequestService.parseRequest(req).getIpAddress();
		Assert.isTrue(ip.equals("199.125.14.2"),String.format("SingleIPInHeaderTest %s does not match %s",ip,"199.125.14.2"));
	}

}
