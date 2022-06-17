package com.dataaxle.pts.acscustompages.service.impl;

import com.dataaxle.pts.acscustompages.configuration.ConfigurationParameters;
import com.dataaxle.pts.acscustompages.exception.BadRequestException;
import com.dataaxle.pts.acscustompages.exception.ResourceNotFoundException;
import com.dataaxle.pts.acscustompages.exception.ServerErrorException;
import com.dataaxle.pts.acscustompages.json.deserialize.DeserializeHelper;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.service.RequestManager;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service("restTemplateRequestManager")
public class RestTemplateRequestManager implements RequestManager {

	private final RestTemplate restTemplate;

	private final ConfigurationParameters configurationParameters;

	@Autowired
	public RestTemplateRequestManager(RestTemplate restTemplate, ConfigurationParameters configurationParameters) {
		this.restTemplate = restTemplate;
		this.configurationParameters = configurationParameters;
	}

	@Override
	public String get(String uri, AppDetails appDetails) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add(CustomPagesConstants.SERVICES_TOKEN_HDR, appDetails.getJwt());
		addRequestIdHeader(headers);
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		String requestUrl = String.format("%s/%s", configurationParameters.getMicroservicesBaseUrl(), uri);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
			String responseBody = responseEntity.getBody();
			LOG.debug("Response: {}", responseBody);
			return responseBody;
		} catch (HttpClientErrorException.NotFound e) {
			throw new ResourceNotFoundException(uri);
		} catch (Exception e) {
			throw new ServerErrorException("Error in GET request", e);
		}
	}

	@Override
	public String post(String uri, AppDetails appDetails, String body) {
		return submit(uri, appDetails, body, HttpMethod.POST);
	}

	@Override
	public String put(String uri, AppDetails appDetails, String body) {
		return submit(uri, appDetails, body, HttpMethod.PUT);
	}

	private String submit(String uri, AppDetails appDetails, String body, HttpMethod httpMethod) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(CustomPagesConstants.SERVICES_TOKEN_HDR, appDetails.getJwt());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		addRequestIdHeader(headers);
		HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
		String requestUrl = String.format("%s/%s", configurationParameters.getMicroservicesBaseUrl(), uri);
		LOG.debug("{} to {} with body '{}'", httpMethod, requestUrl, body);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, httpMethod, httpEntity, String.class);
			String responseBody = responseEntity.getBody();
			LOG.debug("Response: {}", responseBody);
			return responseBody;
		} catch (HttpClientErrorException.NotFound e) {
			throw new ResourceNotFoundException(uri);
		} catch (HttpClientErrorException.BadRequest e) {
			String responseStr = e.getResponseBodyAsString();
			String responseMessage = DeserializeHelper.getBadRequestMessage(responseStr);
			throw new BadRequestException(responseMessage, e);
		} catch (Exception e) {
			throw new ServerErrorException(String.format("Error in %s request", httpMethod), e);
		}
	}

	private void addRequestIdHeader(HttpHeaders headers) {
		String requestId = MDC.get(CustomPagesConstants.MDC_REQUEST_ID);
		if (StringUtils.hasText(requestId)) {
			headers.set(CustomPagesConstants.REQUEST_ID_HEADER, requestId);
		}
	}
}
