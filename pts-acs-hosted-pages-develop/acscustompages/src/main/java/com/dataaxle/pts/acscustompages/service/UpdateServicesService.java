package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.actions.ProfileRequest;

import java.util.Map;

public interface UpdateServicesService {

	ProfileResponseWrapper updateServices(AppDetails appDetails, ProfileRequest profileRequest);
}
