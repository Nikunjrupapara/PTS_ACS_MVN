package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileCustomerIdService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileCustomerId", havingValue = "true")
public class StubbedGetProfileByCustomerIdService implements GetProfileCustomerIdService {
	@Override
	public ProfileResponse getProfile(AppDetails appDetails, Map<String, String> searchFields) {
		return StubbedProfileRepository.search(appDetails.getCompany(), searchFields);
	}

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest) {
		ProfileResponse profileResponse = StubbedProfileRepository.search(appDetails.getCompany(), getProfileRequest.getParameters());
		return new ProfileResponseWrapper(getProfileRequest, profileResponse);
	}
}
