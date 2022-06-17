package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.Profile;
import com.dataaxle.pts.acscustompages.model.ProfileResponse;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;

public interface CreateProfileService {
	ProfileResponseWrapper createProfile(AppDetails appDetails, ProfileRequest profileRequest);
}
