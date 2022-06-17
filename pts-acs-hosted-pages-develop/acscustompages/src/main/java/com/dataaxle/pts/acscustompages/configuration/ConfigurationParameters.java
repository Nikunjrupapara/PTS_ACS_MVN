package com.dataaxle.pts.acscustompages.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationParameters {

	private final String microservicesBaseUrl;

	private final String contextPath;

	private final String barcodeUrl;

	public ConfigurationParameters(@Value("${microservices.base-url}") String microservicesBaseUrl,
								   @Value("${server.servlet.context-path}") String contextPath,
								   @Value("${barcode.baseUrl}") String barcodeUrl) {
		this.microservicesBaseUrl = microservicesBaseUrl;
		this.contextPath = contextPath;
		this.barcodeUrl = barcodeUrl;
	}

	public String getMicroservicesBaseUrl() {
		return microservicesBaseUrl;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getBarcodeUrl() { return barcodeUrl; }
}
