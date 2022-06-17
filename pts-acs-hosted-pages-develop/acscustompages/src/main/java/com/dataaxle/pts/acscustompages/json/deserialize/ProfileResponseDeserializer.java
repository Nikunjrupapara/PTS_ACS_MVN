package com.dataaxle.pts.acscustompages.json.deserialize;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.CurrentService;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProfileResponseDeserializer extends JsonDeserializer<ProfileResponse> {

	private static final String PROFILE_NODE = "profile";

	private static final String CURRENT_SERVICES_NODE = "currentServices";

	private static final String CUSTOM_FIELDS_NODE = "customFields";

	private final ObjectMapper objectMapper;

	public ProfileResponseDeserializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
		throws IOException, JsonProcessingException {
		JsonNode rootNode = objectMapper.readTree(jsonParser);
		return parseJson(rootNode);
	}

	private ProfileResponse parseJson(JsonNode rootNode) throws JsonProcessingException {
		ProfileResponse profileResponse = new ProfileResponse();
		if (rootNode.has(PROFILE_NODE)) {
			profileResponse.setProfile(parseProfileNode(rootNode.get(PROFILE_NODE)));
		}
		if (rootNode.has(CURRENT_SERVICES_NODE)) {
			profileResponse.addCurrentServices(parseCurrentServicesNode(rootNode.get(CURRENT_SERVICES_NODE)));
		}
		return profileResponse;
	}

	private Profile parseProfileNode(JsonNode profileNode) throws JsonProcessingException {
		return DeserializeHelper.getProfile(objectMapper, profileNode.toString());
	}

	private List<CurrentService> parseCurrentServicesNode(JsonNode currentServicesNode) {
		LOG.debug("currentServicesNode: {}", currentServicesNode.toPrettyString());
		List<CurrentService> currentServices = new ArrayList<>();
		try {
			for (JsonNode currentService : currentServicesNode) {
				currentServices.add(objectMapper.readValue(currentService.toString(), CurrentService.class));
			}
			return currentServices;
		} catch (IOException e) {
			throw new ServerErrorException("Error parsing profileResponse.currentServices", e);
		}
	}
}
