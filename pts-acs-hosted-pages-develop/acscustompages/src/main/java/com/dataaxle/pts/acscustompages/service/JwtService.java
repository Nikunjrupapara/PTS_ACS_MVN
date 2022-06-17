package com.dataaxle.pts.acscustompages.service;

public interface JwtService {
	// TODO: This service is only required until the AppDetails API is completed.  It can be removed after that.
	String getToken(String company, String contextPath);
}
