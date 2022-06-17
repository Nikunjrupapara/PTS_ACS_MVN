package com.dataaxle.pts.acscustompages.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.form.ProfileDetails;
import com.dataaxle.pts.acscustompages.model.validation.ValidateEmail;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestValidateEmail {

	private final String fieldName = "email";

	private ValidationRule emailValidator;

	private Map<String, Object> fields;

	private ValidationResult result;

	@BeforeEach
	void setup() {
		emailValidator = new ValidateEmail(fieldName, true, true, false, 6, 40);
		fields = new HashMap<>();
	}

	@ParameterizedTest
	@MethodSource("email_only_data")
	void email_only(String email, boolean hasErrors) {
		emailValidator = new ValidateEmail(fieldName, true, true, false, 6, 40);
		fields.put("email", email);
		result = emailValidator.validate(fields);
		assertEquals(hasErrors, !result.isValid(), "errors found");
	}

	private static Stream<Arguments> email_only_data() {
		return Stream.of(
			arguments(null, true),
			arguments("", true),
			arguments("x@y.c", true),
			arguments("abcdefghijklmnopqrstuwxyz1234567890@test.com", true),
			arguments("x@y@z.com", true),
			arguments("charles.berger@data-axle.com", false)
		);
	}

	@ParameterizedTest
	@MethodSource("email_with_confirmation_data")
	void email_with_confirmation(String email, String confirmEmail, boolean hasErrors) {
		emailValidator = new ValidateEmail(fieldName, true, true, true, 6, 40);
		fields.put("email", email);
		fields.put("confirmEmail", confirmEmail);
		result = emailValidator.validate(fields);
		assertEquals(hasErrors, !result.isValid(), "errors found");
	}

	private static Stream<Arguments> email_with_confirmation_data() {
		return Stream.of(
			arguments("charles.berger@data-axle.com", "charles.a.berger@data-axle.com", true),
			arguments("charles.berger@data-axle.com", "charles.berger@data-axle.com", false),
			arguments("charles.berger@data-axle.com", "", true)
		);
	}

	@Test
	void no_length_validation() {
		emailValidator = new ValidateEmail(fieldName, true, true, false, null, 0);
		fields.put("email", "charles.berger@data-axle.com");
		result = emailValidator.validate(fields);
		assertTrue(result.isValid());
	}

	@ParameterizedTest(name = "{index} => {0}, {1}, {2}")
	@DisplayName("Optional with format validation and confirmation")
	@MethodSource
	public void optionalWithFormatValidationAndConfirmation(String email, String confirmEmail, ErrorCode expected) {
		emailValidator = new ValidateEmail("email", false, true, true, 0, 255,
			"confirmEmail");
		fields.put("email", email);
		fields.put("confirmEmail", confirmEmail);
		result = emailValidator.validate(fields);
		assertEquals(expected, result.getErrorCode());
	}

	public static Stream<Arguments> optionalWithFormatValidationAndConfirmation() {
		return Stream.of(
			arguments("", "", ErrorCode.VALID),
			arguments("charlesb", "", ErrorCode.INVALID),
			arguments("charlesb@yesmail.com", "charles.berger@yesmail.com", ErrorCode.NO_MATCH),
			arguments("charlesb@yesmail.com", "", ErrorCode.REQUIRED),
			arguments("charlesb@yesmail.com", "charlesb@yesmail.com", ErrorCode.VALID)
		);
	}
}
