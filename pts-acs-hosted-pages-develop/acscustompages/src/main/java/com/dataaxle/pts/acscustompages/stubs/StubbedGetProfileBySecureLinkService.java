package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.GetProfileBySecureLinkService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getProfileSecure", havingValue = "true")
public class StubbedGetProfileBySecureLinkService implements GetProfileBySecureLinkService {

	@Override
	public ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest, String view) {
		String lookupValue = getProfileRequest.getParameter(CustomPagesConstants.LOOKUP_PARAMETER);
		ProfileResponse profileResponse = StubbedProfileRepository.getByCustomerIdHash(appDetails.getCompany(), lookupValue);
		return new ProfileResponseWrapper(getProfileRequest, profileResponse);
	}
}
