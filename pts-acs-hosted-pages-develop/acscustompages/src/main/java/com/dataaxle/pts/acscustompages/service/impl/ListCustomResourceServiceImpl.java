package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.CustomResourceRecordDeserializerFactory;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.CustomResourceParameters;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.dataaxle.pts.acscustompages.model.CustomResourceResponseWrapper;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceRequest;
import com.dataaxle.pts.acscustompages.model.ListCustomResourceResponse;
import com.dataaxle.pts.acscustompages.service.ListCustomResourceService;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@ConditionalOnProperty(name = "microservices.useStubbedServices.listCustomResource", havingValue = "false", matchIfMissing = true)
@Slf4j
public class ListCustomResourceServiceImpl implements ListCustomResourceService {

	final RequestManager requestManager;

	final ObjectMapper objectMapper;

	public ListCustomResourceServiceImpl(RequestManager requestManager, ObjectMapper objectMapper) {
		this.requestManager = requestManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public CustomResourceResponseWrapper<ListCustomResourceResponse> listRecords(AppDetails appDetails,
																				 ListCustomResourceRequest request) {
		CustomResourceParameters customResourceParameters = request.getCustomResourceParameters(appDetails.getCompany());
		/*
		Fetch one page worth of data from the Custom Resource and return
		 */
		if (request.isUsePagination()) {
			String responseStr = submitRequest(request, appDetails);
			CustomResourceRecordCollection responseData = parseMicroservicesResponse(responseStr, objectMapper, customResourceParameters);
			return buildResponse(request, responseData);
		}

		/*
		Fetch all data matching the request parameters before returning
		 */
		boolean moreRecords = true;
		int pageNumber = 1;

		List<CustomResourceRecord> records = new ArrayList<>();
		while (moreRecords) {
			String jsonStr = submitRequest(request, appDetails);
			CustomResourceRecordCollection responseData = parseMicroservicesResponse(jsonStr, objectMapper, customResourceParameters);
			moreRecords = responseData.moreRecords;
			if (moreRecords) {
				records.addAll(responseData.data);
				pageNumber++;
				request = request.forPageNumber(pageNumber);
			}
		}
		return buildResponse(pageNumber, request, records);
	}

	private String submitRequest(ListCustomResourceRequest request, AppDetails appDetails) {
		String uri = request.buildUri();
		LOG.debug("GET {}", uri);
		return requestManager.get(uri, appDetails);
	}

	private CustomResourceRecordCollection parseMicroservicesResponse(String jsonStr, ObjectMapper objectMapper,
																	  CustomResourceParameters parameters) {
		try {
			ObjectMapper mapper = CustomResourceRecordDeserializerFactory.getMapper(objectMapper, parameters);
			JsonNode rootNode = mapper.readTree(jsonStr);
			int count = rootNode.get("count").asInt();
			int total = rootNode.get("total").asInt();
			boolean moreRecords = rootNode.get("moreRecords").asBoolean();
			List<CustomResourceRecord> records = new ArrayList<>();
			JsonNode recordsNode = rootNode.get("records");
			Iterator<JsonNode> recordsIterator = recordsNode.elements();
			while (recordsIterator.hasNext()) {
				String recordStr = recordsIterator.next().toString();
				CustomResourceRecord record = mapper.readValue(recordStr, CustomResourceRecord.class);
				records.add(record);
			}
			return new CustomResourceRecordCollection(moreRecords, count, total, records);
		} catch (JsonProcessingException e) {
			throw new ServerErrorException("Error parsing JSON response from microservices", e);
		}
	}

	private CustomResourceResponseWrapper<ListCustomResourceResponse> buildResponse(ListCustomResourceRequest request,
																					CustomResourceRecordCollection responseData) {
		ListCustomResourceResponse response = new ListCustomResourceResponse(request.getPageNumber(), responseData.getCount(),
			responseData.total, responseData.moreRecords, responseData.data);
		CustomResourceResponseWrapper<ListCustomResourceResponse> result = new CustomResourceResponseWrapper<>(request, response);
		result.setCustomResourceSuccess();
		return result;
	}

	private CustomResourceResponseWrapper<ListCustomResourceResponse> buildResponse(
		int pageNumber, ListCustomResourceRequest request, List<CustomResourceRecord> records) {
		ListCustomResourceResponse response = new ListCustomResourceResponse(pageNumber, records.size(), records.size(),
			false, records);
		CustomResourceResponseWrapper<ListCustomResourceResponse> result = new CustomResourceResponseWrapper<>(request, response);
		result.setCustomResourceSuccess();
		return result;
	}

	@Value
	@AllArgsConstructor
	public static class CustomResourceRecordCollection {
		boolean moreRecords;

		int count;

		int total;

		List<CustomResourceRecord> data;
	}
}
