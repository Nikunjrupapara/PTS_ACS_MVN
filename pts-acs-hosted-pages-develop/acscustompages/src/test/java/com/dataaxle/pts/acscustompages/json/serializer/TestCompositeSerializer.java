package com.dataaxle.pts.acscustompages.json.serializer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
public class TestCompositeSerializer extends BaseSerializerTest {

	private ProfileRequest profileRequest;

	private String customerIdHash, email, eventId, firstName, lastName;

	private String customResource1Name;

	@BeforeEach
	void setup() {
		super.initialize();
	}

	@Test
	@DisplayName("Resideo Buoy Create Profile")
	void resideo_buoy_create_profile() throws JsonProcessingException {
		customerIdHash = "";
		email = "charlesb@yesmail.com";
		firstName = "Charles";
		lastName = "Berger";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addProfileField("email", email);
		profileRequest.addProfileField("firstName", firstName);
		profileRequest.addProfileField("lastName", lastName);
		eventId = "EVTcbBuoySignupConfirmation";
		profileRequest.setTriggeredEventId(eventId);
		profileRequest.setTriggeredEventRecipient(email);
		profileRequest.addPersonalizationField("customerIdHash", customerIdHash);
		profileRequest.addPersonalizationField("Source", "buoySignup");

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertEquals(email, getJsonPath(document, "$.profile.email"), "Email");
				assertEquals(firstName, getJsonPath(document, "$.profile.firstName"), "First Name");
				assertEquals(lastName, getJsonPath(document, "$.profile.lastName"), "Last Name");
			});
			assertAll("Services", () -> {
				assertFalse(pathExists(document, "$.services"));
			});
			assertAll("Custom Resources", () -> {
				assertFalse(pathExists(document, "$.customResources"));
			});
			assertAll("Triggered Email", () -> {
				assertEquals(email, getJsonPath(document, "$.emails[0].email"), "Email");
				assertEquals(eventId, getJsonPath(document, "$.emails[0].eventId"), "Event Id");
				assertEquals(customerIdHash, getJsonPath(document, "$.emails[0].ctx.customerIdHash"), "Context Customer ID Hash");
				assertEquals("buoySignup", getJsonPath(document, "$.emails[0].ctx.Source"));
			});
		});
	}

	@Test
	@DisplayName("Resideo Buoy Confirmation")
	void resideo_buoy_confirmation() throws JsonProcessingException {
		customerIdHash = "ABC";
		email = "charlesb@yesmail.com";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addProfileField("cusBuoyPage", "Y");
		profileRequest.addServiceAction(Map.of(
			"honeywellhomeemails", ServiceAction.ADD,
			"honeywellhomeasks", ServiceAction.ADD,
			"honeywellhomepresentedoffers", ServiceAction.ADD
		));
		customResource1Name = "cusHosted_pages_logs";
		String source = "buoyEmailSignup";
		CustomResourceRequest customResourceRequest = new CustomResourceRequest(customResource1Name);
		customResourceRequest.addRecord(new CustomResourceRecord(Map.of(
			"email", email,
			"customerId", email,
			"optInAsks", true,
			"optInEmails", true,
			"optInEnergy", false,
			"optInOffers", true,
			"optInSupport", false,
			"source", source,
			"unsubAll", false
		)));
		profileRequest.addCustomResourceRequest(customResourceRequest);

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertEquals("Y", getJsonPath(document, "$.profile.customFields.cusBuoyPage"), "Email");
			});
			assertAll("Services", () -> {
				assertEquals(3, getJsonPath(document, "$.services.add.length()"));
				assertEquals(0, getJsonPath(document, "$.services.remove.length()"));
			});
			assertAll("Custom Resources", () -> {
				assertEquals(1, getJsonPath(document, "$.customResources.length()"));
				assertEquals(customResource1Name, getJsonPath(document, "$.customResources[0].name"));
				assertEquals(1, getJsonPath(document, "$.customResources[0].records.length()"));
				assertEquals(email, getJsonPath(document, "$.customResources[0].records[0].email"));
				assertEquals(email, getJsonPath(document, "$.customResources[0].records[0].customerId"));
				assertTrue((Boolean) getJsonPath(document, "$.customResources[0].records[0].optInAsks"));
				assertTrue((Boolean) getJsonPath(document, "$.customResources[0].records[0].optInEmails"));
				assertFalse((Boolean) getJsonPath(document, "$.customResources[0].records[0].optInEnergy"));
				assertTrue((Boolean) getJsonPath(document, "$.customResources[0].records[0].optInOffers"));
				assertFalse((Boolean) getJsonPath(document, "$.customResources[0].records[0].optInSupport"));
				assertEquals(source, getJsonPath(document, "$.customResources[0].records[0].source"));
				assertFalse((Boolean) getJsonPath(document, "$.customResources[0].records[0].unsubAll"));
			});
			assertAll("Triggered Email", () -> {
				assertFalse(pathExists(document, "$.emails"), "Email");
			});
		});
	}

	@Test
	@DisplayName("US Bank Preferences Create Profile")
	void usbank_preferences_create_profile() throws JsonProcessingException {
		customerIdHash = "";
		email = "charlesb@yesmail.com";
		firstName = "Charles";
		lastName = "Berger";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addProfileField("email", email);
		profileRequest.addProfileField("firstName", firstName);
		profileRequest.addProfileField("lastName", lastName);
		profileRequest.addProfileField("cusAcctL4", "1234");
		profileRequest.addProfileField("cusPartner", "BMW");
		profileRequest.addServiceAction(Map.of(
			"marketing", ServiceAction.ADD,
			"service", ServiceAction.ADD
		));
		eventId = "EVTsign_uppageconfirmation_bmw";
		profileRequest.setTriggeredEventId(eventId);
		profileRequest.setTriggeredEventRecipient(email);
		profileRequest.addPersonalizationField("customerIdHash", customerIdHash);
		profileRequest.addPersonalizationField("acctL4", "1234");
		profileRequest.addPersonalizationField("firstName", firstName);
		profileRequest.addPersonalizationField("lastName", lastName);

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertEquals(email, getJsonPath(document, "$.profile.email"), "Email");
				assertEquals(firstName, getJsonPath(document, "$.profile.firstName"), "First Name");
				assertEquals(lastName, getJsonPath(document, "$.profile.lastName"), "Last Name");
				assertEquals("1234", getJsonPath(document, "$.profile.customFields.cusAcctL4"));
				assertEquals("BMW", getJsonPath(document, "$.profile.customFields.cusPartner"));
			});
			assertAll("Services", () -> {
				assertEquals(2, getJsonPath(document, "$.services.add.length()"));
				assertEquals(0, getJsonPath(document, "$.services.remove.length()"));
			});
			assertAll("Custom Resources", () -> {
				assertFalse(pathExists(document, "$.customResources"));
			});
			assertAll("Triggered Email", () -> {
				assertEquals(email, getJsonPath(document, "$.emails[0].email"), "Email");
				assertEquals(eventId, getJsonPath(document, "$.emails[0].eventId"), "Event Id");
				assertEquals(customerIdHash, getJsonPath(document, "$.emails[0].ctx.customerIdHash"), "Context Customer ID Hash");
				assertEquals("1234", getJsonPath(document, "$.emails[0].ctx.acctL4"));
				assertEquals(firstName, getJsonPath(document, "$.emails[0].ctx.firstName"));
				assertEquals(lastName, getJsonPath(document, "$.emails[0].ctx.lastName"));
			});
		});
	}

	@Test
	@DisplayName("US Bank Preferences Unsubscribe")
	void usbank_preferences_unsubscribe() throws JsonProcessingException {
		customerIdHash = "ABC";
		email = "charlesb@yesmail.com";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addServiceAction("marketing", ServiceAction.REMOVE);

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertFalse(pathExists(document, "$.profile"), "Profile");
			});
			assertAll("Services", () -> {
				assertEquals(0, getJsonPath(document, "$.services.add.length()"));
				assertEquals(1, getJsonPath(document, "$.services.remove.length()"));
				assertEquals("marketing", getJsonPath(document, "$.services.remove[0]"));
			});
			assertAll("Custom Resources", () -> {
				assertFalse(pathExists(document, "$.customResources"));
			});
			assertAll("Triggered Email", () -> {
				assertFalse(pathExists(document, "$.emails"), "Triggered Email");
			});
		});
	}

	@Test
	@DisplayName("US Bank Preferences Subscribe")
	void usbank_preferences_subscribe() throws JsonProcessingException {
		customerIdHash = "ABC";
		email = "charlesb@yesmail.com";
		firstName = "Charles";
		lastName = "Berger";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addServiceAction("marketing", ServiceAction.ADD);
		eventId = "EVTsign_uppageconfirmation_bmw";
		profileRequest.setTriggeredEventId(eventId);
		profileRequest.setTriggeredEventRecipient(email);
		profileRequest.addPersonalizationField("customerIdHash", customerIdHash);
		profileRequest.addPersonalizationField("acctL4", "1234");
		profileRequest.addPersonalizationField("firstName", firstName);
		profileRequest.addPersonalizationField("lastName", lastName);

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertFalse(pathExists(document, "$.profile"), "Profile");
			});
			assertAll("Services", () -> {
				assertEquals(1, getJsonPath(document, "$.services.add.length()"));
				assertEquals("marketing", getJsonPath(document, "$.services.add[0]"));
				assertEquals(0, getJsonPath(document, "$.services.remove.length()"));
			});
			assertAll("Custom Resources", () -> {
				assertFalse(pathExists(document, "$.customResources"));
			});
			assertAll("Triggered Email", () -> {
				assertEquals(email, getJsonPath(document, "$.emails[0].email"), "Email");
				assertEquals(eventId, getJsonPath(document, "$.emails[0].eventId"), "Event Id");
				assertEquals(customerIdHash, getJsonPath(document, "$.emails[0].ctx.customerIdHash"), "Context Customer ID Hash");
				assertEquals("1234", getJsonPath(document, "$.emails[0].ctx.acctL4"));
				assertEquals(firstName, getJsonPath(document, "$.emails[0].ctx.firstName"));
				assertEquals(lastName, getJsonPath(document, "$.emails[0].ctx.lastName"));
			});
		});
	}

	@Test
	@DisplayName("Multiple CustomResources and Records")
	void multiple_custom_resources_and_records() throws JsonProcessingException {
		customerIdHash = "ABC";
		email = "charlesb@yesmail.com";
		firstName = "Charles";
		profileRequest = new ProfileRequest(customerIdHash);
		profileRequest.addProfileField("email", email);
		profileRequest.addProfileField("firstName", firstName);
		profileRequest.addServiceAction(Map.of(
			"marketing", ServiceAction.ADD,
			"transactional", ServiceAction.REMOVE
		));
		customResource1Name = "cusResource1";
		CustomResourceRequest resource1 = new CustomResourceRequest(customResource1Name);
		resource1.addRecord(new CustomResourceRecord(Map.of(
			"email", email,
			"column1", "value1",
			"column2", 1
		)));
		resource1.addRecord(new CustomResourceRecord(Map.of(
			"email", email,
			"column1", "value2",
			"column2", 2
		)));
		profileRequest.addCustomResourceRequest(resource1);
		String customResource2Name = "cusResource2";
		CustomResourceRequest resource2 = new CustomResourceRequest(customResource2Name);
		resource2.addRecord(new CustomResourceRecord(Map.of(
			"email", email,
			"firstName", firstName
		)));
		profileRequest.addCustomResourceRequest(resource2);

		jsonStr = objectMapper.writeValueAsString(profileRequest);
		LOG.info("body: {}", jsonStr);
		Object document = getDocument(jsonStr);

		assertAll("Check JSON", () -> {
			assertAll("Profile", () -> {
				assertEquals(email, getJsonPath(document, "$.profile.email"), "Email");
				assertEquals(firstName, getJsonPath(document, "$.profile.firstName"), "firstName");
			});
			assertAll("Services", () -> {
				assertEquals(1, getJsonPath(document, "$.services.add.length()"));
				assertEquals("marketing", getJsonPath(document, "$.services.add[0]"));
				assertEquals(1, getJsonPath(document, "$.services.remove.length()"));
				assertEquals("transactional", getJsonPath(document, "$.services.remove[0]"));
			});
			assertAll("Custom Resources", () -> {
				assertEquals(2, getJsonPath(document, "$.customResources.length()"));
				assertEquals(customResource1Name, getJsonPath(document, "$.customResources[0].name"));
				assertEquals(2, getJsonPath(document, "$.customResources[0].records.length()"));
				assertEquals(email, getJsonPath(document, "$.customResources[0].records[0].email"));
				assertEquals("value1", getJsonPath(document, "$.customResources[0].records[0].column1"));
				assertEquals(1, getJsonPath(document, "$.customResources[0].records[0].column2"));
				assertEquals(email, getJsonPath(document, "$.customResources[0].records[1].email"));
				assertEquals("value2", getJsonPath(document, "$.customResources[0].records[1].column1"));
				assertEquals(2, getJsonPath(document, "$.customResources[0].records[1].column2"));

				assertEquals(customResource2Name, getJsonPath(document, "$.customResources[1].name"));
				assertEquals(1, getJsonPath(document, "$.customResources[1].records.length()"));
				assertEquals(email, getJsonPath(document, "$.customResources[1].records[0].email"));
				assertEquals(firstName, getJsonPath(document, "$.customResources[1].records[0].firstName"));
			});
			assertAll("Triggered Email", () -> {
				assertFalse(pathExists(document, "$.emails"), "Email");
			});
		});
	}
}
