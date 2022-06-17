package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;

public interface TriggerEmailService {

	ProfileResponseWrapper process(AppDetails appDetails, ProfileRequest profileRequest);
}
