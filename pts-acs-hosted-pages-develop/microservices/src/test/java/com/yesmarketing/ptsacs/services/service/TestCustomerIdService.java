package com.yesmarketing.ptsacs.services.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import com.yesmarketing.acsapi.model.AdobeCampaignConstants;
import com.yesmarketing.ptsacs.admin.service.CustomerIdDefinitionService;
import com.yesmarketing.ptsacs.common.exception.NoCustomerIdDefinitionException;
import com.yesmarketing.ptsacs.services.configuration.ServicesRequestContext;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.exception.CustomerIdDefinitionMismatchException;
import com.yesmarketing.ptsacs.services.exception.MissingCustomerIdFieldValueException;
import com.yesmarketing.ptsacs.services.service.impl.CustomerIdServiceImpl;
import com.yesmarketing.ptsacs.services.util.CustomerIdDefinitionHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class TestCustomerIdService {

	private final CustomerIdDefinitionService customerIdDefinitionService = Mockito.mock(CustomerIdDefinitionService.class);

	private CustomerIdService customerIdService;

	private String company;

	private Map<String, String> searchFields;

	private ProfileSearchDto profileSearchDto;

	private CustomerIdService getCustomerIdService(String company) {
		ServicesRequestContext servicesRequestContext = new ServicesRequestContext();
		servicesRequestContext.setCompany(company);
		return new CustomerIdServiceImpl(customerIdDefinitionService);
	}

	@BeforeEach
	void setup() {
		searchFields = new HashMap<>();
		profileSearchDto = new ProfileSearchDto(searchFields);
		Mockito
			.when(customerIdDefinitionService.get(anyString()))
			.thenAnswer(invocation -> {
				String company = invocation.getArgument(0);
				return CustomerIdDefinitionHelper.getInstance(company);
			});
	}

	@Test
	@DisplayName("Test customer id - email primary key SHA256")
	void testCustomerIdForEmailHash256Id() throws Exception{
		company = "emailOnlySha256";
		customerIdService = getCustomerIdService(company);
		String email = "andreyK@yesmail.com";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		String customerId = customerIdService.getCustomerIdHash(company,profileSearchDto);
		assertEquals(DigestUtils.sha256Hex(email.toLowerCase()).toUpperCase(), customerId, "wrong customerId value!");
	}

	@Test
	@DisplayName("Test customer id - email primary key SHA512")
	void testCustomerIdForEmailHash512Id() throws Exception{
		company = "emailOnlySha512";
		customerIdService = getCustomerIdService(company);
		String email = "andreyK@yesmail.com";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		String customerId = customerIdService.getCustomerIdHash(company,profileSearchDto);
		assertEquals(DigestUtils.sha512Hex(email.toLowerCase()).toUpperCase(), customerId,"wrong customerId value!");
	}

	@Test
	@DisplayName("Test customer id - SHA512(firstName|email)")
	void testCustomerIdForFirstNameEmailHash512Id() throws Exception{
		company = "firstNameEmailSha512";
		customerIdService = getCustomerIdService(company);
		String email = "andreyK@yesmail.com";
		String firstName = "Andrey";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		searchFields.put(AdobeCampaignConstants.FIRSTNAME, firstName);
		String customerId = customerIdService.getCustomerIdHash(company,profileSearchDto);
		assertEquals(DigestUtils.sha512Hex(firstName+"|"+email.toLowerCase()).toUpperCase(), customerId, "wrong customerId value!");
	}

	@Test
	@DisplayName("Test customer id - SHA512(firstName|email) - empty firstName")
	void testCustomerIdForFirstNameOptEmailHash512Id() throws Exception{
		company = "firstNameOptEmailSha512";
		customerIdService = getCustomerIdService(company);
		String email = "andreyk@yesmail.com";
		String firstName = "";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		searchFields.put(AdobeCampaignConstants.FIRSTNAME, firstName);
		String customerId = customerIdService.getCustomerIdHash(company,profileSearchDto);
		assertEquals(DigestUtils.sha512Hex(firstName+"|"+email).toUpperCase(), customerId, "wrong customerId value!");
	}

	@Test
	@DisplayName("Test Customer Id - SHA512(cusCustomerId)")
	void sha512CusCustomerId() {
		company = "customerid";
		customerIdService = getCustomerIdService(company);
		String customerId = "charlesb";
		searchFields.put(AdobeCampaignConstants.CUS_CUSTOMER_ID, customerId);
		String customerIdOut = customerIdService.getCustomerIdHash(company,profileSearchDto);
		assertEquals(DigestUtils.sha512Hex(customerId).toUpperCase(), customerIdOut, "wrong customerId value!");
	}

	@Test
	@DisplayName("Test that error is thrown if necessary customerId field is not provided")
	void testCustomerIdExceptionForMissingField(){
		company = "firstNameEmailSha512";
		customerIdService = getCustomerIdService(company);
		String email = "andreyk@yesmail.com";
		String firstName = "";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		searchFields.put(AdobeCampaignConstants.FIRSTNAME, firstName);
		assertThatThrownBy(() -> customerIdService.getCustomerIdHash(company,profileSearchDto))
		.isInstanceOf(MissingCustomerIdFieldValueException.class)
		.hasFieldOrPropertyWithValue("company", company)
		.hasFieldOrPropertyWithValue("fieldName", "firstName");
	}

	@Test
	@DisplayName("Test that error is thrown if CustomerIdDefinition is not found")
	void testCustomerIdExceptionForMissingCustomerIdDefinition(){
		company = "nocustomeriddefinition";
		customerIdService = getCustomerIdService(company);
		String email = "andreyk@yesmail.com";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		assertThatThrownBy(() -> customerIdService.getCustomerIdHash(company,profileSearchDto))
		.isInstanceOf(NoCustomerIdDefinitionException.class)
		.hasFieldOrPropertyWithValue("company", company);
	}

	@Test
	@DisplayName("Request payload does not match customerId definition - too many fields")
	void payloadAndDefinitionMismatchTooMany() {
		company = "emailOnly";
		customerIdService = getCustomerIdService(company);
		String email = "test@test.com";
		String firstName = "Testerson";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		searchFields.put(AdobeCampaignConstants.FIRSTNAME, firstName);
		assertThatThrownBy(() -> customerIdService.getCustomerIdHash(company,profileSearchDto))
			.isInstanceOf(CustomerIdDefinitionMismatchException.class);
	}

	@Test
	@DisplayName("Request payload does not match customerId definition - not enough fields")
	void payloadAndDefinitionMismatchTooFew() {
		company = "firstNameEmailSha512";
		customerIdService = getCustomerIdService(company);
		String email = "test@test.com";
		searchFields.put(AdobeCampaignConstants.EMAIL, email);
		assertThatThrownBy(() -> customerIdService.getCustomerIdHash(company,profileSearchDto))
			.isInstanceOf(CustomerIdDefinitionMismatchException.class);
	}

	@Test
	@DisplayName("Request payload does not match customerId definition - incorrect fields")
	void payloadAndDefinitionMismatchIncorrect() {
		company = "emailOnly";
		customerIdService = getCustomerIdService(company);
		String firstName = "Testerson";
		searchFields.put(AdobeCampaignConstants.FIRSTNAME, firstName);
		assertThatThrownBy(() -> customerIdService.getCustomerIdHash(company,profileSearchDto))
			.isInstanceOf(CustomerIdDefinitionMismatchException.class);
	}
}
