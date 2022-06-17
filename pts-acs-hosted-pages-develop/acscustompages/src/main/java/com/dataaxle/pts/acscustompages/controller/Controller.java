package com.dataaxle.pts.acscustompages.controller;

public enum Controller {

	NONE(ControllerUtils.NO_PATH),
	CREATE_CUSTOM_RESOURCE(ControllerUtils.CREATE_CUSTOM_RESOURCE_PATH),
	CREATE_PROFILE(ControllerUtils.CREATE_PROFILE_PATH),
	CONFIRMATION_PAGE(ControllerUtils.CONFIRMATION_PAGE_PATH),
	CUSTOM_RESOURCE_CONFIRMATION_PAGE(ControllerUtils.CUSTOM_RESOURCE_CONFIRMATION_PATH),
	ERROR_PAGE(ControllerUtils.ERROR_PAGE_PATH),
	GET_CUSTOM_RESOURCE_BY_ID(ControllerUtils.GET_CUSTOM_RESOURCE_BY_ID_PATH),
	GET_CUSTOM_RESOURCE_BY_LINK(ControllerUtils.GET_CUSTOM_RESOURCE_BY_LINK_PATH),
	GET_PROFILE_CUSTOMERID(ControllerUtils.GET_PROFILE_CUSTOMERID_PATH),
	GET_PROFILE_FILTER(ControllerUtils.GET_PROFILE_FILTER_PATH),
	GET_PROFILE_SECURELINK(ControllerUtils.GET_PROFILE_SECURELINK_PATH),
	LIST_CUSTOM_RESOURCE(ControllerUtils.LIST_CUSTOM_RESOURCE_PATH),
	POST_PROFILE(ControllerUtils.POST_PROFILE_PATH),
	PROFILE_CONFIRM(ControllerUtils.PROFILE_CONFIRM_PATH),
	TRIGGER_EMAIL(ControllerUtils.TRIGGER_EMAIL_PATH),
	UPDATE_PROFILE(ControllerUtils.UPDATE_PROFILE_PATH),
	UPDATE_SERVICES(ControllerUtils.UPDATE_SERVICES_PATH),
	GENERIC_PAGE(ControllerUtils.GENERIC_PAGE_PATH);

	private final String path;

	Controller(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
