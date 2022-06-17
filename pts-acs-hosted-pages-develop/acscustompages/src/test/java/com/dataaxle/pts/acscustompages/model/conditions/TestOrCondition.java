package com.dataaxle.pts.acscustompages.model.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.form.DynamicFormBean;
import com.dataaxle.pts.acscustompages.stubs.StubbedProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class TestOrCondition {

	@Nested
	class ProfileOnly {

		private ProfileResponse profileResponse;

		private Condition orCondition;

		@BeforeEach
		void setup() {
			Condition beginnerSeriesStarted = new ProfileStringFieldValueCondition("beginnerSeriesStarted", 0, "cusBgnasy", Collections.singletonList("Yes"));
			Condition beginnerSeriesDone = new ProfileStringFieldValueCondition("beginnerSeriesDone", 1, "cusBgn_asy", Collections.singletonList("Yes"));
			orCondition = new OrCondition("inBeginnerSeries", 0, beginnerSeriesStarted, beginnerSeriesDone);
			profileResponse = getProfile();
		}

		@AfterEach
		void tearDown() {
			profileResponse.getProfile().getFields().remove("cusBgnasy");
			profileResponse.getProfile().getFields().remove("cusBgn_asy");
		}

		@Test
		void evaluatesToTrueAllTrue() {
			profileResponse.getProfile().addField("cusBgnasy", "Yes");
			profileResponse.getProfile().addField("cusBgn_asy", "Yes");
			assertTrue(orCondition.evaluate(profileResponse));
		}

		@Test
		void evaluatesToTrueOneTrue() {
			profileResponse.getProfile().addField("cusBgn_asy", "Yes");
			assertTrue(orCondition.evaluate(profileResponse));
		}

		@Test
		void evaluatesToFalse1() {
			assertFalse(orCondition.evaluate(profileResponse));
		}

		@Test
		void evaluatesToFalse2() {
			profileResponse.getProfile().addField("cusBgnasy", "");
			profileResponse.getProfile().addField("cusBgn_asy", "");
			assertFalse(orCondition.evaluate(profileResponse));
		}
	}

	@Nested
	class FormOnly {
		private DynamicFormBean dynamicFormBean;

		Condition orCondition;

		@BeforeEach
		void setup() {
			dynamicFormBean = new DynamicFormBean();
			Condition countryIsUs = new FormFieldValueCondition("countryIsUs", 0, "countryCode", "US");
			Condition resident = new FormFieldValueCondition("resident", 1, "resident", "yes");
			orCondition = new OrCondition("residentInUs", 0, countryIsUs, resident);
		}

		@Test
		void evaluatesToTrue() {
			dynamicFormBean.initialiseField("countryCode", "US");
			dynamicFormBean.initialiseField("resident", "yes");
			assertTrue(orCondition.evaluate(dynamicFormBean));
		}

		@Test
		void evaluatesToFalse() {
			dynamicFormBean.initialiseField("countryCode", "CA");
			dynamicFormBean.initialiseField("resident", "");
			assertFalse(orCondition.evaluate(dynamicFormBean));
		}
	}

	@Nested
	class ProfileAndForm {
		private ProfileResponse profileResponse;

		private DynamicFormBean dynamicFormBean;

		Condition orCondition;

		@BeforeEach
		void setup() {
			profileResponse = getProfile();
			dynamicFormBean = new DynamicFormBean();
			Condition hasScience = new HasServiceCondition("subscribedToHobby", 0, "hobby");
			Condition optIntoHobby = new FormFieldValueCondition("optIntoScience", 1, "science", "yes");
			orCondition = new OrCondition("optInHobby", 0, hasScience, optIntoHobby);
		}

		@Test
		void evaluatesToTrue() {
			dynamicFormBean.initialiseField("science", "yes");
			assertTrue(orCondition.evaluate(profileResponse, dynamicFormBean));
		}

		@Test
		void evaluatesToFalse() {
			dynamicFormBean.initialiseField("science", "");
			assertFalse(orCondition.evaluate(profileResponse, dynamicFormBean));
		}
	}


	private ProfileResponse getProfile() {
		return StubbedProfileRepository.search("kalmbachmedia", Map.of("email", "charlesb@yesmail.com"));
	}
}
