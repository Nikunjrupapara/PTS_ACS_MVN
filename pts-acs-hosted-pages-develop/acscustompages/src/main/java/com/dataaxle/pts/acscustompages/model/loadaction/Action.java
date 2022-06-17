package com.dataaxle.pts.acscustompages.model.loadaction;

import java.util.Arrays;

public enum Action {
	GET_CUSTOM_RESOURCE(LoadActionConfig.ActionType.GET_CUSTOM_RESOURCE),
	LIST_CUSTOM_RESOURCE(LoadActionConfig.ActionType.LIST_CUSTOM_RESOURCE);

	private final LoadActionConfig.ActionType actionType;

	Action(LoadActionConfig.ActionType actionType) {
		this.actionType = actionType;
	}

	public static Action of(LoadActionConfig.ActionType actionType) {
		return Arrays.stream(values())
			.filter(val -> val.actionType == actionType)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException(String.
				format("No Action defined for LoadActionConfig.ActionType %s", actionType)));
	}
}
