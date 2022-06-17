package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;

public interface RequestManager {
	String get(String uri, AppDetails appDetails);

	String post(String uri, AppDetails appDetails, String body);

	String put(String uri, AppDetails appDetails, String body);
}
