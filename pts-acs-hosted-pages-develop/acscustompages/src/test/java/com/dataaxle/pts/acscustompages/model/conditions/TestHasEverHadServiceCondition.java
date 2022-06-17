package com.dataaxle.pts.acscustompages.model.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestHasEverHadServiceCondition {

	private ProfileResponse profileResponse;

	private Condition condition;

	@BeforeEach
	void setup() {
		condition = new HasEverHadServiceCondition("memberOfHobby", 0, "hobby");
	}

	@Test
	void evaluatesTrue() {
		profileResponse = getProfile();
		profileResponse.getProfile().addField("cusEverSubHobby", true);
		assertTrue(condition.evaluate(profileResponse), "Member of hobby true");
	}

	@Test
	void evaluatesFalse() {
		profileResponse = getProfile();
		profileResponse.getProfile().addField("cusEverSubHobby", false);
		assertFalse(condition.evaluate(profileResponse), "Member of hobby false");
	}

	@Test
	void serviceFlagNotFound() {
		profileResponse = getProfile();
		Exception ex = assertThrows(IllegalStateException.class, () -> condition.evaluate(profileResponse));
		assertEquals("Service membership flag 'cusEverSubHobby' not found in profile!", ex.getMessage());
	}

	@Test
	void serviceMembershipFlagNotBoolean() {
		profileResponse = getProfile();
		profileResponse.getProfile().addField("cusEverSubHobby", "foo");
		Exception ex = assertThrows(IllegalStateException.class, () -> condition.evaluate(profileResponse));
		assertEquals("Service membership flag 'cusEverSubHobby' should be boolean!", ex.getMessage());
	}

	private ProfileResponse getProfile() {
		return StubbedProfileRepository.search("kalmbachmedia", Map.of("email", "charlesb@yesmail.com"));
	}
}
