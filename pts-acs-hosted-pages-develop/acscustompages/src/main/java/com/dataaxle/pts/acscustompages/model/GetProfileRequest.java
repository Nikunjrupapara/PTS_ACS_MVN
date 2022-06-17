package com.dataaxle.pts.acscustompages.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
@AllArgsConstructor
public class GetProfileRequest {

	String filterName;

	Map<String, String> parameters = new HashMap<>();

	public GetProfileRequest() {
		this.filterName = "";
	}

	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}

	public String getParameter(String name) {
		return parameters.getOrDefault(name, "");
	}
}
