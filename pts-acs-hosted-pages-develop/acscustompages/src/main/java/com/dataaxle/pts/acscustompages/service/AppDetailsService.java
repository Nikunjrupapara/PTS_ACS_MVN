package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;

import javax.servlet.http.HttpServletRequest;

public interface AppDetailsService {

	AppDetails getAppDetails(String company, String contextPath);
}
