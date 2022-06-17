package com.dataaxle.pts.acscustompages.json.serializer;

import com.dataaxle.pts.acscustompages.configuration.JacksonConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public abstract class BaseSerializerTest {

	protected static final String ACS_DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z";
	protected ObjectMapper objectMapper;

	protected String jsonStr;

	protected void initialize() {
		objectMapper = new JacksonConfiguration().objectMapper();
	}

	protected Object getDocument(String json) {
		return Configuration.defaultConfiguration().jsonProvider().parse(json);
	}

	protected Object getJsonPath(Object document, String jsonPath) {
		return JsonPath.read(document, jsonPath);
	}

	protected boolean pathExists(Object document, String jsonPath) {
		try {
			JsonPath.read(document, jsonPath);
			return true;
		} catch (PathNotFoundException e) {
			return false;
		}
	}
}
