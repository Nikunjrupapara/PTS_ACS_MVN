package com.yesmarketing.ptsacs.services.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.yesmarketing.acsapi.admin.deserializer.CustomResourceMetadataDeserializer;

import com.yesmarketing.acsapi.admin.model.CustomResourceMetadata;
import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.deserializer.CustomResourceRecordDeserializer;
import com.yesmarketing.acsapi.exception.AcsServerErrorException;
import com.yesmarketing.acsapi.exception.SagasRuntimeException;
import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordCollectionDto;
import com.yesmarketing.ptsacs.services.dto.UpdateResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
	Using for private utility/convenient functions so that it's easier to put together/follow
	logic in service layer.
*/
public abstract class CustomResourceServiceBase {
	protected static final Logger LOG = LoggerFactory.getLogger(CustomResourceServiceBase.class);
	SagasHelper sagasHelper;
	public CustomResourceServiceBase(SagasHelper sagasHelper){
		this.sagasHelper = sagasHelper;
	}

	protected String submitRequestToAdobe(String company, String method, HttpMethod httpMethod) throws HttpStatusCodeException {
		CredentialModel cred = sagasHelper.getCredential(company);
		RestTemplate restTemplate = sagasHelper.createAuthenticatedRestTemplate(cred);
		try {
			String url = sagasHelper.formUrl(cred.getACSBaseUrl(), method);
			if (httpMethod.equals(HttpMethod.GET)) {
				//return restTemplate.getForObject(url, clazz);
				return restTemplate.getForObject(url, String.class);
			} else if (httpMethod.equals(HttpMethod.DELETE)) {
				restTemplate.delete(url);
			}else{
				// throw unsupported http request
			}
			return null;
		} catch (HttpServerErrorException.InternalServerError e) {
			String errorResponse = e.getResponseBodyAsString();
			LOG.error("An error occurred calling Adobe's API.  Company: {}, uri: {}, Response: {}",
					company, method, errorResponse, e);
			throw new AcsServerErrorException(errorResponse, e);
		} catch (HttpStatusCodeException e) {
			//Adobe sometimes throw 500 errors. Rethrow it
			String errorResponse = e.getResponseBodyAsString();
			LOG.error("An error occurred calling Adobe's API.  Company: {}, uri: {}, Response: {}",
					company, method, errorResponse, e);
			throw e;
		} catch (Exception ex) {
			LOG.error("{}: (company={}, uri={}) ex={}", httpMethod, company, method, ex.getMessage(), ex);
			throw new SagasRuntimeException(ex);
		}

	}

	protected String submitRequestToAdobe(String company, String method, CustomResourceRecord recordData, HttpMethod httpMethod) throws HttpStatusCodeException {
		CredentialModel cred = sagasHelper.getCredential(company);
		RestTemplate restTemplate = sagasHelper.createAuthenticatedRestTemplate(cred);

		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
		headers.setContentType(mediaType);

		ResponseEntity<String> responseBody;
		//ResponseEntity<Object> responseBody;
		try {
			String url = sagasHelper.formUrl(cred.getACSBaseUrl(), method);
			HttpEntity<CustomResourceRecord> requestEntity = new HttpEntity<>(recordData,headers);
			responseBody = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
			return responseBody.getBody();

			//responseBody = restTemplate.exchange(url, httpMethod, requestEntity, clazz);
			//return responseBody.getBody();

		} catch (HttpServerErrorException.InternalServerError e) {
			String errorResponse = e.getResponseBodyAsString();
			LOG.error("An error occurred calling Adobe's API.  Company: {}, uri: {}, Response: {}",
					company, method, errorResponse, e);
			throw new AcsServerErrorException(errorResponse, e);
		} catch (HttpStatusCodeException e) {
			//Adobe sometimes throw 500 errors. Rethrow it
			String errorResponse = e.getResponseBodyAsString();
			LOG.error("An error occurred calling Adobe's API.  Company: {}, uri: {}, Response: {}",
					company, method, errorResponse, e);
			throw e;
		} catch (Exception ex) {
			LOG.error("{}: (company={}, uri={}) ex={}", httpMethod, company, method, ex.getMessage(), ex);
			throw new SagasRuntimeException(ex);
		}
	}

	protected UpdateResourceDto parseUpdateResourceDtoJson(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, UpdateResourceDto.class);
	}

	protected CustomResourceMetadata parseCustomResourceMetadataJson(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(CustomResourceMetadata.class, new CustomResourceMetadataDeserializer());
		mapper.registerModule(module);
		return mapper.readValue(json, CustomResourceMetadata.class);
	}

	protected List<CustomResourceRecord> parseCustomResourceRecordsJson(String recordsStr) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(recordsStr);
		String contentStr = rootNode.get("content").toString();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(CustomResourceRecord.class, new CustomResourceRecordDeserializer());
		mapper.registerModule(module);
		JavaType recordList = mapper.getTypeFactory().constructCollectionLikeType(List.class, CustomResourceRecord.class);
		return mapper.readValue(contentStr, recordList);
	}

	protected CustomResourceRecordCollectionDto parseListCustomResourceRecordsJson(ObjectMapper mapper, JsonNode rootNode) throws IOException {

		String contentStr = rootNode.get("content").toString();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(CustomResourceRecord.class, new CustomResourceRecordDeserializer());
		mapper.registerModule(module);
		JavaType recordList = mapper.getTypeFactory().constructCollectionLikeType(List.class, CustomResourceRecord.class);
		List<CustomResourceRecord> records = mapper.readValue(contentStr, recordList);
		return new CustomResourceRecordCollectionDto(records);
	}

	public CustomResourceRecord parseCustomResourceRecordJson(String recordStr) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(CustomResourceRecord.class, new CustomResourceRecordDeserializer());
		mapper.registerModule(module);
		return mapper.readValue(recordStr, CustomResourceRecord.class);
	}


	protected List<String> parseCustomResources(String recordsStr) throws IOException {
		List<String> list =  new LinkedList<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(recordsStr);

		Iterator<String> columnNames = rootNode.fieldNames();
		while (columnNames.hasNext()) {
			String name = columnNames.next();
			//String value = rootNode.get(name).asText();
			if(!name.equals("apiName")){
				list.add(name);
			}
		}
		return list;

	}

	protected String appendQueryParameters(String base, String customResourceName, String filter, Map<String, String> queryParameters) {
		if (queryParameters.isEmpty()) {
			return String.format("/%s/%s/%s", base, customResourceName,filter);
		}
		Map<String, String> remappedParameters = remapParameterNames(queryParameters);
		String parameterStr = remappedParameters
				.entrySet()
				.stream()
				.map(this::getParameterString)
				.collect(Collectors.joining("&"));
		
		return String.format("/%s/%s/%s?%s", base,customResourceName,filter, parameterStr);
	}

	private String getParameterString(Map.Entry<String, String> entry) { return String.format("%s=%s", entry.getKey(), entry.getValue()); }

	protected Map<String, String> remapParameterNames(Map<String, String> queryParameters){
		Map<String, String> remappedParameters = new HashMap<>();
		queryParameters.keySet().forEach(element-> {
			String parameterName = element;
			if (remapParameter(element)) {
				parameterName = String.format("%s_parameter",element);
			}
			remappedParameters.put(parameterName, queryParameters.get(element));
		});
		return remappedParameters;
	}

	private boolean remapParameter(String parameterName) {
		// Parameter names starting with underscore are ACS internal parameter names and should not be amended
		if (parameterName.startsWith("_")) return false;
		// acsId is the primary key for a custom resource and should not be amended
		if (parameterName.equals("acsId")) return false;
		// For other parameters, if the name ends with "_parameter" do nothing
		if (parameterName.endsWith("_parameter")) return false;
		// otherwise append "_parameter" to the name
		return true;
	}

}
