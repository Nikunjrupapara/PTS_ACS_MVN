package com.dataaxle.pts.acscustompages.service;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;

public interface ListCustomResourceService {
	CustomResourceResponseWrapper<ListCustomResourceResponse> listRecords(AppDetails appDetails, ListCustomResourceRequest request);
}
