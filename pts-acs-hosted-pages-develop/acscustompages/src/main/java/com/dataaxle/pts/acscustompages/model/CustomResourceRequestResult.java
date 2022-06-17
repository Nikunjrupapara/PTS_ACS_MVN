package com.dataaxle.pts.acscustompages.model;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_ID;

import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@ToString
public class CustomResourceRequestResult implements RequestResult {

	private static final long serialVersionUID = -795169502348146342L;

	final RequestType requestType = RequestType.CUSTOM_RESOURCE;

	final boolean resourceLookup;

	final boolean resourceList;

	final boolean resourceFound;

	final boolean resourceLookupError;

	final Map<String, String> searchFields;

	final boolean recordIncluded;

	final ProcessingAction processingAction;

	boolean success = false;

	String errorMessage = "";

	public CustomResourceRequestResult(GetCustomResourceRequest getCustomResourceRequest) {
		this.resourceLookup = false;
		this.resourceList = false;
		this.resourceFound = false;
		this.resourceLookupError = false;
		this.searchFields = getCustomResourceRequest.getParameters();
		this.recordIncluded = false;
		this.processingAction = null;
	}

	public CustomResourceRequestResult(GetCustomResourceRequest getCustomResourceRequest, boolean lookup, boolean found) {
		this.resourceLookup = lookup;
		this.resourceFound = found;
		this.resourceList = false;
		this.resourceLookupError = false;
		this.searchFields = getCustomResourceRequest.getParameters();
		this.recordIncluded = false;
		this.processingAction = null;
	}

	public CustomResourceRequestResult(GetCustomResourceRequest getCustomResourceRequest, boolean lookup, boolean found,
									   boolean error) {
		this.resourceLookup = lookup;
		this.resourceFound = found;
		this.resourceList = false;
		this.resourceLookupError = error;
		this.searchFields = getCustomResourceRequest.getParameters();
		this.recordIncluded = false;
		this.processingAction = null;
	}

	public CustomResourceRequestResult(CustomResourceRequest customResourceRequest) {
		this.resourceLookup = false;
		this.resourceList = false;
		this.resourceFound = false;
		this.resourceLookupError = false;
		this.searchFields = Collections.emptyMap();
		this.recordIncluded = customResourceRequest.getRecords().size() > 0;
		// This may need refining in the event of a request with multiple records or for a request to delete a record
		String acsId = (String)customResourceRequest.getRecords().get(0).getValue(ACS_ID).orElse("");
		this.processingAction = acsId.length() > 0 ? ProcessingAction.UPDATE_CUSTOM_RESOURCE : ProcessingAction.CREATE_CUSTOM_RESOURCE;
	}

	public CustomResourceRequestResult(ListCustomResourceRequest request) {
		this.resourceLookup = false;
		this.resourceList = true;
		this.resourceFound = false;
		this.resourceLookupError = false;
		this.searchFields = request.getParameters();
		this.recordIncluded = false;
		this.processingAction = null;
	}

	@Override
	public RequestType getRequestType() {
		return requestType;
	}

	@Override
	public boolean isCreate() {
		return processingAction == ProcessingAction.CREATE_CUSTOM_RESOURCE;
	}

	@Override
	public boolean isUpdateProfile() {
		return false;
	}

	@Override
	public boolean isUpdate() {
		return processingAction == ProcessingAction.UPDATE_CUSTOM_RESOURCE;
	}

	@Override
	public boolean isAllSuccess() {
		return isCustomResourcesSuccess();
	}

	@Override
	public boolean isLookup() {
		return resourceLookup;
	}

	@Override
	public boolean isList() { return resourceList; }

	@Override
	public boolean isFound() {
		return resourceFound;
	}

	@Override
	public boolean isLookupError() {
		return resourceLookupError;
	}

	@Override
	public Map<String, String> getSearchFields() {
		return searchFields;
	}

	@Override
	public ProcessingAction getProcessingAction() {
		return processingAction;
	}

	@Override
	public String getServiceAction(String serviceName) {
		return "";
	}

	@Override
	public boolean isProfileIncluded() {
		return false;
	}

	@Override
	public boolean isProfileSuccess() {
		return false;
	}

	@Override
	public boolean isServicesIncluded() {
		return false;
	}

	@Override
	public boolean isServicesSuccess() {
		return false;
	}

	@Override
	public Map<String, ServiceAction> getServiceActions() {
		return Collections.emptyMap();
	}

	@Override
	public boolean isCustomResourcesIncluded() {
		return recordIncluded;
	}

	@Override
	public boolean isCustomResourcesSuccess() {
		return success;
	}

	@Override
	public boolean isEmailTriggered() {
		return false;
	}

	@Override
	public boolean isEmailSuccess() {
		return false;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void setProfileSuccess(boolean profileSuccess) {
		// do nothing
	}

	@Override
	public void setServicesSuccess(boolean servicesSuccess) {
		// do nothing
	}

	@Override
	public void setCustomResourcesSuccess(boolean customResourcesSuccess) {
		this.success = customResourcesSuccess;
	}

	@Override
	public void setEmailSuccess(boolean emailSuccess) {
		// do nothing
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		// do nothing
	}

	@Deprecated
	@Override
	public boolean isCreateProfile() {
		return false;
	}

	@Deprecated
	@Override
	public boolean isProfileLookup() {
		return false;
	}

	@Deprecated
	@Override
	public boolean isProfileFound() {
		return false;
	}

	@Deprecated
	@Override
	public boolean isProfileLookupError() {
		return false;
	}

	@Deprecated
	@Override
	public ProcessingAction getProfileAction() {
		return null;
	}
}
