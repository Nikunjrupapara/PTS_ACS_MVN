package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceParameters;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponse;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.GetCustomResourceByIdService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.getCustomResourceId", havingValue = "false", matchIfMissing = true)
public class GetCustomResourceByIdServiceImpl implements GetCustomResourceByIdService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public GetCustomResourceByIdServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		// Copy the provided ObjectMapper so that in process configuration changes do not affect the underlying object
		this.objectMapper = objectMapper.copy();
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> getCustomResource(AppDetails appDetails, GetCustomResourceRequest request) {
		String queryStr = request.getParameters().entrySet().stream()
							  .sorted(Map.Entry.comparingByKey())
							  .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
							  .collect(Collectors.joining("&"));

		String uri = String.format("customResources/%s/records/find?%s", request.getCustomResource(), queryStr);

		try {
			LOG.debug("GetCustomResourceById: uri: {}", uri);
			String jsonStr = requestManager.get(uri, appDetails);
			if (LOG.isDebugEnabled()) {
				JsonNode temp = objectMapper.readTree(jsonStr);
				LOG.debug("response: {}", temp.toPrettyString());
			}
			CustomResourceParameters parameters = request.getCustomResourceParameters(appDetails.getCompany());
			SimpleModule simpleModule = new SimpleModule();
			simpleModule.addDeserializer(CustomResourceRecord.class, new CustomResourceRecord.Deserializer(objectMapper,
				parameters));
			objectMapper.registerModule(simpleModule);
			CustomResourceRecord record = objectMapper.readValue(jsonStr, CustomResourceRecord.class);
			GetCustomResourceResponse response = new GetCustomResourceResponse(record);
			return new CustomResourceResponseWrapper<>(request, response);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
	}
}
