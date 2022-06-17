package com.dataaxle.pts.acscustompages.utils;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.FORWARDED_FOR_HDR;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.FORWARDED_HDR;
import static org.mockito.ArgumentMatchers.any;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.configuration.ConfigurationParameters;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.repository.AppDetailsRepository;
import com.dataaxle.pts.acscustompages.service.AppDetailsService;
import com.dataaxle.pts.acscustompages.service.CustomPagesRequestService;
import com.dataaxle.pts.acscustompages.service.DomainService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultAppDetailsService;
import com.dataaxle.pts.acscustompages.service.impl.DefaultCustomPagesRequestService;
import com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository;
import com.dataaxle.pts.acscustompages.stubs.StubbedDomainService;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestUtils {

	public static final ConfigurationParameters TEST_CONFIG_PARAMS = new ConfigurationParameters(
		"http://localhost:8080/v1/services", "acscustompages", "http://localhost:8083/barode-google"
	);

	private static final CustomPagesRequestService customPagesRequestService;

	static {
		DomainService domainService = new StubbedDomainService();
		AppDetailsRepository appDetailsRepository = Mockito.mock(AppDetailsRepository .class);
		Mockito
			.when(appDetailsRepository.findById(any(AppDetails.AppDetailsId.class)))
			.thenAnswer(invocationOnMock -> {
				AppDetails.AppDetailsId id = invocationOnMock.getArgument(0, AppDetails.AppDetailsId.class);
				return StubbedAppDetailsRepository.findById(id);
			});
		AppDetailsService appDetailsService = new DefaultAppDetailsService(appDetailsRepository);
		customPagesRequestService = new DefaultCustomPagesRequestService(domainService, appDetailsService, TEST_CONFIG_PARAMS);
	}

	public static CustomPagesRequest getCustomPagesRequest(String method, String url, Map<String, Object> headers) {
		MockHttpServletRequest request = getMockHttpServletRequest(method, url, headers);
		return customPagesRequestService.parseRequest(request);
	}

	public static MockHttpServletRequest getMockHttpServletRequest(String method, String url, Map<String, Object> headers) {
		MockHttpServletRequest req = new MockHttpServletRequest(method, url);
		headers.forEach(req::addHeader);
		int domainEnds = url.indexOf("/", url.contains("https") ? 8 : 7);
		String contextPath;
		String queryString = "";
		if (url.contains("?")) {
			contextPath = url.substring(domainEnds, url.indexOf("?"));
			queryString = url.substring(url.indexOf("?") + 1);
		} else {
			contextPath = url.substring(domainEnds);
		}
		req.setContextPath(contextPath);
		req.setServletPath(contextPath);
		req.setQueryString(queryString);
		return req;
	}

	public static CustomPagesRequest getCustomPagesRequest(HttpMethod httpMethod, String url) {
		return customPagesRequestService.parseRequest(getMockHttpServletRequest(httpMethod, url));
	}

	public static MockHttpServletRequest getMockHttpServletRequest(HttpMethod method, String url) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(method, url);
		return builder.buildRequest(new MockServletContext());
	}



	public static MockHttpServletRequest getMockHttpServletRequestForIPTest(IpTestType type) {
		Map<String, Object> headers = new LinkedMap();

		String method = "GET";
		String url = "/p/profile/create/marketing/preferences/index";
		MockHttpServletRequest req = new MockHttpServletRequest(method, url);
		headers.put(FORWARDED_HDR, "test.ymnewsolutions.com");
		switch(type){
			case SINGLE_HEADER:
				headers.put(FORWARDED_FOR_HDR,"199.125.14.2");
				break;
			case MULTIPLE_HEADER:
				headers.put(FORWARDED_FOR_HDR,"199.125.14.2, 192.168.25.251");
				break;
			case SINGLE_REMOTE_ADDR:
				req.setRemoteAddr("199.125.14.2");
				break;
			case MULTIPLE_REMOTE_ADDR:
				req.setRemoteAddr("199.125.14.2, 192.168.25.251");
				break;
		}
		headers.forEach(req::addHeader);

		return req;
	}

	private TestUtils() {

	}


	public enum IpTestType {
		SINGLE_HEADER,
		MULTIPLE_HEADER,
		SINGLE_REMOTE_ADDR,
		MULTIPLE_REMOTE_ADDR
	}
}
