package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.DeserializeHelper;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.CreateProfileService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.createProfile", havingValue = "false", matchIfMissing = true)
public class CreateProfileServiceImpl implements CreateProfileService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public CreateProfileServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper createProfile(AppDetails appDetails, ProfileRequest profileRequest) {
		String uri = "profiles/create";
		String body;
		try {
			body = objectMapper.writeValueAsString(profileRequest.getProfile());
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating profile JSON", e);
		}
		String responseStr = requestManager.post(uri, appDetails, body);
		ProfileResponse profileResponse;
		Profile profile;
		ProfileResponseWrapper profileResponseWrapper;
		try {
			profile = DeserializeHelper.getProfile(objectMapper, responseStr);
			profileResponse = new ProfileResponse();
			profileResponse.setProfile(profile);
			profileResponseWrapper =  new ProfileResponseWrapper(profileRequest, profileResponse);
			profileResponseWrapper.setProfileSuccess();
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}

		return profileResponseWrapper;
	}
}
