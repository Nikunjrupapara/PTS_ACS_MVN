package com.dataaxle.pts.acscustompages.model;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class ServicesRequest {

	Map<String, ServiceAction> services = new HashMap<>();

	public void addServiceAction(String serviceName, ServiceAction serviceAction) {
		services.put(serviceName, serviceAction);
	}

	public void addServiceAction(Map<String, ServiceAction> serviceActions) {
		services.putAll(serviceActions);
	}

	public boolean isUpdateServices() {
		return !services.isEmpty();
	}
}
