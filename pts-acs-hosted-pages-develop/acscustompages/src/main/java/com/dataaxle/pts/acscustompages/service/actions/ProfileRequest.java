package com.dataaxle.pts.acscustompages.service.actions;

import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequests;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.ServicesRequest;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Value
public class ProfileRequest {

	String customerUniqueId;

	Profile profile = new Profile();

	ServicesRequest servicesRequest = new ServicesRequest();

	TriggerEmailData triggerEmailData = new TriggerEmailData();

	CustomResourceRequests customResourceRequests = new CustomResourceRequests();

	public ProfileRequest(String customerUniqueId) {
		this.customerUniqueId = customerUniqueId;
	}

	public void addProfileField(String name, Object value) {
		profile.addField(name, value);
	}

	public boolean hasActions() {
		List<Boolean> actions = Arrays.asList(isUpdateProfile(), isUpdateServices(), isCustomResourcesRequest(), isTriggerEmail());
		return actions.stream().reduce(Boolean::logicalOr).orElse(false);
	}

	public boolean isUpdateProfile() {
		return !profile.getFields().isEmpty();
	}

	public boolean isNewProfile() {
		return StringUtils.isEmpty(customerUniqueId);
	}

	public void addServiceAction(String serviceName, ServiceAction serviceAction) {
		servicesRequest.addServiceAction(serviceName, serviceAction);
	}

	public void addServiceAction(Map<String, ServiceAction> serviceActions) {
		servicesRequest.addServiceAction(serviceActions);
	}

	public boolean isUpdateServices() {
		return servicesRequest.isUpdateServices();
	}

	public void setTriggeredEventId(String eventId) {
		triggerEmailData.setEventId(eventId);
	}

	public void setTriggeredEventRecipient(String email) {
		triggerEmailData.setEmail(email);
	}

	public void setTriggeredEventExpiration(LocalDateTime expiration) {
		triggerEmailData.setExpiration(expiration);
	}

	public void setTriggeredEventScheduled(LocalDateTime scheduled) {
		triggerEmailData.setScheduled(scheduled);
	}

	public void addPersonalizationField(String name, Object value) {
		triggerEmailData.addContextField(name, value);
	}

	public boolean isTriggerEmail() {
		return triggerEmailData.getEventId() != null;
	}

	public String getTriggeredEventId() {
		return triggerEmailData.getEventId();
	}

	public Map<String, Object> getTriggeredEventContext() {
		return triggerEmailData.getContext();
	}

	public boolean isCustomResourcesRequest() {
		return customResourceRequests.isCustomResourcesRequest();
	}

	public void addCustomResourceRequest(CustomResourceRequest customResourceRequest) {
		customResourceRequests.addCustomResourceRequest(customResourceRequest);
	}

	public List<ProcessingAction> getProcessingActions() {
		List<ProcessingAction> actions = new ArrayList<>();
		if (isNewProfile()) {
			actions.add(ProcessingAction.CREATE_PROFILE);
		} else {
			if (isUpdateProfile()) {
				actions.add(ProcessingAction.UPDATE_PROFILE);
			}
		}
		boolean addService = servicesRequest.getServices().values().stream().anyMatch(action -> action.equals(ServiceAction.ADD));
		if (addService) {
			actions.add(ProcessingAction.SERVICE_ADD);
		}
		boolean removeService = servicesRequest.getServices().values().stream().anyMatch(action -> action.equals(ServiceAction.REMOVE));
		if (removeService) {
			actions.add(ProcessingAction.SERVICE_REMOVE);
		}
		return actions;
	}
}
