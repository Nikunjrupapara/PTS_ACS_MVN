package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.TriggerEmailService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.dataaxle.pts.acscustompages.service.actions.TriggerEmailData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.triggerEmail", havingValue = "true")
public class StubbedEmailService implements TriggerEmailService {
	@Override
	public ProfileResponseWrapper process(AppDetails appDetails, ProfileRequest profileRequest) {
		TriggerEmailData triggerEmailData = profileRequest.getTriggerEmailData();
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.setSerial
		try {
			String jsonStr = objectMapper.writeValueAsString(triggerEmailData);
			LOG.debug("Payload: {}", jsonStr);
			ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
			profileResponseWrapper.setEmailSuccess();
			return profileResponseWrapper;
		} catch (JsonProcessingException e) {
			LOG.error("JSON writing error!", e);
			throw new ServerErrorException("JSON writing error", e);
		}
	}
}
