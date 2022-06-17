package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;

public interface CustomResourceService {

	CustomResourceResponseWrapper<GetCustomResourceResponse> get(AppDetails appDetails, GetCustomResourceRequest request);

	CustomResourceResponseWrapper<GetCustomResourceResponse> create(AppDetails appDetails, CustomResourceRequest customResourceRequest);

	CustomResourceResponseWrapper<GetCustomResourceResponse> update(AppDetails appDetails, CustomResourceRequest customResourceRequest);

	// TODO: implement when required
	//void delete(AppDetails appDetails, CustomResourceRequest customResourceRequest);
}
