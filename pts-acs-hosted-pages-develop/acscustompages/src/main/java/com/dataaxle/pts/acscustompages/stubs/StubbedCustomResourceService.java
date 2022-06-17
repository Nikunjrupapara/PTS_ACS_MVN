package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequestResult;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "microservices.useStubbedServices.customResource", havingValue = "true")
public class StubbedCustomResourceService implements CustomResourceService {

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> get(AppDetails appDetails, GetCustomResourceRequest request) {
		String recordId = String.join("|", request.getParameters().values());
		try {
			GetCustomResourceResponse response = StubbedCustomResourceRepository.getRecordById(request.getCustomResource(), recordId);
			return new CustomResourceResponseWrapper<>(request, response);
		} catch (ResourceNotFoundException e) {
			return new CustomResourceResponseWrapper<>(request, true, false);
		}
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> create(AppDetails appDetails, CustomResourceRequest customResourceRequest) {
		CustomResourceRecord record = customResourceRequest.getRecords().get(0);
		GetCustomResourceResponse customResourceResponse = StubbedCustomResourceRepository.create(appDetails.getCompany(),
			customResourceRequest.getResourceName(), (String)record.getValue(CustomPagesConstants.ACS_ID).orElse(""),
			record);
		CustomResourceResponseWrapper<GetCustomResourceResponse> wrapper = new CustomResourceResponseWrapper<>(customResourceRequest, customResourceResponse);
		wrapper.setCustomResourceSuccess();
		return wrapper;
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> update(AppDetails appDetails, CustomResourceRequest customResourceRequest) {
		CustomResourceRecord record = customResourceRequest.getRecords().get(0);
		GetCustomResourceResponse customResourceResponse = StubbedCustomResourceRepository.update(appDetails.getCompany(),
			customResourceRequest.getResourceName(), (String)record.getValue(CustomPagesConstants.ACS_ID).orElse(""),
			record);
		CustomResourceResponseWrapper<GetCustomResourceResponse> wrapper = new CustomResourceResponseWrapper<>(customResourceRequest, customResourceResponse);
		wrapper.setCustomResourceSuccess();
		return wrapper;
	}
}
