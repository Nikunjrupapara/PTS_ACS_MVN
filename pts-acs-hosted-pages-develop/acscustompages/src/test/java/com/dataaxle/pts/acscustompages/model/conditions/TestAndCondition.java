package com.dataaxle.pts.acscustompages.model.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class TestAndCondition {

	@Nested
	class ProfileOnly {

		private ProfileResponse profileResponse;

		private Condition subbedAndDone;

		@BeforeEach
		void setup() {
			Condition hasScience = new HasServiceCondition("subscribedToScience", 0, "science");
			Condition beginnerSeriesDone = new ProfileStringFieldValueCondition("beginnerSeriesDone", 1, "cusBgn_asy", Collections.singletonList("Yes"));
			subbedAndDone = new AndCondition("subbedAndDone", 0, hasScience, beginnerSeriesDone);
			profileResponse = getProfile();
		}

		@Test
		void evaluatesToTrue() {
			profileResponse.getProfile().addField("cusBgn_asy", "Yes");
			assertTrue(subbedAndDone.evaluate(profileResponse));
		}

		@Test
		void evaluatesToFalse1() {
			profileResponse.getProfile().addField("cusBgn_asy", "");
			assertFalse(subbedAndDone.evaluate(profileResponse));
		}

		@Test
		void evaluatesToFalse2() {
			assertFalse(subbedAndDone.evaluate(profileResponse));
		}
	}

	@Nested
	class FormOnly {
		private DynamicFormBean dynamicFormBean;

		Condition andCondition;

		@BeforeEach
		void setup() {
			dynamicFormBean = new DynamicFormBean();
			Condition countryIsUs = new FormFieldValueCondition("countryIsUs", 0, "countryCode", "US");
			Condition resident = new FormFieldValueCondition("resident", 1, "resident", "yes");
			andCondition = new AndCondition("residentInUs", 0, countryIsUs, resident);
		}

		@Test
		void evaluatesToTrue() {
			dynamicFormBean.initialiseField("countryCode", "US");
			dynamicFormBean.initialiseField("resident", "yes");
			assertTrue(andCondition.evaluate(dynamicFormBean));
		}

		@Test
		void evaluatesToFalse() {
			dynamicFormBean.initialiseField("countryCode", "CA");
			dynamicFormBean.initialiseField("resident", "yes");
			assertFalse(andCondition.evaluate(dynamicFormBean));
		}
	}

	@Nested
	class ProfileAndForm {
		private ProfileResponse profileResponse;

		private DynamicFormBean dynamicFormBean;

		Condition andCondition;

		@BeforeEach
		void setup() {
			profileResponse = getProfile();
			dynamicFormBean = new DynamicFormBean();
			Condition hasScience = new HasServiceCondition("subscribedToScience", 0, "science");
			Condition optIntoHobby = new FormFieldValueCondition("optIntoHobby", 1, "hobby", "yes");
			andCondition = new AndCondition("optInHobby", 0, hasScience, optIntoHobby);
		}

		@Test
		void evaluatesToTrue() {
			dynamicFormBean.initialiseField("hobby", "yes");
			assertTrue(andCondition.evaluate(profileResponse, dynamicFormBean));
		}

		@Test
		void evaluatesToFalse() {
			dynamicFormBean.initialiseField("hobby", "");
			assertFalse(andCondition.evaluate(profileResponse, dynamicFormBean));
		}
	}


	private ProfileResponse getProfile() {
		return StubbedProfileRepository.search("kalmbachmedia", Map.of("email", "charlesb@yesmail.com"));
	}
}
