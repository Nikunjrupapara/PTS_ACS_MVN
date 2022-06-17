package com.dataaxle.pts.acscustompages.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.form.ProfileDetails;
import com.dataaxle.pts.acscustompages.model.validation.ValidateString;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationRule;
import com.dataaxle.pts.acscustompages.utils.MessageCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestValidateString {

	private ValidationRule stringValidator;

	private Map<String, Object> fields;

	private ValidationResult validationResult;

	@BeforeEach
	void setup() {
		fields = new HashMap<>();
	}

	@ParameterizedTest
	@MethodSource("required_min10_max_20_uppercase_data")
	void required_min10_max_20_uppercase(String value, boolean hasErrors, String expectedError) {
		stringValidator = new ValidateString("firstName", true, 10, 20, "[A-Z]*");
		fields.put("firstName", value);
		validationResult = stringValidator.validate(fields);
		assertEquals(hasErrors, !validationResult.isValid());
		assertEquals(expectedError, validationResult.getErrorCode().getMessageCode());
	}

	private static Stream<Arguments> required_min10_max_20_uppercase_data() {
		return Stream.of(
			arguments(null, true, "message.is.required"),
			arguments("", true, "message.is.required"),
			arguments("abc", true, "message.is.too.short"),
			arguments("abcdefghijklmnopqrstuvwxyz", true, "message.is.too.long"),
			arguments("abcdefghij", true, "message.has.invalid.format"),
			arguments("ABCDEFGHIJ", false, "")
		);
	}

	@ParameterizedTest
	@MethodSource("required_min2_max30_alphautf8_data")
	void required_min2_max30_alphautf8(String value, boolean hasErrors, String expectedError) {
		stringValidator = new ValidateString("firstName", true, null, null, "[\\p{L} '-\\.]{2,30}");
		fields.put("firstName", value);
		validationResult = stringValidator.validate(fields);
		assertEquals(hasErrors, !validationResult.isValid());
		assertEquals(expectedError, validationResult.getErrorCode().getMessageCode());
	}

	private static Stream<Arguments> required_min2_max30_alphautf8_data() {
		return Stream.of(
			arguments(null, true, "message.is.required"),
			arguments("", true, "message.is.required"),
			arguments("François", false, ""),
			arguments("Jean-Piérre", false, ""),
			arguments("Jo", false, ""),
			arguments("X", true, "message.has.invalid.format"),
			arguments("abcdefghijklmnopqrstuvwxyz12345", true, "message.has.invalid.format"),
			arguments("Charles", false, ""),
			arguments("查爾斯", false, "")
		);
	}

	@ParameterizedTest
	@MethodSource("not_required_min0_max1_YN_data")
	void not_required_min1_max1_YN(String value, boolean hasErrors, String expectedError) {
		stringValidator = new ValidateString("testField", false, 0, 1, "[Y|N]");
		fields.put("testField", value);
		validationResult = stringValidator.validate(fields);
		assertEquals(hasErrors, validationResult.isNotValid());
		assertEquals(expectedError, validationResult.getErrorCode().getMessageCode());
	}

	private static Stream<Arguments> not_required_min0_max1_YN_data() {
		return Stream.of(
			arguments("Y", false, ""),
			arguments("y", true, MessageCodes.FIELD_HAS_INVALID_FORMAT),
			arguments(null, true, MessageCodes.FIELD_HAS_INVALID_FORMAT)
		);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource
	@DisplayName("Boscov's FHF Validate Org Name")
	public void boscovsFhfOrgName(String name) {
		String fieldName = "orgName";
		ValidateString validator = new ValidateString(fieldName, true, 5, 255,
			"^[\\p{Graph} ]{5,255}$");
		assertEquals(ErrorCode.VALID, validator.validate(Map.of(fieldName, name)).getErrorCode());
	}

	public static List<String> boscovsFhfOrgName() {
		return List.of(
			"AIDS TASK FORCE",
			"BETHLEHEM ELEMENTARY SCHOOL",
			"BISHOP DONAHUE MEM HIGH SCHOOL",
			"BOB'S WISH GLOBES OF HOPE",
			"BRIDGE STREET MIDDLE SCHOOL",
			"CHAPTER JPEO",
			"CHAPTER J P.E.O.",
			"CHILDREN'S HOME SOCIETY OF WEST VIRGINIA",
			"CORPUS CHRISTI LADIES GUILD",
			"CORPUS CHRISTI SCHOOL",
			"DELTA KAPPA GAMMA PSI CHAPTER",
			"ELM GROVE ELEMENTARY SCHOOL",
			"FAMILY SERVICE - UPPER OHIO VALLEY",
			"FAMILY SERVICE-UPPER OHIO VALLEY",
			"GABRIEL PROJECT",
			"GROW WARWOOD PRIDE",
			"HARMONY HOUSE",
			"HILLTOP ELEMENTARY SCHOOL",
			"HOUSE OF THE CARPENTER",
			"LINSLY SCHOOL",
			"MADISON ELEMENTARY SCHOOL",
			"MICHAEL'S MEANIES INC.",
			"MOUNTAINEER PTSA",
			"MT. OLIVET LION'S CLUB",
			"NATIONAL YOUTH ADVOCATE PROGRAM",
			"NORTHERN PANHANDLE HEAD START, INC.",
			"NORTHERN PROGRAMS CHAPTER- GABRIEL PROJECT OF WEST VIRGINIA",
			"RITCHIE ELEMENTARY SCHOOL",
			"SECOND CHANCE TECHNOLOGY CORP.",
			"SEEING HAND ASSOCIATION",
			"SHERRARD MIDDLE SCHOOL",
			"SOROPTIMIST INTERNATIONAL OF WHEELING",
			"STEENROD ELEMENTARY SCHOOL",
			"ST MICHAEL PARISH SCHOOL",
			"ST VINCENT DEPAUL SCHOOL",
			"ST. VINCENT DE PAUL SCHOOL",
			"THRESHOLD REHABILITATION SERVICES, INC.",
			"TRIADELPHIA MIDDLE SCHOOL",
			"VETERAN STAND DOWN",
			"WARWOOD SCHOOL",
			"WHEELING COUNTRY DAY SCHOOL",
			"WHEELING HEALTH RIGHT, INC.�",
			"WHEELING LIONS CLUB",
			"WHEELING MIDDLE SCHOOL",
			"WHEELING PARK HIGH SCHOOL CHORAL BOOSTERS",
			"ZANE TRACE CHAPTER NSDAR"
		);
	}
}
