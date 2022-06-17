package com.yesmarketing.ptsacs.common.util.logging;

import static net.logstash.logback.marker.Markers.append;

import com.yesmarketing.acsapi.util.AcsMetricsLoggingInterceptor;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ElasticsearchAnalysisFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		FormConfig formConfig = (FormConfig) authentication.getPrincipal();
		boolean logRequest = formConfig != null;
		MetricData metricData = null;
		if (logRequest) {
			try {
				metricData = new MetricData(request, formConfig);
			} catch (Exception e) {
				// This is intended to ensure the request is still processed, even if there is an error in the filter.
				LOG.error("Error in Elasticsearch filter!", e);
				logRequest = false;
			}
		}
		filterChain.doFilter(request, response);
		if (logRequest) {
			metricData.requestComplete(response);
			LOG.info(append("metrics", metricData), "Microservices Metrics");
		}
	}

	@Data
	public static class MetricData {
		private final Instant startTime;

		private Instant endTime;

		private Duration elapsedTime;

		private final String company;

		private final String formName;

		private final HttpMethod method;

		private final String endpoint;

		// This list will contain both query parameters and substituted path variables, e.g. customerIdHash, PKey, etc.
		// This is to avoid having to redefine the mapping of the ElasticSearch data stream that stores the Metric data.
		private final List<AcsMetricsLoggingInterceptor.QueryParameter> queryParameters;

		private HttpStatus status;

		public MetricData(HttpServletRequest request, FormConfig formConfig) {
			this.startTime = Instant.now();
			this.company = formConfig.getCompany();
			this.formName = formConfig.getCode();
			this.method = HttpMethod.valueOf(request.getMethod());
			this.queryParameters = new ArrayList<>();
			this.endpoint = parseContextPath(request.getRequestURI());
			parseQueryString(request.getQueryString());
		}

		public void requestComplete(HttpServletResponse response) {
			this.endTime = Instant.now();
			this.elapsedTime = Duration.between(startTime, endTime);
			this.status = HttpStatus.valueOf(response.getStatus());
		}

		private String parseContextPath(String contextPath) {
			PathVariableReplacer[] replacers = new PathVariableReplacer[] {
				new PkeyPathVariableReplacer(),
				new CustomerIdHashPathVariableReplacer(),
				new EventIdPathVariableReplacer()
			};
			String updatedContextPath = contextPath;
			for (PathVariableReplacer replacer : replacers) {
				if (replacer.containsVariable(updatedContextPath)) {
					PathVariableReplacementResult result = replacer.replaceVariable(updatedContextPath);
					updatedContextPath = result.getContextPath();
					queryParameters.addAll(result.getReplacements());
				}
			}
			return updatedContextPath;
		}

		private void parseQueryString(String queryStr) {
			if (StringUtils.hasText(queryStr)) {
				queryParameters.addAll(Arrays.stream(queryStr.split("&")).map(str -> {
					String[] parts = str.split("=");
					return new AcsMetricsLoggingInterceptor.QueryParameter(parts[0], parts[1]);
				}).collect(Collectors.toList()));
			}
		}
	}

	@Value
	@AllArgsConstructor
	static class PathVariableReplacementResult {

		String contextPath;

		List<AcsMetricsLoggingInterceptor.QueryParameter> replacements;
	}

	interface PathVariableReplacer {
		boolean containsVariable(String contextPath);

		PathVariableReplacementResult replaceVariable(String contextPath);
	}

	static abstract class AbstractPathVariableReplacer implements PathVariableReplacer {

		// A regex to match the path variable value we wish to replace
		protected final String partRegex;

		protected final Pattern partPattern;

		// A regex that matches if the entire URI contains the path variable regex
		protected final String uriRegex;

		protected final Pattern uriPattern;

		protected AbstractPathVariableReplacer(String partRegex) {
			this.partRegex = partRegex;
			this.partPattern = Pattern.compile(partRegex);
			// For uriRegex, the part pattern will always start after a path delimiter (/) but may not end with one,
			// for example if the path variable is at the end of the URI string.
			this.uriRegex = String.format("^.*/%s.*$", partRegex.replace("^", "").replace("$", ""));
			this.uriPattern = Pattern.compile(uriRegex);
		}

		@Override
		public boolean containsVariable(String contextPath) { return uriPattern.matcher(contextPath).matches(); }

		@Override
		public PathVariableReplacementResult replaceVariable(String contextPath) {
			List<AcsMetricsLoggingInterceptor.QueryParameter> replacements = new ArrayList<>();
			String[] parts = contextPath.split("/");
			String[] newParts = new String[parts.length];
			for (int i = 0; i < parts.length; i++) {
				String part = parts[i];
				if (partMatches(part)) {
					newParts[i] = replace();
					replacements.add(new AcsMetricsLoggingInterceptor.QueryParameter(getName(), part));
				} else {
					newParts[i] = part;
				}
			}
			String updatedContextPath = String.join("/", newParts);
			return new PathVariableReplacementResult(updatedContextPath, replacements);
		}

		protected abstract String getName();

		protected boolean partMatches(String part) { return partPattern.matcher(part).matches(); }

		protected String replace() { return String.format("{%s}", getName()); }
	}

	static final class PkeyPathVariableReplacer extends AbstractPathVariableReplacer {

		public PkeyPathVariableReplacer() { super("^@[a-zA-Z0-9\\-\\_]+$"); }

		@Override
		protected String getName() { return "PKey"; }
	}

	static class EventIdPathVariableReplacer extends AbstractPathVariableReplacer {

		public EventIdPathVariableReplacer() { super("^EVT.*$"); }

		@Override
		protected String getName() { return "eventId"; }
	}

	static class CustomerIdHashPathVariableReplacer extends AbstractPathVariableReplacer {

		public CustomerIdHashPathVariableReplacer() { super("^[a-zA-Z0-9]{64}$"); }

		@Override
		protected String getName() { return "customerIdHash"; }
	}
}
