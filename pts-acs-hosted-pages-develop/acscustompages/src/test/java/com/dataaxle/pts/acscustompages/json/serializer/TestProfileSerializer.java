package com.dataaxle.pts.acscustompages.json.serializer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dataaxle.pts.acscustompages.model.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class TestProfileSerializer extends BaseSerializerTest {

	private Profile profile;

	@BeforeEach
	void setup() {
		super.initialize();
		profile = new Profile();
	}

	@Test
	@DisplayName("Resideo create profile")
	void resideo_buoy_create_profile() throws JsonProcessingException {
		profile.setFields(Map.of(
			"email", "charlesb@yesmail.com",
			"firstName", "Charles",
			"lastName", "Berger"
		));
		jsonStr = objectMapper.writeValueAsString(profile);
		LOG.info("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals("charlesb@yesmail.com", getJsonPath(document, "$.email"), "Email");
			assertEquals("Charles", getJsonPath(document, "$.firstName"), "First Name");
			assertEquals("Berger", getJsonPath(document, "$.lastName"), "Last Name");
		});
	}

	@Test
	@DisplayName("Resideo Buoy update subscribed profile")
	void resideo_buoy_update_subscribed_profile() throws JsonProcessingException {
		profile.setFields(Map.of("cusBuoyPage", "Y"));
		jsonStr = objectMapper.writeValueAsString(profile);
		LOG.info("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals("Y", getJsonPath(document, "$.customFields.cusBuoyPage"), "Buoy signup");
		});
	}

	@Test
	@DisplayName("US Bank Preferences Signup")
	void usbank_preferences_signup() throws JsonProcessingException {
		profile.setFields(Map.of(
			"cusAcctL4", "1234",
			"cusPartner", "BMW",
			"email", "charlesb@yesmail.com",
			"firstName", "Charles",
			"lastName", "Berger"
		));
		jsonStr = objectMapper.writeValueAsString(profile);
		LOG.info("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals("1234", getJsonPath(document, "$.customFields.cusAcctL4"), "cusAcctL4");
			assertEquals("BMW", getJsonPath(document, "$.customFields.cusPartner"), "cusPartner");
			assertEquals("charlesb@yesmail.com", getJsonPath(document, "$.email"), "Email");
			assertEquals("Charles", getJsonPath(document, "$.firstName"), "First Name");
			assertEquals("Berger", getJsonPath(document, "$.lastName"), "Last Name");
		});
	}

	@Test
	@DisplayName("Address data to Location object")
	void address_data_to_location() throws JsonProcessingException {
		profile.setFields(Map.of(
			"email", "charlesb@yesmail.com",
			"location.address1", "Address Line 1",
			"location.address2", "Address Line 2",
			"location.address3", "Address Line 3",
			"location.address4", "Address Line 4",
			"location.city", "City",
			"location.countryCode", "Country Code",
			"location.stateCode", "NY",
			"location.zipCode", "12345"
		));
		jsonStr = objectMapper.writeValueAsString(profile);
		LOG.info("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals("charlesb@yesmail.com", getJsonPath(document, "$.email"), "Email");
			assertEquals("Address Line 1", getJsonPath(document, "$.location.address1"), "location.address1");
			assertEquals("Address Line 2", getJsonPath(document, "$.location.address2"), "location.address2");
			assertEquals("Address Line 3", getJsonPath(document, "$.location.address3"), "location.address3");
			assertEquals("Address Line 4", getJsonPath(document, "$.location.address4"), "location.address4");
			assertEquals("City", getJsonPath(document, "$.location.city"), "location.city");
			assertEquals("Country Code", getJsonPath(document, "$.location.countryCode"), "location.countryCode");
			assertEquals("NY", getJsonPath(document, "$.location.stateCode"), "location.stateCode");
			assertEquals("12345", getJsonPath(document, "$.location.zipCode"), "location.zipCode");
		});
	}

	@Test
	@DisplayName("Resideo Product Registration New Profile")
	void resideo_product_registration_new_profile() throws JsonProcessingException {
		profile.setFields(Map.of(
			"email", "charlesb@yesmail.com",
			"firstName", "Charles",
			"lastName", "Berger",
			"location.address1", "A Street",
			"location.city", "A City",
			"location.countryCode", "US",
			"location.stateCode", "NY",
			"location.zipCode", "12345",
			"cusMobileNumber", "1112223333",
			"cusSmsFlag", true
		));
		jsonStr = objectMapper.writeValueAsString(profile);
		LOG.info("result: {}", jsonStr);
		Object document = getDocument(jsonStr);
		assertAll("Check JSON", () -> {
			assertEquals("charlesb@yesmail.com", getJsonPath(document, "$.email"), "Email");
			assertEquals("Charles", getJsonPath(document, "$.firstName"), "First Name");
			assertEquals("Berger", getJsonPath(document, "$.lastName"), "Last Name");
			assertEquals("A Street", getJsonPath(document, "$.location.address1"), "location.address1");
			assertEquals("A City", getJsonPath(document, "$.location.city"), "location.city");
			assertEquals("US", getJsonPath(document, "$.location.countryCode"), "location.countryCode");
			assertEquals("NY", getJsonPath(document, "$.location.stateCode"), "location.stateCode");
			assertEquals("12345", getJsonPath(document, "$.location.zipCode"), "location.zipCode");
			assertEquals("1112223333", getJsonPath(document, "$.customFields.cusMobileNumber"), "cusMobileNumber");
			assertEquals(true, getJsonPath(document, "$.customFields.cusSmsFlag"), "cusSmsFlag");
		});
	}
}
