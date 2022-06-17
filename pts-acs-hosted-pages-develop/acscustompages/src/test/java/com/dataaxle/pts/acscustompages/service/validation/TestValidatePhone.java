package com.dataaxle.pts.acscustompages.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.validation.ValidatePhoneNumber;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestValidatePhone {

	@ParameterizedTest(name = "{index} => {0} is {1}")
	@MethodSource
	@DisplayName("Validate Phone with no confirmation")
	public void validateNoConfirmation(String number, ErrorCode expectedErrorCode) {
		String fieldName = "phone";
		ValidatePhoneNumber validator = new ValidatePhoneNumber(fieldName, true, false,
			null, List.of("US"));

		Map<String, Object> input;
		if (number != null) {
			input = Map.of(fieldName, number);
		} else {
			input = new HashMap<>();
			input.put(fieldName, null);
		}
		ValidationResult result = validator.validate(input);
		assertEquals(fieldName, result.getFieldName(), "Field Name");
		assertEquals(expectedErrorCode, result.getErrorCode(), "Error Code");
	}

	static Stream<Arguments> validateNoConfirmation() {
		return Stream.of(
			arguments(null, ErrorCode.REQUIRED),
			arguments("", ErrorCode.REQUIRED),
			arguments("foobar", ErrorCode.INVALID),
			arguments("123&456*7890", ErrorCode.INVALID),
			arguments("12345", ErrorCode.INVALID),
			arguments("6312832118321", ErrorCode.INVALID),
			arguments("+16312832118", ErrorCode.VALID),
			arguments("+1 631-283-2118", ErrorCode.VALID),
			arguments("(631) 283 2118", ErrorCode.VALID),
			arguments("63.12.83.2118", ErrorCode.VALID)
		);
	}

	@ParameterizedTest(name = "{index} => {0} equals {1} is {2}")
	@MethodSource
	@DisplayName("Validate Phone with confirmation")
	public void validateWithConfirmation(String number, String confirmNumber, boolean equal, String errorFieldName) {
		String fieldName = "phone", confirmFieldName = "confirmPhone";
		ValidatePhoneNumber validator = new ValidatePhoneNumber(fieldName, true, true,
			confirmFieldName, List.of("US"));

		Map<String, Object> input;
		if (number != null && confirmNumber != null) {
			input = Map.of(fieldName, number, confirmFieldName, confirmNumber);
		} else {
			input = new HashMap<>();
			input.put(fieldName, number);
			input.put(confirmFieldName, confirmNumber);
		}
		ValidationResult result = validator.validate(input);
		assertEquals(errorFieldName, result.getFieldName(), "Field Name");
		assertEquals(equal, result.getErrorCode() == ErrorCode.VALID, "Error Code");
	}

	static Stream<Arguments> validateWithConfirmation() {
		return Stream.of(
			arguments("+16312832118", null, false, "confirmPhone"),
			arguments("+16312832118", "", false, "confirmPhone"),
			arguments("+16312832118", "+81123821361", false, "phone"),
			arguments("+16312832118", "+16312832118", true, "phone"),
			arguments("+16312832118", "6312832118", true, "phone"),
			arguments("+16312832118", "631 283 2118", true, "phone")
		);
	}
}
