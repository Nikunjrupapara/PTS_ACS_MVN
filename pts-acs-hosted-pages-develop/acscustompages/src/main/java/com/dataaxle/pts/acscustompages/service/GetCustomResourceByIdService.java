package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;

public interface GetCustomResourceByIdService {
	CustomResourceResponseWrapper<GetCustomResourceResponse> getCustomResource(AppDetails appDetails, GetCustomResourceRequest request);
}
