package com.dataaxle.pts.acscustompages.model.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public class TestProfileFieldValueCondition {

	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("evaluateToTrueParameters")
	@DisplayName("Evaluate to true")
	void evaluateToTrue(String name, Condition condition, ProfileResponse profileResponse) {
		assertTrue(condition.evaluate(profileResponse), () -> String.format("Expected %s to evaluate to true", name));
	}

	static Stream<Arguments> evaluateToTrueParameters() {
		final String CUS_BGN_ASY = "cusBgn_asy";
		ProfileResponse pr0 = StubbedProfileRepository.search("kalmbachmedia", Map.of("email", "charlesb@yesmail.com"));
		pr0.getProfile().addField(CUS_BGN_ASY, "Yes");
		ProfileResponse pr1 = new ProfileResponse(pr0);
		pr1.getProfile().addField(CUS_BGN_ASY, "yes");
		ProfileResponse pr2 = new ProfileResponse(pr0);
		pr1.getProfile().addField(CUS_BGN_ASY, "YES");
		Condition c0 = new ProfileStringFieldValueCondition(CUS_BGN_ASY, 0, CUS_BGN_ASY, Collections.singletonList("Yes"));
		Condition c1 = new ProfileStringFieldValueCondition(CUS_BGN_ASY, 0, CUS_BGN_ASY, Collections.singletonList("Yes"), false);
		ProfileResponse pr3 = StubbedProfileRepository.search("resideo", Map.of("email", "charlesb@yesmail.com"));
		pr3.getProfile().addField("cusOptStatus", true);
		Condition c2 = new ProfileBooleanFieldValueCondition("optStatus", 0, "cusOptStatus");
		return Stream.of(
			arguments("cusBgn_asy = Yes", c0, pr0),
			arguments("cusBgn_asy = yes", c1, pr1),
			arguments("cusBgn_asy = YES", c1, pr2),
			arguments("optStatus is true", c2, pr3)
		);
	}

	@ParameterizedTest(name = "{index} => {0}")
	@MethodSource("evaluateToFalseParameters")
	@DisplayName("Evaluate to false")
	void evaluateToFalse(String name, Condition condition, ProfileResponse profileResponse) {
		assertFalse(condition.evaluate(profileResponse), () -> String.format("Expected %s to evaluate to false", name));
	}

	static Stream<Arguments> evaluateToFalseParameters() {
		final String CUS_BGN_ASY = "cusBgn_asy";
		ProfileResponse pr0 = StubbedProfileRepository.search("kalmbachmedia", Map.of("email", "charlesb@yesmail.com"));
		ProfileResponse pr1 = new ProfileResponse(pr0);
		pr1.getProfile().addField(CUS_BGN_ASY, "No");
		ProfileResponse pr2 = new ProfileResponse(pr0);
		pr2.getProfile().addField(CUS_BGN_ASY, "");
		ProfileResponse pr3 = new ProfileResponse(pr0);
		pr2.getProfile().addField(CUS_BGN_ASY, "no");
		Condition c0 = new ProfileStringFieldValueCondition(CUS_BGN_ASY, 0, CUS_BGN_ASY, Collections.singletonList("Yes"));
		Condition c1 = new ProfileStringFieldValueCondition(CUS_BGN_ASY, 0, CUS_BGN_ASY, Collections.singletonList("Yes"), false);
		ProfileResponse pr4 = StubbedProfileRepository.search("resideo", Map.of("email", "charlesb@yesmail.com"));
		pr4.getProfile().addField("cusOptStatus", false);
		Condition c2 = new ProfileBooleanFieldValueCondition("optStatus", 0, "cusOptStatus");
		return Stream.of(
			arguments("cusBgn_asy is null", c0, pr0),
			arguments("cusBgn_asy = No", c0, pr1),
			arguments("cusBgn_asy = \"\"", c1, pr2),
			arguments("cusBgn_asy = no", c1, pr3),
			arguments("optStatus is false", c2, pr4)
		);
	}
}
