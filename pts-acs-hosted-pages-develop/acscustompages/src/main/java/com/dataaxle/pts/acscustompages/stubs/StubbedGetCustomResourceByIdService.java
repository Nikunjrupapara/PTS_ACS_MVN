package com.dataaxle.pts.acscustompages.stubs;

import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getCustomResourceId", havingValue = "true")
public class StubbedGetCustomResourceByIdService implements GetCustomResourceByIdService {

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> getCustomResource(AppDetails appDetails, GetCustomResourceRequest request) {
		String recordId = request.getParameters()
							  .entrySet()
							  .stream()
							  .sorted(Map.Entry.comparingByKey())
							  .map(Map.Entry::getValue)
							  .collect(Collectors.joining("|"));

		return new CustomResourceResponseWrapper<GetCustomResourceResponse>(request,
			StubbedCustomResourceRepository.getRecordById(request.getCustomResource(), recordId));
	}
}
