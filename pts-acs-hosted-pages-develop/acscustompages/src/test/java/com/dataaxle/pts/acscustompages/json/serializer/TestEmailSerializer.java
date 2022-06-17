package com.dataaxle.pts.acscustompages.json.serializer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailData;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class TestEmailSerializer extends BaseSerializerTest {

	private String email, eventId;

	private TriggerEmailData triggerEmailData;

	@BeforeEach
	void setup() {
		super.initialize();
		triggerEmailData = new TriggerEmailData();
	}

	@Test
	@DisplayName("Resideo Create Profile")
	void resideo_create_profile() throws JsonProcessingException {
		email = "charlesb@yesmail.com";
		eventId = "EVTsign_uppageconfirmation_bmw";
		triggerEmailData.setEmail(email);
		triggerEmailData.setEventId(eventId);
		triggerEmailData.setExpiration(LocalDateTime.now().plusDays(1L));
		triggerEmailData.setScheduled(LocalDateTime.now());
		triggerEmailData.setContext(Map.of(
			"customerIdHash", "b1c8b9cb9eccec7ea8f9e03293b3523b6acdc5b530af1d886db24c1790185027",
			"Source", "buoySignup"
		));
		jsonStr = objectMapper.writeValueAsString(triggerEmailData);
		LOG.debug("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals(email, getJsonPath(document, "$.email"), "Email");
			assertEquals(eventId, getJsonPath(document, "$.eventId"), "Event Id");
			assertTrue(((String)getJsonPath(document, "$.expiration")).matches(ACS_DATE_REGEX), "Expiration");
			assertTrue(((String)getJsonPath(document, "$.scheduled")).matches(ACS_DATE_REGEX), "Scheduled");
			assertNotNull(getJsonPath(document, "$.ctx.customerIdHash"), "Context Customer ID Hash");
			assertNotNull(getJsonPath(document, "$.ctx.Source"));
		});
	}

	@Test
	@DisplayName("US Bank Preferences New Signup")
	void usbank_preferences_new_signup() throws JsonProcessingException {
		email = "charlesb@yesmail.com";
		eventId = "EVTsign_uppageconfirmation_bmw";
		triggerEmailData.setEmail(email);
		triggerEmailData.setEventId(eventId);
		triggerEmailData.setContext(Map.of(
			"customerIDHash", "",
			"acctL4", "1234",
			"firstName", "Charles",
			"lastName", "Berger"
		));
		jsonStr = objectMapper.writeValueAsString(triggerEmailData);
		LOG.debug("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals(email, getJsonPath(document, "$.email"), "Email");
			assertEquals(eventId, getJsonPath(document, "$.eventId"), "Event Id");
			assertFalse(pathExists(document, "$.expiration"), "Expiration");
			assertFalse(pathExists(document, "$.scheduled"), "Scheduled");
			assertNotNull(getJsonPath(document, "$.ctx.customerIDHash"), "Context Customer ID Hash");
			assertNotNull(getJsonPath(document, "$.ctx.acctL4"));
			assertNotNull(getJsonPath(document, "$.ctx.firstName"));
			assertNotNull(getJsonPath(document, "$.ctx.lastName"));
		});
	}
}
