package com.dataaxle.pts.acscustompages.json.serializer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.ServicesRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class TestServicesSerializer extends BaseSerializerTest {

	private ServicesRequest servicesRequest;

	@BeforeEach
	void setup() {
		super.initialize();
		servicesRequest = new ServicesRequest();
	}

	@Test
	@DisplayName("US Bank Preferences Create Profile")
	void usbank_preferences_create_profile() throws JsonProcessingException {
		servicesRequest.addServiceAction(Map.of("marketing", ServiceAction.ADD, "transactional", ServiceAction.REMOVE));

		jsonStr = objectMapper.writeValueAsString(servicesRequest);
		Object document = getDocument(jsonStr);

		assertAll("Services", () -> {
			assertEquals(1, getJsonPath(document, "$.add.length()"));
			assertEquals("marketing", getJsonPath(document, "$.add[0]"));
			assertEquals(1, getJsonPath(document, "$.remove.length()"));
			assertEquals("transactional", getJsonPath(document, "$.remove[0]"));
		});
	}
}
