package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.ProfileResponseDeserializer;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileCustomerIdService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileCustomerId", havingValue = "false", matchIfMissing = true)
public class GetProfileCustomerIdServiceImpl implements GetProfileCustomerIdService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public GetProfileCustomerIdServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponse getProfile(AppDetails appDetails, Map<String, String> searchFields) {
		return null;
	}

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest) {
		ObjectNode searchFields = objectMapper.createObjectNode();
		getProfileRequest.getParameters().forEach(searchFields::put);
		ObjectNode searchDto = objectMapper.createObjectNode();
		searchDto.set("searchFields", searchFields);

		String uri = "profiles/customer?view=services";

		try {
			LOG.debug("GetProfileByCustomerId: uri: {}, payload: {}", uri, searchDto.toPrettyString());
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
}
