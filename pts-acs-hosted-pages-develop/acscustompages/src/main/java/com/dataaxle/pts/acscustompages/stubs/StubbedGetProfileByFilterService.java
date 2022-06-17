package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileFilterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileFilter", havingValue = "true")
public class StubbedGetProfileByFilterService implements GetProfileFilterService {

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest) {
		ProfileResponse profileResponse = StubbedProfileRepository.find(appDetails.getCompany(), getProfileRequest.getParameters());
		return new ProfileResponseWrapper(getProfileRequest, profileResponse);
	}
}
