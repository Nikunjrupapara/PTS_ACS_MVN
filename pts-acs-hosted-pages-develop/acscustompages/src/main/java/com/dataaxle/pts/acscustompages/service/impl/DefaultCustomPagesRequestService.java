package com.dataaxle.pts.acscustompages.service.impl;

import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.FORWARDED_FOR_HDR;
import static com.dataaxle.pts.acscustompages.utils.CustomPagesConstants.RECAPTCHA_RESPONSE_NM;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.configuration.ConfigurationParameters;
import com.dataaxle.pts.acscustompages.model.AppDetails;
import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.service.AppDetailsService;
import com.dataaxle.pts.acscustompages.service.CustomPagesRequestService;
import com.dataaxle.pts.acscustompages.service.DomainService;
import com.dataaxle.pts.acscustompages.utils.CustomPagesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class DefaultCustomPagesRequestService implements CustomPagesRequestService {

	// URL Regex taken from here: https://tools.ietf.org/html/rfc3986#appendix-B
	private static final String URL_REGEX = "^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?";

	private static final int DOMAIN_GROUP = 4;

	private static final int PATH_GROUP = 5;

	private final DomainService domainService;

	private final AppDetailsService appDetailsService;

	private final ConfigurationParameters configurationParameters;

	public DefaultCustomPagesRequestService(DomainService domainService, AppDetailsService appDetailsService, ConfigurationParameters configurationParameters) {
		this.domainService = domainService;
		this.appDetailsService = appDetailsService;
		this.configurationParameters = configurationParameters;
	}

	@Override
	public CustomPagesRequest parseRequest(HttpServletRequest request) {
		String requestUri = request.getRequestURL().toString();
		String queryStr = request.getQueryString();
		if (StringUtils.hasText(queryStr)) {
			if (!requestUri.contains("?")) {
				requestUri = String.format("%s?%s", requestUri, queryStr);
			}
		}
		Matcher matcher = parseUri(requestUri);
		String domainStr;
		String forwardedFrom = request.getHeader(CustomPagesConstants.FORWARDED_HDR);
		if (forwardedFrom != null) {
			domainStr = forwardedFrom;
		} else {
			domainStr = getDomain(matcher);
		}
		Domain domain = domainService.getDomain(domainStr);
		String company = domain.getCompany();
		String contextPath = getContextPath(matcher);
		AppDetails appDetails = appDetailsService.getAppDetails(company, contextPath);
		CustomPagesRequest.Builder builder = CustomPagesRequest.builder()
												 .appDetails(appDetails)
												 .domain(domain)
												 .servletContextPath(requestUri)
			.barcodeUrl(configurationParameters.getBarcodeUrl());
		String ipAddress = getIpAddress(request);
		if (StringUtils.hasText(ipAddress)) {
			builder = builder.ipAddress(ipAddress);
		}
		String recaptchaResponse = request.getParameter(RECAPTCHA_RESPONSE_NM);
		if (StringUtils.hasText(recaptchaResponse)) {
			LOG.debug("recaptchaResponse: {}", recaptchaResponse);
			builder = builder.recaptchaResponse(recaptchaResponse);
		}
		return builder.build();
	}


	private Matcher parseUri(String requestUri) {
		Pattern uriPattern = Pattern.compile(URL_REGEX);
		Matcher uriMatcher = uriPattern.matcher(requestUri);
		if (!uriMatcher.matches()) {
			String msg = String.format("Unable to match URL: %s", requestUri);
			LOG.error(msg);
			throw new IllegalArgumentException(msg);
		}
		return uriMatcher;
	}

	private String getDomain(Matcher matcher) {
		String domain = matcher.group(DOMAIN_GROUP);
		if (domain.contains(":")) {
			String[] domainParts = domain.split(":");
			domain = domainParts[0];
		}
		return domain;
	}

	private String getContextPath(Matcher matcher) {
		String matcherContextPath = matcher.group(PATH_GROUP);
		if (matcherContextPath.contains("?")) {
			matcherContextPath = matcherContextPath.substring(0, matcherContextPath.indexOf("?"));
		}
		// contextPath should look like this:
		// [/acscustompages]/p/{resource}/{action}/{segment1}/.../{segmentN}/{view}
		// /acscustompages is the servlet context, which may or may not be present depending on the URL used to get here
		// /p is the container for all controllers (used for path matching for Spring Security authentication)
		// resource is the controller "family", e.g. profile, customResource, etc
		// action is the action the controller performs against the resource - usually maps to a Microservice
		// segment1 to segmentN represents the path to the client specific "app", e.g. preference centre, signup page, etc
		// view represents a specific page in that app.
		// The context path is the list of segments concatenated together delimited by /, e.g. preferences/BMW
		List<String> contextPathParts = new ArrayList<>(Arrays.asList(matcherContextPath.split("/")));
		// remove the server context path if present
		contextPathParts.remove(configurationParameters.getContextPath().replace("/", ""));
		// remove the /p, which is the container for controllers
		contextPathParts.remove("p");
		// remove the third element, which is the action
		contextPathParts.remove(2);
		// remove the second element, which is  the resource name
		contextPathParts.remove(1);
		// Remove the first element as it is empty
		contextPathParts.remove(0);
		// remove the last part, which is the view name
		contextPathParts.remove(contextPathParts.size() - 1);
		return String.join("/", contextPathParts);
	}

	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader(FORWARDED_FOR_HDR);
		if (StringUtils.isEmpty(ip) || ip.equalsIgnoreCase("unknown")) {
			ip = request.getRemoteAddr();
		}
		if(ip!=null){
			ip = ip.split(",")[0].trim();
		}
		LOG.debug("IP address is {}", ip);
		return ip;
	}
}
