package com.dataaxle.pts.acscustompages.service.actions;

public enum ProcessingAction {
	CREATE_CUSTOM_RESOURCE(ProcessingActionGroup.CUSTOM_RESOURCE),
	DELETE_CUSTOM_RESOURCE(ProcessingActionGroup.CUSTOM_RESOURCE),
	UPDATE_CUSTOM_RESOURCE(ProcessingActionGroup.CUSTOM_RESOURCE),
	CREATE_PROFILE(ProcessingActionGroup.PROFILE),
	UPDATE_PROFILE(ProcessingActionGroup.PROFILE),
	SERVICE_ADD(ProcessingActionGroup.SERVICE),
	SERVICE_REMOVE(ProcessingActionGroup.SERVICE),
	TRIGGER_EMAIL(ProcessingActionGroup.TRIGGER_EMAIL),
	NO_ACTION(ProcessingActionGroup.NO_ACTION);

	private final ProcessingActionGroup processingActionGroup;

	private ProcessingAction(ProcessingActionGroup processingActionGroup) {
		this.processingActionGroup = processingActionGroup;
	}

	public ProcessingActionGroup getProcessingActionGroup() {
		return processingActionGroup;
	};
}
