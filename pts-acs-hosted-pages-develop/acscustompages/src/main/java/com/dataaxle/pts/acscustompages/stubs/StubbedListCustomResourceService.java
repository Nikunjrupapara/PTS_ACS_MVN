package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.ListCustomResourceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.listCustomResource", havingValue = "true")
public class StubbedListCustomResourceService implements ListCustomResourceService {
	@Override
	public CustomResourceResponseWrapper<ListCustomResourceResponse> listRecords(AppDetails appDetails, ListCustomResourceRequest request) {
		ListCustomResourceResponse response = StubbedCustomResourceRepository.listRecords(request);
		CustomResourceResponseWrapper<ListCustomResourceResponse> wrapper = new CustomResourceResponseWrapper<>(request, response);
		wrapper.setCustomResourceSuccess();
		return wrapper;
	}
}
