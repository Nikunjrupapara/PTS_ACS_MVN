package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.ProfileResponseDeserializer;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileFilterService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileFilter", havingValue = "false", matchIfMissing = true)
public class GetProfileFilterServiceImpl implements GetProfileFilterService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public GetProfileFilterServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest) {
		ObjectNode searchFields = objectMapper.createObjectNode();
		getProfileRequest.getParameters().entrySet().stream()
			.map(this::remapParameter)
			.forEach(entry -> searchFields.put(entry.getKey(), entry.getValue()));
		ObjectNode searchDto = objectMapper.createObjectNode();
		searchDto.put("filterName", getProfileRequest.getFilterName());
		searchDto.set("searchFields", searchFields);

		String uri = "profiles/search?view=services";

		try {
			LOG.debug("GetProfileByFilter: uri: {}, payload: {}", uri, searchDto.toPrettyString());
			String jsonStr = requestManager.post(uri, appDetails, searchDto.toString());
			if (LOG.isDebugEnabled()) {
				JsonNode temp = objectMapper.readTree(jsonStr);
				LOG.debug("response: {}", temp.toPrettyString());
			}
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addDeserializer(ProfileResponse.class, new ProfileResponseDeserializer(objectMapper));
			objectMapper.registerModule(simpleModule);
			ProfileResponse profileResponse = objectMapper.readValue(jsonStr, ProfileResponse.class);
			return new ProfileResponseWrapper(getProfileRequest, profileResponse);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
	}

	private Map.Entry<String, String> remapParameter(Map.Entry<String, String> entry) {
		String key = String.format("%s_parameter", entry.getKey());
		return new AbstractMap.SimpleImmutableEntry<>(key, entry.getValue());
	}
}
