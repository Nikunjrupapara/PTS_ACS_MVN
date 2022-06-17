package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;

import javax.servlet.http.HttpServletRequest;

public interface CustomPagesRequestService {

	CustomPagesRequest parseRequest(HttpServletRequest request);
}
