package com.dataaxle.pts.acscustompages.service.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.model.ServiceAction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TestConditionEvaluation {

	@Disabled("This is no longer required since conditional processing was refactored")
	@ParameterizedTest
	@MethodSource("generateData")
	void evaluateActions(ProfileRequest profileRequest, List<ProcessingAction> expectedActions,
						 List<ProcessingAction> conditions, boolean expectedConditionMatch) {
		List<ProcessingAction> actions = profileRequest.getProcessingActions();
		assertIterableEquals(expectedActions, actions);
		//assertEquals(expectedConditionMatch, ActionProcessor.evaluateConditions(conditions, actions));
	}

	private static Stream<Arguments> generateData() {
		return Stream.of(
			create_profile_only(),
			create_profile_add_services(),
			remove_service_only(),
			add_service_only(),
			update_profile_only()
		);
	}

	static Arguments create_profile_only() {
		ProfileRequest profileRequest = new ProfileRequest("");
		profileRequest.addProfileField("email", "charlesb@yesmail.com");
		profileRequest.addProfileField("firstName", "Charles");
		return arguments(profileRequest, Collections.singletonList(ProcessingAction.CREATE_PROFILE),
			Collections.emptyList(), true);
	}

	static Arguments create_profile_add_services() {
		ProfileRequest profileRequest = new ProfileRequest("");
		profileRequest.addProfileField("email", "charlesb@yesmail.com");
		profileRequest.addProfileField("firstName", "Charles");
		profileRequest.addServiceAction("marketing", ServiceAction.ADD);
		profileRequest.addServiceAction("service", ServiceAction.ADD);
		return arguments(profileRequest, Arrays.asList(ProcessingAction.CREATE_PROFILE, ProcessingAction.SERVICE_ADD),
			Arrays.asList(ProcessingAction.UPDATE_PROFILE, ProcessingAction.SERVICE_REMOVE), false);
	}

	static Arguments remove_service_only() {
		ProfileRequest profileRequest = new ProfileRequest("cccb443a638e1da77a3281cc39c9345b02602af90cc4b57f07e9994a54fa340600b6e10b002feb0aedfac3458568a458d7ccb0f251bebd867fae6d760cb95db7");
		profileRequest.addServiceAction("marketing", ServiceAction.REMOVE);
		return arguments(profileRequest, Collections.singletonList(ProcessingAction.SERVICE_REMOVE),
			Collections.singletonList(ProcessingAction.SERVICE_ADD), false);
	}

	static Arguments add_service_only() {
		ProfileRequest profileRequest = new ProfileRequest("cccb443a638e1da77a3281cc39c9345b02602af90cc4b57f07e9994a54fa340600b6e10b002feb0aedfac3458568a458d7ccb0f251bebd867fae6d760cb95db7");
		profileRequest.addServiceAction("marketing", ServiceAction.ADD);
		return arguments(profileRequest, Collections.singletonList(ProcessingAction.SERVICE_ADD),
			Collections.singletonList(ProcessingAction.SERVICE_ADD), true);
	}

	static Arguments update_profile_only() {
		ProfileRequest profileRequest = new ProfileRequest("cccb443a638e1da77a3281cc39c9345b02602af90cc4b57f07e9994a54fa340600b6e10b002feb0aedfac3458568a458d7ccb0f251bebd867fae6d760cb95db7");
		profileRequest.addProfileField("cusUnsubReason", "1");
		profileRequest.addProfileField("cusUnsubReasonDesc", "Too many emails");
		profileRequest.addProfileField("cusUnsubReasonDate", LocalDateTime.now().toString());
		return arguments(profileRequest, Collections.singletonList(ProcessingAction.UPDATE_PROFILE),
			Collections.singletonList(ProcessingAction.CREATE_PROFILE), false);
	}
}
