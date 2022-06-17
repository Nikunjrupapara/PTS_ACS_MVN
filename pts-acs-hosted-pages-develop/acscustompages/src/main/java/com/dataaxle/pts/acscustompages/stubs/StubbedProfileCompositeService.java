package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.ProfileCompositeService;
import com.dataaxle.pts.acscustompages.service.UpdateServicesService;
import com.dataaxle.pts.acscustompages.service.actions.ProcessingAction;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.profileComposite", havingValue = "true")
public class StubbedProfileCompositeService implements ProfileCompositeService {

	public static final String ERROR_EXIST_HASH = Hashing.sha256().hashString("error@yesmail.com", StandardCharsets.UTF_8).toString();

	private final ObjectMapper objectMapper;

	public StubbedProfileCompositeService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public ProfileResponseWrapper process(AppDetails appDetails, ProfileRequest profileRequest) {

		ProfileResponseWrapper profileResponseWrapper = new ProfileResponseWrapper(profileRequest);
		String customerUniqueId = profileRequest.getCustomerUniqueId();

		boolean simulateError = false;
		if (customerUniqueId.equals(ERROR_EXIST_HASH)) {
			simulateError = true;
		} else if (profileRequest.isUpdateProfile() && profileRequest.getProfile().getField("email").orElse("").equals("error.new@yesmail.com")) {
			simulateError = true;
		}
		if (simulateError) {
			profileResponseWrapper.setProfileError();
			profileResponseWrapper.setErrorMessage("An error occurred!");
			return profileResponseWrapper;
		}

		try {
			String jsonStr = objectMapper.writeValueAsString(profileRequest);
			LOG.debug("Payload: {}", jsonStr);

			ProfileResponse profileResponse = null;
			if (profileRequest.isUpdateProfile()) {
				ProcessingAction profileAction = profileRequest.isNewProfile()
													 ? ProcessingAction.CREATE_PROFILE
													 : ProcessingAction.UPDATE_PROFILE;
				try {
					profileResponse = StubbedProfileRepository.createOrUpdateProfile(appDetails.getCompany(),
						profileRequest.getCustomerUniqueId(), profileRequest.getProfile().getFields());
					customerUniqueId = (String) profileResponse.getProfile().getField("cusCustomerIdHash").orElse("");

					if(StringUtils.isEmpty(profileRequest.getCustomerUniqueId())){
						ProfileRequest newProfileRequest =  new ProfileRequest(customerUniqueId);
						profileRequest.getServicesRequest().getServices().forEach(newProfileRequest::addServiceAction);
						profileRequest.getCustomResourceRequests().getCustomResourceRequests().values().forEach(reqs ->
							reqs.forEach(newProfileRequest::addCustomResourceRequest));
						newProfileRequest.setTriggeredEventId(profileRequest.getTriggeredEventId());
						newProfileRequest.setTriggeredEventExpiration(profileRequest.getTriggerEmailData().getExpiration());
						newProfileRequest.setTriggeredEventRecipient(profileRequest.getTriggerEmailData().getEmail());
						newProfileRequest.setTriggeredEventScheduled(profileRequest.getTriggerEmailData().getScheduled());
						profileRequest = newProfileRequest;
					}
					profileResponseWrapper.setProfileResponse(profileResponse);
					profileResponseWrapper.setProfileSuccess();
				} catch (Exception e) {
					LOG.error("Profile Update error", e);
					profileResponseWrapper.setProfileError();
				}
			} else {
				profileResponse = StubbedProfileRepository.getByCustomerIdHash(appDetails.getCompany(), profileRequest.getCustomerUniqueId());
			}

			if (profileRequest.isUpdateServices()) {
				ProfileResponseWrapper servicesResponse;
				UpdateServicesService svc = new StubbedUpdateServicesService();
				try {

					servicesResponse = svc.updateServices(appDetails, profileRequest);

					if (servicesResponse.getProfileRequestResult().isServicesSuccess()) {
						profileResponseWrapper.setServicesSuccess();
					}
				} catch (Exception e) {
					LOG.error("Services Update error", e);
					profileResponseWrapper.setServicesError();
				}
			}

			if (profileRequest.isCustomResourcesRequest()) {
				profileRequest.getCustomResourceRequests().getCustomResourceRequests().forEach((res, reqs) -> {
					reqs.forEach(req -> LOG.info("Create customResourceRecord: {}, data: {}", res, req));
				});
				profileResponseWrapper.setCustomResourcesSuccess();
			}

			if (profileRequest.isTriggerEmail()) {
				LOG.info("Triggering email {} with context: {}", profileRequest.getTriggeredEventId(),
					profileRequest.getTriggeredEventContext());
				profileResponseWrapper.setEmailSuccess();
			}

			profileResponseWrapper.setProfileResponse(profileResponse);

			return profileResponseWrapper;
		} catch (JsonProcessingException e) {
			LOG.error("JSON writing error!", e);
			throw new ServerErrorException("JSON writing error", e);
		}
	}
}
