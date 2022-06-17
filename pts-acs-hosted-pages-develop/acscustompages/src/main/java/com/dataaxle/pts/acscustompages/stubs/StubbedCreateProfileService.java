package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.CreateProfileService;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(value = "microservices.useStubbedServices.createProfile", havingValue = "true")
@Service
public class StubbedCreateProfileService implements CreateProfileService {
	@Override
	public ProfileResponseWrapper createProfile(AppDetails appDetails, ProfileRequest profileRequest) {
		ProfileResponse profileResponse = StubbedProfileRepository.createProfile(appDetails.getCompany(), profileRequest.getProfile().getFields());
		ProfileResponseWrapper profileResponseWrapper =  new ProfileResponseWrapper(profileRequest, profileResponse);
		profileResponseWrapper.setProfileSuccess();
		return profileResponseWrapper;
	}
}
