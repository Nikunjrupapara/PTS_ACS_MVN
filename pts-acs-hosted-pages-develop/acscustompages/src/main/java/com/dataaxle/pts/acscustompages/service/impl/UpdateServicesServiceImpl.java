package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.BadRequestException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.DeserializeHelper;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ServiceAction;
import com.dataaxle.pts.acscustompages.model.ServicesRequest;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.service.UpdateServicesService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.updateServices", havingValue = "false", matchIfMissing = true)
public class UpdateServicesServiceImpl implements UpdateServicesService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	@Autowired
	public UpdateServicesServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper updateServices(AppDetails appDetails, ProfileRequest profileRequest) {
		String uri = String.format("profiles/%s/services", profileRequest.getCustomerUniqueId());
		String body;
		try {
			ServicesRequest servicesRequest = profileRequest.getServicesRequest();
			body = objectMapper.writeValueAsString(servicesRequest);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating update services request JSON", e);
		}
		String responseStr;
		ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
		try {
			responseStr = requestManager.put(uri, appDetails, body);
		} catch (BadRequestException e) {
			profileResponseWrapper.setServicesError();
			profileResponseWrapper.setErrorMessage(e.getResponseMessage());
			return profileResponseWrapper;
		}
		ProfileResponse profileResponse;
		try {
			profileResponse = DeserializeHelper.getProfileResponse(objectMapper, responseStr);
			profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
			profileResponseWrapper.setProfileResponse(profileResponse);
			profileResponseWrapper.setServicesSuccess();
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
		return profileResponseWrapper;
	}
}
