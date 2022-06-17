package com.dataaxle.pts.acscustompages.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.validation.ValidateDate;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

public class TestValidateDate {

	private static final String fieldName = "bday";

	private static ValidationRule dateValidator;

	private static ValidationRule dateValidatorOptional;

	private static ValidationRule dateValidatorRange;

	private static ValidationRule dateValidatorMin;

	private static ValidationRule dateValidatorMax;

	@BeforeAll
	static void setupClass() {
		dateValidator = new ValidateDate(fieldName, true);
		dateValidatorOptional = new ValidateDate(fieldName, false);
		dateValidatorRange = new ValidateDate(fieldName, true, "uuuu/MM/dd","2021/01/01", "2021/12/31");
		dateValidatorMin = new ValidateDate(fieldName, true, "uuuu/MM/dd","2022/01/01", null);
		dateValidatorMax = new ValidateDate(fieldName, true, "uuuu/MM/dd",null, "2020/12/31");
	}

	@BeforeEach
	void setup() {

	}

	@DisplayName("Invalid Date Tests")
	@ParameterizedTest(name = "{index} => {0}: {2}")
	@MethodSource("invalidDates")
	void invalidDates(String name, ValidationRule dateValidator, String dateStr, ErrorCode expectedError) {
		ValidationResult result = dateValidator.validate(Map.of(fieldName, dateStr));
		assertTrue(result.isNotValid(), "Value is invalid");
		assertEquals(expectedError, result.getErrorCode());
	}

	public static Stream<Arguments> invalidDates() {
		return Stream.of(
			arguments("Is Required", dateValidator, "", ErrorCode.REQUIRED),
			arguments("Text input", dateValidator, "abcdefgh", ErrorCode.INVALID),
			arguments("Incorrect pattern", dateValidatorRange, "01/01/2021", ErrorCode.INVALID),
			arguments("Month out of range", dateValidator, "2021/13/01", ErrorCode.INVALID),
			arguments("Day out of range", dateValidator, "2021/12/33", ErrorCode.INVALID),
			arguments("Not a leap year", dateValidator, "2021/02/29", ErrorCode.INVALID),
			arguments("Before permitted range", dateValidatorMin, "2020/12/31", ErrorCode.TOO_LOW),
			arguments("After permitted range", dateValidatorMax, "2022/01/01", ErrorCode.TOO_HIGH)
		);
	}

	@DisplayName("Valid Date Tests")
	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("validDates")
	void validDates(String dateStr, ValidationRule dateValidator) {
		ValidationResult result = dateValidator.validate(Map.of(fieldName, dateStr));
		assertTrue(result.isValid(), "Value is valid");
	}

	public static Stream<Arguments> validDates() {
		return Stream.of(
			arguments("2021/03/01", dateValidator),
			arguments("2020/02/29", dateValidator),
			arguments("2021/06/03", dateValidatorRange),
			arguments("", dateValidatorOptional),
			arguments("2021/06/03", dateValidatorOptional)
		);
	}
}
