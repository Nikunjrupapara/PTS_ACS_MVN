package com.dataaxle.pts.acscustompages.service.impl;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.ACS_ID;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.PKEY;
import static com.google.common.base.Preconditions.checkState;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.CustomResourceRecordDeserializerFactory;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceParameters;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.GetCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@ConditionalOnProperty(value = "microservices.useStubbedServices.customResource", havingValue = "false", matchIfMissing = true)
@Slf4j
public class CustomResourceServiceImpl implements CustomResourceService {

	private final RequestManager requestManager;

	private final ObjectMapper objectMapper;

	public CustomResourceServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> get(AppDetails appDetails, GetCustomResourceRequest request) {
		String uri = request.buildUri();
		LOG.debug("GetCustomResource: uri: {}", uri);
		try {
			String jsonStr = requestManager.get(uri, appDetails);
			if (LOG.isDebugEnabled()) {
				JsonNode temp = objectMapper.readTree(jsonStr);
				LOG.debug("response: {}", temp.toPrettyString());
			}
			// Response could be a single record, or an array, depending on which method is called.  Either way it should
			// only consist of a single record.
			JsonNode rootNode = objectMapper.readTree(jsonStr);
			if (rootNode.isArray()) {
				if (rootNode.isEmpty()) {
					jsonStr = "{}";
				} else {
					JsonNode recordNode = rootNode.get(0);
					jsonStr = recordNode.toString();
				}
			}
			CustomResourceParameters parameters = request.getCustomResourceParameters(appDetails.getCompany());
			ObjectMapper mapper = CustomResourceRecordDeserializerFactory.getMapper(objectMapper, parameters);
			CustomResourceRecord record = mapper.readValue(jsonStr, CustomResourceRecord.class);
			GetCustomResourceResponse response = new GetCustomResourceResponse(record);
			CustomResourceResponseWrapper<GetCustomResourceResponse> wrapper = new CustomResourceResponseWrapper<>(request, response);
			wrapper.setCustomResourceSuccess();
			return wrapper;
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> create(AppDetails appDetails, CustomResourceRequest customResourceRequest) {
		String resourceName = customResourceRequest.getResourceName();
		checkState(StringUtils.hasText(resourceName), "Resource name not provided");
		checkState(customResourceRequest.getRecords().size() == 1, "Only 1 record can be created in a request");
		CustomResourceRecord record = customResourceRequest.getRecords().get(0);
		checkState(!record.hasField(ACS_ID), "ACS Id cannot be included in a create request");
		checkState(!record.hasField(PKEY), "PKey cannot be included in a create request");
		String uri = String.format("customResources/%s", resourceName);
		String body;
		try {
			body = objectMapper.writeValueAsString(record);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating request body", e);
		}
		String jsonStr = requestManager.post(uri, appDetails, body);
		try {
			if (LOG.isDebugEnabled()) {
				JsonNode temp = objectMapper.readTree(jsonStr);
				LOG.debug("response: {}", temp.toPrettyString());
			}
			CustomResourceParameters parameters = customResourceRequest.getCustomResourceParameters(appDetails.getCompany());
			ObjectMapper mapper = CustomResourceRecordDeserializerFactory.getMapper(objectMapper, parameters);
			CustomResourceRecord updated = mapper.readValue(jsonStr, CustomResourceRecord.class);
			GetCustomResourceResponse response = new GetCustomResourceResponse(updated);
			CustomResourceResponseWrapper<GetCustomResourceResponse> responseWrapper =
				new CustomResourceResponseWrapper<>(customResourceRequest, response);
			responseWrapper.setCustomResourceSuccess();
			return responseWrapper;
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from Microservices API", e);
		}
	}

	@Override
	public CustomResourceResponseWrapper<GetCustomResourceResponse> update(AppDetails appDetails, CustomResourceRequest customResourceRequest) {
		String resourceName = customResourceRequest.getResourceName();
		checkState(StringUtils.hasText(resourceName), "Resource name not provided");
		checkState(customResourceRequest.getRecords().size() == 1, "Only 1 record can be updated in a request");
		CustomResourceRecord record = customResourceRequest.getRecords().get(0);
		String acsId = (String)record.getValue(ACS_ID).orElse("");
		checkState(StringUtils.hasText(acsId), "The acsId value must be provided in the request");
		String uri = String.format("customResources/%s/records/id/%s", resourceName, acsId);
		// remove the ACS Id & PKey values from the record so they aren't included in the payload.
		// We copy the record and remove them from the copy as the values in the original request are used in the
		// CustomResourceRequestResult to determine if the request is a create or update.
		CustomResourceRecord forUpdatePayload = CustomResourceRecord.copy(record);
		forUpdatePayload.getValues().remove(ACS_ID);
		forUpdatePayload.getValues().remove(PKEY);
		String body;
		try {
			body = objectMapper.writeValueAsString(forUpdatePayload);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error creating customResourceRequest body", e);
		}
		String jsonStr = requestManager.put(uri, appDetails, body);
		try {
			if (LOG.isDebugEnabled()) {
				JsonNode temp = objectMapper.readTree(jsonStr);
				LOG.debug("response: {}", temp.toPrettyString());
			}
			CustomResourceParameters parameters = customResourceRequest.getCustomResourceParameters(appDetails.getCompany());
			ObjectMapper mapper = CustomResourceRecordDeserializerFactory.getMapper(objectMapper, parameters);
			CustomResourceRecord updated = mapper.readValue(jsonStr, CustomResourceRecord.class);
			GetCustomResourceResponse response = new GetCustomResourceResponse(updated);
			CustomResourceResponseWrapper<GetCustomResourceResponse> responseWrapper =
				new CustomResourceResponseWrapper<>(customResourceRequest, response);
			responseWrapper.setCustomResourceSuccess();
			return responseWrapper;
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from Microservices API", e);
		}
	}
}
