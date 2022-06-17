package com.yesmarketing.ptsacs.common.util.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.yesmarketing.ptsacs.common.exception.ResourceNotFoundException;
import com.yesmarketing.ptsacs.services.util.FormConfigTestHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

public class TestMetricData {

	@ParameterizedTest
	@MethodSource("testCases")
	void parseUri(String method, String uri, String endpoint, int qpSize) {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.request(HttpMethod.valueOf(method), uri);
		MockHttpServletRequest request = builder.buildRequest(new MockServletContext());
		ElasticsearchAnalysisFilter.MetricData data = new ElasticsearchAnalysisFilter.MetricData(request,
			FormConfigTestHelper.getByCompanyAndCode("ymnewsolutions", "VALIDTESTFORM")
				.orElseThrow(() -> new ResourceNotFoundException("formConfig", "company", "ymnewsolutions")));
		assertEquals(endpoint, data.getEndpoint(), "Endpoint");
		assertEquals(qpSize, data.getQueryParameters().size(), "QP Size");
	}

	public static Stream<Arguments> testCases() {
		return Stream.of(
			arguments("POST", "https://amsapi.data-axle.com/v1/services/composite/c150fe1ded8078c20849b73d60e1099b844a0a710b4a2ac44439aaeecc9c3fa1",
				"/v1/services/composite/{customerIdHash}", 1),
			arguments("PUT", "https://amspai.data-axle.com/v1/services/profiles/c150fe1ded8078c20849b73d60e1099b844a0a710b4a2ac44439aaeecc9c3fa1/services",
				"/v1/services/profiles/{customerIdHash}/services", 1),
			arguments("GET", "https://amspai.data-axle.com/v1/services/profiles?l=df4b3bd917aeae458092b9a8cf0b06be70f240c16267e810ec9b4afc070716f5&s=c74724ec-0164-4e0f-9eed-018ab06eb708&view=services",
				"/v1/services/profiles", 3),
			arguments("GET", "https://amspai.data-axle.com/v1/services/customResources/cusProducts/records/search/byParentcategoryfilter?parentCategory=Thermostat%20and%20Sensors&_lineStart=0&_lineCount=300",
				"/v1/services/customResources/cusProducts/records/search/byParentcategoryfilter", 3),
			arguments("POST", "https://amspai.data-axle.com/v1/services/composite", "/v1/services/composite", 0),
			arguments("POST", "https://amspai.data-axle.com/v1/services/profiles/customer?view=services", "/v1/services/profiles/customer", 1),
			arguments("GET", "https://amsapi.data-axle.com/v1/services/email/EVT_1723278/status/@VWFiqbERkSr-2qDl0iyV139og_lJZj3Mf9j20FSRU7sMw2GyR5dXLPJ5YxgCwLIMDCTOf8iurThQv_zVDx299qSxW70",
				"/v1/services/email/{eventId}/status/{PKey}", 2)
		);
	}
}
