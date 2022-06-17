package com.dataaxle.pts.acscustompages.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.exception.AppDetailsNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.AppPage;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResult;
import com.dataaxle.pts.acscustompages.model.validation.ValidationResults;
import com.dataaxle.pts.acscustompages.service.RecaptchaService;
import com.dataaxle.pts.acscustompages.service.validation.impl.DefaultValidationService;
import com.dataaxle.pts.acscustompages.stubs.StubbedAppDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestValidationService {

	private ValidationService validationService;

	@BeforeEach
	void setup() {
		RecaptchaService recaptchaService = Mockito.mock(RecaptchaService.class);
		validationService = new DefaultValidationService(recaptchaService);
	}

	@ParameterizedTest(name = "{index}, {0}/{1}/{2}")
	@MethodSource("validate_data")
	void validate(String company, String contextPath, String pageName, Map<String, Object> fieldValues, boolean errorsFound,
				  Map<String, ValidationResult> results) {
		AppPage appPage = getAppPage(company, contextPath, pageName);
		ValidationResults validationResults = validationService.validate(appPage.getFields(), fieldValues);
		assertEquals(errorsFound, validationResults.hasErrors(), "errors found");
		compareValidationResults(results, validationResults);
	}

	private static Stream<Arguments> validate_data() {
		return Stream.of(
			arguments(
				"usbank", "preferences", "signup",
				Map.of(
					"email", "charlesb@yesmail.com",
					"confirmEmail", "charlesb@yesmail.com",
					"cusAcctL4", "1234",
					"cusPartner", "BMW"), false,
				Map.of(
					"email", new ValidationResult("email", "charlesb@yesmail.com", true, ErrorCode.VALID),
					"confirmEmail", new ValidationResult("confirmEmail", "charlesb@yesmail.com", true, ErrorCode.VALID),
					"cusAcctL4", new ValidationResult("cusAcctL4", "1234", true, ErrorCode.VALID),
					"cusPartner", new ValidationResult("cusPartner", "BMW", true, ErrorCode.VALID))
				),
			arguments(
				"usbank", "preferences", "signup",
				Map.of(
					"email", "charles.berger@1@data-axle.com",
					"confirmEmail", "charles.berger@data-axle.com",
					"cusAcctL4", "Abcd",
					"cusPartner", "BMW"
					), true,
				Map.of(
					"email", new ValidationResult("email", "charles.berger@1@data-axle.com", false, ErrorCode.INVALID),
					"confirmEmail", new ValidationResult("confirmEmail", "charles.berger@data-axle.com", true, ErrorCode.VALID),
					"cusAcctL4", new ValidationResult("cusAcctL4", "Abcd", false, ErrorCode.INVALID),
					"cusPartner", new ValidationResult("cusPartner", "BMW", true, ErrorCode.VALID),
					"global", new ValidationResult("global", "", false, ErrorCode.ERRORS_FOUND)
				)
			),
			arguments(
				"usbank", "preferences", "signup-new",
				getValues(), true,
				Map.of(
					"email", new ValidationResult("email", "charlesb@yesmail.com", true, ErrorCode.VALID),
					"cusAcctL4", new ValidationResult("cusAcctL4", "1234", true, ErrorCode.VALID),
					"cusPartner", new ValidationResult("cusPartner", "BMW", true, ErrorCode.VALID),
					"firstName", new ValidationResult("firstName", "Charles", true, ErrorCode.VALID),
					"lastName", new ValidationResult("lastName", "Berger", true, ErrorCode.VALID),
					"cusOver18", new ValidationResult("cusOver18", "false", false, ErrorCode.REQUIRED),
					"cusZipCode", new ValidationResult("cusZipCode", "12345", true, ErrorCode.VALID),
					"global", new ValidationResult("global", "", false, ErrorCode.ERRORS_FOUND)
				)
			)
		);
	}

	private AppPage getAppPage(String company, String contextPath, String pageName) {
		AppDetails.AppDetailsId appId = new AppDetails.AppDetailsId(company, contextPath);
		AppDetails appDetails = StubbedAppDetailsRepository.findById(appId).orElseThrow(() -> new AppDetailsNotFoundException(company, contextPath));
		return appDetails.getPage(pageName);
	}

	private void compareValidationResults(Map<String, ValidationResult> expected, ValidationResults validationResults) {
		expected.forEach((key, value) -> {
			if (!validationResults.hasResult(key)) {
				fail(String.format("Expected ValidationResult for field %s", key));
			}
			ValidationResult validationResult = validationResults.getResult(key);
			assertEquals(value, validationResult, String.format("Results for field %s are not the same", key));
		});
		validationResults.getResults().keySet()
			.forEach(fieldName -> {
				if (!expected.containsKey(fieldName)) {
					fail(String.format("No expected result for field %s", fieldName));
				}
			});
	}

	private static Map<String, Object> getValues() {
		Map<String, Object> data = new HashMap<>();
		data.put("email", "charlesb@yesmail.com");
		data.put("cusAcctL4", "1234");
		data.put("cusPartner", "BMW");
		data.put("firstName", "Charles");
		data.put("lastName", "Berger");
		data.put("cusOver18", null);
		data.put("cusZipCode", "12345");
		data.put("newUser", "Y");
		data.put("subscribed", "N");
		return data;
	}
}
