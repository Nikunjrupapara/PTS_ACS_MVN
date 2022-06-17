package com.dataaxle.pts.acscustompages.model;

import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class CustomResourceRequests {

	Map<String, List<CustomResourceRequest>> customResourceRequests = new HashMap<>();

	public boolean isCustomResourcesRequest() {
		return customResourceRequests.size() > 0;
	}

	public void addCustomResourceRequest(CustomResourceRequest customResourceRequest) {
		String name = customResourceRequest.getResourceName();
		if (!customResourceRequests.containsKey(name)) {
			customResourceRequests.put(name, new ArrayList<>());
		}
		customResourceRequests.get(name).add(customResourceRequest);
	}

}
