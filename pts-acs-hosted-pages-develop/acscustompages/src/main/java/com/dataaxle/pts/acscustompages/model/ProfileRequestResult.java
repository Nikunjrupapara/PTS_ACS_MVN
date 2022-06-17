package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class ProfileRequestResult implements RequestResult {

	private static final long serialVersionUID = -5628245768261285510L;

	final RequestType requestType = RequestType.PROFILE;

	final boolean profileLookup;

	final boolean profileFound;

	final boolean profileLookupError;

	final Map<String, String> searchFields;

	final boolean profileIncluded;

	final ProcessingAction profileAction;

	boolean profileSuccess = false;

	final boolean servicesIncluded;

	boolean servicesSuccess = false;

	final Map<String, ServiceAction> serviceActions = new HashMap<>();

	final boolean customResourcesIncluded;

	boolean customResourcesSuccess = false;

	final boolean emailTriggered;

	boolean emailSuccess = false;

	String errorMessage = "";

	public ProfileRequestResult(ProfileRequest profileRequest) {
		this.profileLookup = false;
		this.profileFound = false;
		this.profileLookupError = false;
		this.searchFields = new HashMap<>();
		this.profileIncluded = profileRequest.isUpdateProfile();
		if (profileIncluded) {
			this.profileAction = profileRequest.isNewProfile()
									 ? ProcessingAction.CREATE_PROFILE : ProcessingAction.UPDATE_PROFILE;
		} else {
			this.profileAction = null;
		}
		this.servicesIncluded = profileRequest.isUpdateServices();
		if (servicesIncluded) {
			serviceActions.putAll(profileRequest.getServicesRequest().getServices());
		}
		this.customResourcesIncluded = profileRequest.isCustomResourcesRequest();
		this.emailTriggered = profileRequest.isTriggerEmail();
	}

	public ProfileRequestResult(GetProfileRequest getProfileRequest, boolean profileLookup, boolean profileFound) {
		this.profileLookup = profileLookup;
		this.profileFound = profileFound;
		this.searchFields = getProfileRequest.getParameters();
		this.profileLookupError = false;
		this.profileIncluded = false;
		this.profileAction = null;
		this.servicesIncluded = false;
		this.customResourcesIncluded = false;
		this.emailTriggered = false;
	}

	public ProfileRequestResult(GetProfileRequest getProfileRequest, boolean profileLookup, boolean profileFound, boolean profileLookupError) {
		this.profileLookup = profileLookup;
		this.profileFound = profileFound;
		this.profileLookupError = profileLookupError;
		this.searchFields = getProfileRequest.getParameters();
		this.profileIncluded = false;
		this.profileAction = null;
		this.servicesIncluded = false;
		this.customResourcesIncluded = false;
		this.emailTriggered = false;
	}

	@Override
	public String getServiceAction(String serviceName) {
		if (serviceActions.containsKey(serviceName)) {
			return serviceActions.get(serviceName).name();
		}
		return "";
	}

	@Deprecated
	@Override
	public boolean isCreateProfile() {
		return isCreate();
	}

	@Deprecated
	@Override
	public boolean isProfileLookup() {
		return isLookup();
	}

	@Deprecated
	@Override
	public boolean isProfileFound() {
		return isFound();
	}

	@Deprecated
	@Override
	public boolean isProfileLookupError() {
		return isLookupError();
	}

	@Override
	public boolean isProfileIncluded() {
		return profileIncluded;
	}

	@Deprecated
	@Override
	public ProcessingAction getProfileAction() {
		return getProcessingAction();
	}

	@Override
	public RequestType getRequestType() {
		return requestType;
	}

	@Override
	public boolean isCreate() {
		if (profileIncluded) {
			return profileAction == ProcessingAction.CREATE_PROFILE;
		}
		return false;
	}

	@Override
	public boolean isUpdateProfile() {
		if (profileIncluded) {
			return profileAction == ProcessingAction.UPDATE_PROFILE;
		}
		return false;
	}

	@Override
	public boolean isAllSuccess() {
		List<Boolean> results = new ArrayList<>();
		if (profileIncluded) { results.add(profileSuccess); }
		if (servicesIncluded) { results.add(servicesSuccess); }
		if (customResourcesIncluded) { results.add(customResourcesSuccess); }
		if (emailTriggered) { results.add(emailSuccess); }
		return results.stream().reduce(Boolean::logicalAnd).orElse(false);
	}

	@Override
	public boolean isLookup() {
		return profileLookup;
	}

	@Override
	public boolean isFound() {
		return profileFound;
	}

	@Override
	public boolean isLookupError() {
		return profileLookupError;
	}

	@Override
	public Map<String, String> getSearchFields() {
		return searchFields;
	}

	@Override
	public ProcessingAction getProcessingAction() {
		return profileAction;
	}

	@Override
	public boolean isProfileSuccess() {
		return profileSuccess;
	}

	@Override
	public boolean isServicesIncluded() {
		return servicesIncluded;
	}

	@Override
	public boolean isServicesSuccess() {
		return servicesSuccess;
	}

	@Override
	public Map<String, ServiceAction> getServiceActions() {
		return serviceActions;
	}

	@Override
	public boolean isCustomResourcesIncluded() {
		return customResourcesIncluded;
	}

	@Override
	public boolean isCustomResourcesSuccess() {
		return customResourcesSuccess;
	}

	@Override
	public boolean isEmailTriggered() {
		return emailTriggered;
	}

	@Override
	public boolean isEmailSuccess() {
		return emailSuccess;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void setProfileSuccess(boolean profileSuccess) {
		this.profileSuccess = profileSuccess;
	}

	@Override
	public void setServicesSuccess(boolean servicesSuccess) {
		this.servicesSuccess = servicesSuccess;
	}

	@Override
	public void setCustomResourcesSuccess(boolean customResourcesSuccess) {
		this.customResourcesSuccess = customResourcesSuccess;
	}

	@Override
	public void setEmailSuccess(boolean emailSuccess) {
		this.emailSuccess = emailSuccess;
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public boolean isUpdate() {
		return profileIncluded || servicesIncluded || customResourcesIncluded || emailTriggered;
	}
}
