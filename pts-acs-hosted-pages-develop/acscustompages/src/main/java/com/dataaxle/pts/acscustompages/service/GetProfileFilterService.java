package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.GetProfileRequest;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;

public interface GetProfileFilterService {

	ProfileResponseWrapper getProfile(AppDetails appDetails, GetProfileRequest getProfileRequest);
}
