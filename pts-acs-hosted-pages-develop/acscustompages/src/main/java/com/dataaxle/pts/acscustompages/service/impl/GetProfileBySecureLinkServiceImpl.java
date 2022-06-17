package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.ProfileResponseDeserializer;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileBySecureLinkService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileSecure", havingValue = "false", matchIfMissing = true)
public class GetProfileBySecureLinkServiceImpl implements GetProfileBySecureLinkService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public GetProfileBySecureLinkServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest, String view) {
		String lookupValue = getProfileRequest.getParameter(CustomPagesConstants.LOOKUP_PARAMETER);
		String securityValue = getProfileRequest.getParameter(CustomPagesConstants.SECURITY_PARAMETER);

		String uri = String.format("/profiles?l=%s&s=%s", lookupValue, securityValue);
		if (StringUtils.hasText(view)) {
			uri = String.format("%s&view=%s", uri, view);
		}
		try {
			String jsonStr = requestManager.get(uri, appDetails);
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addDeserializer(ProfileResponse.class, new ProfileResponseDeserializer(objectMapper));
			objectMapper.registerModule(simpleModule);
			ProfileResponse profileResponse = objectMapper.readValue(jsonStr, ProfileResponse.class);
			return new ProfileResponseWrapper(getProfileRequest, profileResponse);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		} catch (ServerErrorException e) {
			Throwable cause = e.getCause();
			if (cause instanceof HttpClientErrorException.BadRequest) {
				throw new ResourceNotFoundException(uri);
			}
			throw e;
		}
	}
}
