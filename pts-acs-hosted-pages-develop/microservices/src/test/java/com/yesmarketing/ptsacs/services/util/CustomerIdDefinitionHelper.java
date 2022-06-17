package com.yesmarketing.ptsacs.services.util;

import com.yesmarketing.ptsacs.common.exception.NoCustomerIdDefinitionException;
import com.yesmarketing.ptsacs.services.enums.HashingFunction;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import com.yesmarketing.ptsacs.services.model.CustomerIdField;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomerIdDefinitionHelper {

	private static final Map<String, CustomerIdDefinition> definitions = new HashMap<>();

	static {
		String company = "emailOnly";
		definitions.put(company, new CustomerIdDefinition(company, Collections.singletonList(
			new CustomerIdField("email", true)), "", null));
		company = "emailOnlySha256";
		definitions.put(company, new CustomerIdDefinition(company, Collections.singletonList(
			new CustomerIdField("email", true)), "", HashingFunction.SHA256));
		company = "emailOnlySha512";
		definitions.put(company, new CustomerIdDefinition(company, Collections.singletonList(
			new CustomerIdField("email", true)), "", HashingFunction.SHA512));
		company = "firstNameOptEmailSha512";
		definitions.put(company, new CustomerIdDefinition(company, Arrays.asList(
			new CustomerIdField("firstName", false), new CustomerIdField("email", true)),
			"|", HashingFunction.SHA512));
		company = "firstNameEmailSha512";
		definitions.put(company, new CustomerIdDefinition(company, Arrays.asList(
			new CustomerIdField("firstName", true), new CustomerIdField("email", true)),
			"|", HashingFunction.SHA512));
		company = "musicnotes";
		definitions.put(company, new CustomerIdDefinition(company, Collections.singletonList(
			new CustomerIdField("cusShopperId", true)), "", HashingFunction.SHA512));
		company = "usbank";
		definitions.put(company, new CustomerIdDefinition(company, Arrays.asList(
			new CustomerIdField("email", true), new CustomerIdField("cusAcctL4", true),
			new CustomerIdField("cusBrand2", true)), "|", HashingFunction.SHA512));
		company = "customerid";
		definitions.put(company, new CustomerIdDefinition(company, Arrays.asList(new CustomerIdField("cusCustomerId",
				true)), "", HashingFunction.SHA512));
	}

	public static CustomerIdDefinition getInstance(String company) {
		if (definitions.containsKey(company)) {
			return definitions.get(company);
		}
		throw new NoCustomerIdDefinitionException(company);
	}
}
