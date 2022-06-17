package com.dataaxle.pts.acscustompages.utils.logging;

import static net.logstash.logback.marker.Markers.append;

import com.dataaxle.pts.acscustompages.authentication.CustomPagesRequest;
import com.dataaxle.pts.acscustompages.model.form.QueryParameter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ElasticsearchAnalysisFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomPagesRequest customPagesRequest = (CustomPagesRequest) authentication.getPrincipal();
		boolean logRequest = customPagesRequest != null;
		ElasticsearchData elasticsearchData = null;
		if (logRequest) {
			try {
				elasticsearchData = new ElasticsearchData(httpServletRequest, customPagesRequest);
			} catch (Exception e) {
				// This is intended to ensure the request is still processed, even if there is an error in the filter.
				LOG.error("Error in Elasticsearch filter!", e);
				logRequest = false;
			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
		if (logRequest) {
			elasticsearchData.requestComplete(httpServletResponse);
			LOG.info(append("metrics", elasticsearchData), "Hosted Pages metrics");
		}
	}

	@Data
	public static class ElasticsearchData {

		private Instant startTime;

		private Instant endTime;

		private Duration elapsedTime;

		private HttpMethod method;

		private String company;

		private String brand;

		private String domain;

		private String uri;

		private String contextPath;

		private String viewName;

		private String userAgent;

		private List<QueryParameter> queryParameters;

		private HttpStatus status;

		public ElasticsearchData(HttpServletRequest request, CustomPagesRequest customPagesRequest) {
			this.startTime = Instant.now();
			this.method = HttpMethod.valueOf(request.getMethod());
			this.company = customPagesRequest.getCompany();
			this.brand = customPagesRequest.getBrand();
			this.domain = customPagesRequest.getDomainName();
			this.uri = request.getRequestURI();
			this.contextPath = customPagesRequest.getContextPath();
			this.viewName = customPagesRequest.getCurrentPageName();
			this.userAgent = request.getHeader("User-Agent");
			this.queryParameters = customPagesRequest.getQueryParams().getParameters().entrySet().stream()
				.map(entry -> new QueryParameter(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
		}

		public void requestComplete(HttpServletResponse response) {
			this.endTime = Instant.now();
			this.elapsedTime = Duration.between(startTime, endTime);
			this.status = HttpStatus.valueOf(response.getStatus());
		}
	}
}
