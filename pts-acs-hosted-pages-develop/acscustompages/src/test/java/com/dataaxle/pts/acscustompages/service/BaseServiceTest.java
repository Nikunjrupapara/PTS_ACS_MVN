package com.dataaxle.pts.acscustompages.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.configuration.ConfigurationParameters;
import com.dataaxle.pts.acscustompages.configuration.JacksonConfiguration;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.ProfileResponseWrapper;
import com.dataaxle.pts.acscustompages.service.impl.RestTemplateRequestManager;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class BaseServiceTest {

	protected static final String ACS_BASE_URL = "http://localhost:%d/v1/services";

	protected WireMockServer wireMockServer;

	protected JacksonConfiguration jacksonConfiguration = new JacksonConfiguration();

	protected String wireMockUrl;

	protected RestTemplateRequestManager restTemplateRequestManager;

	protected String url;

	protected Map<String, Object> headers;

	protected CustomPagesRequest customPagesRequest;

	protected AppDetails appDetails;

	protected ProfileResponseWrapper profileResponseWrapper;

	@BeforeAll
	protected static void loggingSetup() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Map<String, String> loggerLevels = Map.of(
			"com.github.tomakehurst.wiremock", "INFO",
			"com.dataaxle.pts.acscustompages", "DEBUG",
			"wiremock.org", "ERROR",
			"WireMock", "WARN",
			"org.eclipse.jetty", "WARN",
			"root", "INFO"
			);
		loggerLevels.forEach((loggerName, level) -> {
			Logger logger = loggerContext.getLogger(loggerName);
			logger.setLevel(Level.toLevel(level));
		});
	}

	protected void globalSetup() throws Exception {
		wireMockServer = new WireMockServer(
			options()
				.dynamicPort()
				.usingFilesUnderClasspath("src/test/resources/wiremock")
				.notifier(new ConsoleNotifier(false))
		);
		wireMockServer.start();

		wireMockUrl = String.format(ACS_BASE_URL, wireMockServer.port());

		restTemplateRequestManager = new RestTemplateRequestManager(new RestTemplate(),
			new ConfigurationParameters(wireMockUrl, "", ""));

		headers = new HashMap<>();
	}

	@AfterEach
	protected void tearDown() {
		wireMockServer.stop();
	}
}
