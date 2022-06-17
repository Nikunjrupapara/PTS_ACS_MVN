package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.DeserializeHelper;
import com.dataaxle.pts.acscustompages.json.deserialize.ProfileResponseDeserializer;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.profileComposite", havingValue = "false", matchIfMissing = true)
public class ProfileCompositeServiceImpl implements ProfileCompositeService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public ProfileCompositeServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper process(AppDetails appDetails, ProfileRequest profileRequest) {
		String uri = "composite";
		String profileUniqueId = profileRequest.getCustomerUniqueId();
		if (StringUtils.hasText(profileUniqueId)) {
			uri = String.format("%s/%s", uri, profileUniqueId);
		}
		String body;
		try {
			body = objectMapper.writeValueAsString(profileRequest);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating composite request JSON", e);
		}
		String responseStr = requestManager.post(uri, appDetails, body);
		ProfileResponseWrapper profileResponseWrapper;
		try {
			profileResponseWrapper = parseResponse(profileRequest, responseStr);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
		return profileResponseWrapper;
	}

	private ProfileResponseWrapper parseResponse(ProfileRequest profileRequest, String responseStr) throws JsonProcessingException {
		JsonNode rootNode = objectMapper.readTree(responseStr);
		ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
		setFlag(rootNode, "profileSuccess", profileResponseWrapper);
		setFlag(rootNode,"servicesSuccess", profileResponseWrapper);
		setFlag(rootNode, "customResourcesSuccess", profileResponseWrapper);
		setFlag(rootNode, "emailsSuccess", profileResponseWrapper);
		if (rootNode.hasNonNull("errorMessage")) {
			String errorMessage = rootNode.get("errorMessage").asText();
			if (StringUtils.hasText(errorMessage)) {
				profileResponseWrapper.setErrorMessage(errorMessage);
			}
		}
		if (rootNode.has("profile")) {
			String profileJson = rootNode.get("profile").toString();
			ProfileResponse profileResponse = DeserializeHelper.getProfileResponse(objectMapper, profileJson);
			profileResponseWrapper.setProfileResponse(profileResponse);
		}
		return profileResponseWrapper;
	}

	void setFlag(JsonNode jsonNode, String name, ProfileResponseWrapper profileResponseWrapper) {
		boolean flag = false;
		if (jsonNode.has(name)) {
			flag = jsonNode.get(name).asBoolean();
		}
		if (flag) {
			if ("profileSuccess".equals(name)) {
				profileResponseWrapper.setProfileSuccess();
			}
			if ("servicesSuccess".equals(name)) {
				profileResponseWrapper.setServicesSuccess();
			}
			if ("customResourcesSuccess".equals(name)) {
				profileResponseWrapper.setCustomResourcesSuccess();
			}
			if ("emailsSuccess".equals(name)) {
				profileResponseWrapper.setEmailSuccess();
			}
		}
	}
}
