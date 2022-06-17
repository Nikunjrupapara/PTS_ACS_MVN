package com.dataaxle.pts.acscustompages.model;

import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class GetCustomResourceRequest implements FindCustomResourceRequest {
	/**
	 * This class represents a request to retrieve a record from a Custom Resource.  Clients can use it in two ways:
	 * with or without a filter.  This will determine which Microservices API endpoint is used to service the request:
	 * With a filter:  /v1/services/customResource/{customResource}/records/search/{filter}?{queryString}
	 * Without a filter: /v1/services/customResource/{customResource}/records/find?{queryString}
	 *
	 */

	String customResource;

	String filter;

	Map<String, String> parameters;

	List<String> numbersAsString;

	private GetCustomResourceRequest(Builder builder) {
		this.customResource = builder.customResource;
		this.filter = builder.filter;
		this.numbersAsString = builder.numbersAsString;
		this.parameters = builder.parameters;
	}

	@Deprecated
	public GetCustomResourceRequest(String customResource) {
		this.customResource = customResource;
		this.filter = "";
		this.numbersAsString = Collections.emptyList();
		this.parameters = new HashMap<>();
	}

	@Deprecated
	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}

	public String getParameter(String name) {
		return parameters.getOrDefault(name, "");
	}

	@Override
	public String buildUri() {
		StringBuilder uri = new StringBuilder("customResources/").append(customResource).append("/records");
		boolean useFilter = StringUtils.hasText(filter);
		uri.append(useFilter ? uriWithFilter() : uriWithoutFilter());
		if (parameters.size() > 0) {
			uri.append("?").append(queryString());
		}
		return uri.toString();
	}

	@Override
	public CustomResourceParameters getCustomResourceParameters(String company) {
		return CustomResourceParameters.builder()
			.company(company)
			.name(customResource)
			.numberAsString(numbersAsString)
			.build();
	}

	private String uriWithFilter() {
		return String.format("/search/%s", filter);
	}

	private String uriWithoutFilter() {
		return "find";
	}

	private String queryString() {
		return parameters.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
			.collect(Collectors.joining("&"));
	}

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Builder() {}

		private String customResource;

		private String filter;

		private final Map<String, String> parameters = new HashMap<>();

		private final List<String> numbersAsString = new ArrayList<>();

		public GetCustomResourceRequest build() {
			return new GetCustomResourceRequest(this);
		}

		private Builder self() {
			return this;
		}

		public Builder customResource(String customResource) {
			this.customResource = customResource;
			return self();
		}

		public Builder filter(String filter) {
			this.filter = filter;
			return self();
		}

		public Builder addParameter(String name, String value) {
			this.parameters.put(name, value);
			return self();
		}

		public Builder addParameters(Map<String, String> parameters) {
			this.parameters.putAll(parameters);
			return self();
		}

		public Builder numberAsString(List<String> names) {
			this.numbersAsString.addAll(names);
			return self();
		}
	}
}
