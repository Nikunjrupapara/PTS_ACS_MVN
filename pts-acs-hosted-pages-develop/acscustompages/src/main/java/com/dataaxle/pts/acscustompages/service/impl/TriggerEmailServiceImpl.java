package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.model.TriggerEmailResponse;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.service.TriggerEmailService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.triggerEmail", havingValue = "false", matchIfMissing = true)
public class TriggerEmailServiceImpl  implements TriggerEmailService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public TriggerEmailServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper process(AppDetails appDetails, ProfileRequest profileRequest) {
		String uri = "email/send";
		String body;
		try {
			body = objectMapper.writeValueAsString(profileRequest.getTriggerEmailData());
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating Triggered Email JSON", e);
		}
		String responseStr = requestManager.post(uri, appDetails, body);
		TriggerEmailResponse triggerEmailResponse;
		try {
			triggerEmailResponse = objectMapper.readValue(responseStr, TriggerEmailResponse.class);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
		ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest, triggerEmailResponse);
		return profileResponseWrapper;
	}
}
