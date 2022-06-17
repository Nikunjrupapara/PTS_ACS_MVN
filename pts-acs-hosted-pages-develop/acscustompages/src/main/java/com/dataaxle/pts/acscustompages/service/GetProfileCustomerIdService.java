package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;

import java.util.Map;

public interface GetProfileCustomerIdService {
	@Deprecated
	ProfileResponse getProfile(AppDetails appDetails, Map<String, String> searchFields);

	ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest);
}
