package com.dataaxle.pts.acscustompages.json.deserialize;

import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeserializeHelper {

	public static ProfileResponse getProfileResponse(ObjectMapper objectMapper, String jsonStr) throws JsonProcessingException {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(ProfileResponse.class, new ProfileResponseDeserializer(objectMapper));
		objectMapper.registerModule(simpleModule);
		return objectMapper.readValue(jsonStr, ProfileResponse.class);
	}

	public static Profile getProfile(ObjectMapper objectMapper, String profileJsonStr) throws JsonProcessingException {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Profile.class, new ProfileDeserializer(objectMapper));
		objectMapper.registerModule(simpleModule);
		return objectMapper.readValue(profileJsonStr, Profile.class);
	}

	public static String getBadRequestMessage(String responseStr) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode responseNode = objectMapper.readTree(responseStr);
			if (responseNode.has("message")) {
				return responseNode.get("message").asText();
			}
			return "No message provided";
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing Microservices Json", e);
			return String.format("Error parsing JSON: %s", responseStr);
		}
	}

}
