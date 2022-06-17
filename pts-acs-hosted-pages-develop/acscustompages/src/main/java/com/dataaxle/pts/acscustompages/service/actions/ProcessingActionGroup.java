package com.dataaxle.pts.acscustompages.service.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ProcessingActionGroup {
	CUSTOM_RESOURCE(ProcessingAction.CREATE_CUSTOM_RESOURCE, ProcessingAction.DELETE_CUSTOM_RESOURCE,
		ProcessingAction.UPDATE_CUSTOM_RESOURCE),
	NO_ACTION(ProcessingAction.NO_ACTION),
	PROFILE(ProcessingAction.CREATE_PROFILE, ProcessingAction.UPDATE_PROFILE),
	SERVICE(ProcessingAction.SERVICE_ADD, ProcessingAction.SERVICE_REMOVE),
	TRIGGER_EMAIL(ProcessingAction.TRIGGER_EMAIL);

	private final List<ProcessingAction> actions = new ArrayList<>();

	private ProcessingActionGroup(ProcessingAction... actions) {
		this.actions.addAll(Arrays.asList(actions));
	}

	public List<ProcessingAction> getProcessingActions() {
		return actions;
	}
}
