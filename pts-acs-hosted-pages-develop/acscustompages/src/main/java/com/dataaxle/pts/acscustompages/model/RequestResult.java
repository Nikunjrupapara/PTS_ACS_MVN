package com.dataaxle.pts.acscustompages.model;

import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;

import java.io.Serializable;
import java.util.Map;

public interface RequestResult extends Serializable {

	RequestType getRequestType();

	boolean isCreate();

	boolean isUpdateProfile();

	boolean isUpdate();

	boolean isAllSuccess();

	boolean isLookup();

	default boolean isList() { return false; }

	boolean isFound();

	boolean isLookupError();

	Map<String, String> getSearchFields();

	ProcessingAction getProcessingAction();

	String getServiceAction(String serviceName);

	boolean isProfileIncluded();

	boolean isProfileSuccess();

	boolean isServicesIncluded();

	boolean isServicesSuccess();

	Map<String, ServiceAction> getServiceActions();

	boolean isCustomResourcesIncluded();

	boolean isCustomResourcesSuccess();

	boolean isEmailTriggered();

	boolean isEmailSuccess();

	String getErrorMessage();

	void setProfileSuccess(boolean profileSuccess);

	void setServicesSuccess(boolean servicesSuccess);

	void setCustomResourcesSuccess(boolean customResourcesSuccess);

	void setEmailSuccess(boolean emailSuccess);

	void setErrorMessage(String errorMessage);

	// The methods below are retained from the ProfileRequestResult class for backwards compatibility.  However, they are
	// all deprecated and should be replaced with the generic equivalent (above) at some point.

	@Deprecated // use isCreate() instead
	boolean isCreateProfile();

	@Deprecated // use isLookup() instead
	boolean isProfileLookup();

	@Deprecated // use isFound() instead
	boolean isProfileFound();

	@Deprecated // use isLookupError() instead
	boolean isProfileLookupError();

	@Deprecated // use getProcessingAction() instead
	ProcessingAction getProfileAction();
}
