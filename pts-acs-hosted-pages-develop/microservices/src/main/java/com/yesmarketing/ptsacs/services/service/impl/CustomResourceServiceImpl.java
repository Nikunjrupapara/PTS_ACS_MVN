package com.yesmarketing.ptsacs.services.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.admin.model.CustomResourceMetadata;
import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.acsapi.exception.SagasRuntimeException;
import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.ptsacs.common.exception.BadRequestException;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordCollectionDto;
import com.yesmarketing.ptsacs.services.dto.UpdateResourceDto;
import com.yesmarketing.ptsacs.services.service.CustomResourceListService;
import com.yesmarketing.ptsacs.services.service.CustomResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomResourceServiceImpl extends CustomResourceServiceBase implements CustomResourceService {

	private final CustomResourceListService customResourceListService;

	@Autowired
	public CustomResourceServiceImpl(SagasHelper sagasHelper,
									 CustomResourceListService customResourceListService){
		super(sagasHelper);
		this.customResourceListService = customResourceListService;
	}

	public CustomResourceMetadata getMetadata(String company, String dataSet) {
		String metadataStr = getMetadataString(company, dataSet);
		try{
			return parseCustomResourceMetadataJson(metadataStr);
		}catch(IOException e){
			throw new ServerErrorException(String.format("issue with ACS call: %s",e.getMessage()), e);
		}
	}

	public String getMetadataString(String company, String dataSet) {
		String uri = String.format("/%s/%s/metadata",getBase(company,dataSet), dataSet);
		return submitRequestToAdobe(company, uri, HttpMethod.GET);
	}

	public String getCount(String company, String resource){
		String uri = String.format("/%s/%s/_count",getBase(company,resource), resource);
		return submitRequestToAdobe(company, uri, HttpMethod.GET);
	}

	public CustomResourceRecordCollectionDto getRecords(String company, String resource, String filter, Map<String, String> queryParameters){
		if (!queryParameters.containsKey(LINE_START_PARAM)) {
			queryParameters.put(LINE_START_PARAM, DEFAULT_LINE_START);
		}
		if (!queryParameters.containsKey(LINE_COUNT_PARAM)) {
			queryParameters.put(LINE_COUNT_PARAM, DEFAULT_LINE_COUNT);
		}
		queryParameters.put(FORCE_PAGINATION_PARAM, FORCE_PAGINATION_VALUE);
		String uri = appendQueryParameters(getBase(company,resource),resource,filter,queryParameters);
		String recordsStr = submitRequestToAdobe(company, uri, HttpMethod.GET);
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(recordsStr);
			CustomResourceRecordCollectionDto result = parseListCustomResourceRecordsJson(mapper, rootNode);
			result.setCount(result.getRecords().size());
			boolean moreRecords = rootNode.has("next");
			result.setMoreRecords(moreRecords);
			processCountNode(company, mapper, rootNode, result);
			return result;
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}

	public CustomResourceRecord getRecord(String company, String resource, String pKey){
		String uri = String.format("/%s/%s/%s",getBase(company,resource), resource,pKey);
		String recordsStr = submitRequestToAdobe(company, uri, HttpMethod.GET);
		try {
			return parseCustomResourceRecordJson(recordsStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}

	public CustomResourceRecord createRecord(String company, String resource, CustomResourceRecord recordData){
		String uri = String.format("/%s/%s",getBase(company,resource), resource);
		String recordsStr = submitRequestToAdobe(company, uri,recordData, HttpMethod.POST);
		try {
			return parseCustomResourceRecordJson(recordsStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}
	public UpdateResourceDto updateRecord(String company, String resource, String pKey, CustomResourceRecord recordData){
		String uri = String.format("/%s/%s/%s",getBase(company,resource), resource,pKey);
		String response = submitRequestToAdobe(company, uri,recordData, HttpMethod.PATCH);
		try {
			return parseUpdateResourceDtoJson(response);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}
	public void deleteRecord(String company, String resource, String pKey){
		String uri = String.format("/%s/%s/%s",getBase(company,resource), resource,pKey);
		submitRequestToAdobe(company, uri, HttpMethod.DELETE);
	}





	//// Get record by ACS Id
	public CustomResourceRecord getRecordByAcsId(String company, String resourceName, String acsId){
		String ACS_FILTER = "byAcsId";
		Map<String,String> queryParameters = new HashMap<>();
		queryParameters.put("acsId",acsId);
		List<CustomResourceRecord> records = searchRecords(company,resourceName,ACS_FILTER,queryParameters);

		if(CollectionUtils.isEmpty(records)){
			//throw new ResourceNotFoundException(resourceName,ACS_FILTER,acsId);
			throw new ObjectNotFoundException("Requested resource could not be found.",String.format("resource: %s, id: %s",resourceName,acsId));
		}

		return records.get(0); // assume only 1 record
	}

	//// Get record by business key
	public CustomResourceRecord getRecordByBusinessKey(String company, String resourceName, Map<String, String> queryParameters){
		String ACS_FILTER = "byIdentificationkey"; //"byIdentificationKey"

		List<CustomResourceRecord> records = searchRecords(company,resourceName,ACS_FILTER,queryParameters);

		if(CollectionUtils.isEmpty(records)){
//			throw new ResourceNotFoundException(resourceName,ACS_FILTER,queryParameters.entrySet()
//					.stream()
//					.map(entry -> entry.getKey() + " - " + entry.getValue())
//					.collect(Collectors.joining(", ")));

			throw new ObjectNotFoundException("Requested resource could not be found.",String.format("resource: %s, id: %s",resourceName,queryParameters.entrySet()
					.stream()
					.map(entry -> entry.getKey() + " - " + entry.getValue())
					.collect(Collectors.joining(", "))));

		}

		return records.get(0); // assume only 1 record
	}


	public List<CustomResourceRecord> searchRecords(String company, String resourceName, String filter, Map<String, String> queryParameters){
		String uri = appendQueryParameters(getBase(company,resourceName),resourceName,filter,queryParameters);
		String recordsStr = submitRequestToAdobe(company, uri, HttpMethod.GET);
		try {
			return parseCustomResourceRecordsJson(recordsStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}


	public CustomResourceRecord updateRecordByAcsId(String company, String resourceName, String acsId, CustomResourceRecord recordData){
		CustomResourceRecord customResourceRecord = getRecordByAcsId(company, resourceName, acsId);
		String pKey = (String)customResourceRecord.getValue("PKey");
		UpdateResourceDto updateResourceDto = updateRecord(company,resourceName,pKey,recordData);
		/*
		 	filter is likely slower, so using pkey instead of below
		 	return getRecordByAcsId(company, resourceName, acsId);
		*/
		return getRecord(company, resourceName, updateResourceDto.getPKey());

	}

	public void deleteRecordByAcsId(String company, String resourceName, String acsId){
		CustomResourceRecord customResourceRecord = getRecordByAcsId(company, resourceName, acsId);
		String pKey = (String)customResourceRecord.getValue("PKey");
		deleteRecord(company,resourceName,pKey);
	}


	public void validateCreateRecord(String company, String resourceName, CustomResourceRecord customResourceRecord){
		if(CollectionUtils.isEmpty(customResourceRecord.getValues())){
				throw new BadRequestException("CustomResourceRecord  is empty");
		}
	}

	public void validateUpdateRecordByAcsId(String company,String resourceName, String acsId, CustomResourceRecord customResourceRecord){

		if(StringUtils.isEmpty(acsId)){
			throw new BadRequestException("acsId  is empty");
		}
		if(CollectionUtils.isEmpty(customResourceRecord.getValues())){
			throw new BadRequestException("CustomResourceRecord  is empty");
		}
	}

	public void validateDeleteRecordByAcsId(String company, String resourceName, String acsId){
		if(StringUtils.isEmpty(acsId)){
			throw new BadRequestException("acsId  is empty");
		}
	}

	public List<String> getAssociatedList(String company){
		return customResourceListService.getAssociatedList(company);
	}

	public List<String> getUnassociatedList(String company){
		return customResourceListService.getUnassociatedList(company);
	}

	public CustomResourceRecord parseCustomResourceRecordJson(String recordStr) {
		try {
			return super.parseCustomResourceRecordJson(recordStr);
		}catch(IOException e){
			throw new ServerErrorException("issue with parseCustomResourceRecordJson: "+e.getMessage(), e);
		}
	}

	private String getBase(String company, String resource){
		String base = "profileAndServicesExt";
		if(customResourceListService.getUnassociatedList(company).contains(resource)){base = "customResources"; }
		return base;
	}

	private void processCountNode(String company, ObjectMapper mapper, JsonNode rootNode, CustomResourceRecordCollectionDto result)
		throws JsonProcessingException {
		JsonNode countNode = rootNode.get("count");
		String countUrl = countNode.get("href").asText();
		String uri = Arrays.asList(countUrl.split("campaign")).get(1);
		String countResult = submitRequestToAdobe(company, uri, HttpMethod.GET);
		JsonNode countResultNode = mapper.readTree(countResult);
		result.setTotal(countResultNode.get("count").asInt());
	}


}
