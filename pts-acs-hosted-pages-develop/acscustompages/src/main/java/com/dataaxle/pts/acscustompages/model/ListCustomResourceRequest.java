package com.dataaxle.pts.acscustompages.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode
@ToString
public class ListCustomResourceRequest implements FindCustomResourceRequest, Serializable {

	private static final long serialVersionUID = 3587072875348240342L;

	String customResource;

	String filterName;

	Map<String, String> parameters;

	boolean usePagination;

	int pageSize;

	int pageNumber;

	int startIndex;

	List<SortParameter> sorting;

	List<String> numbersAsString;

	private ListCustomResourceRequest(Builder builder) {
		this.customResource = builder.config.getCustomResource();
		this.filterName = builder.config.getFilterName();
		this.parameters = builder.parameters;
		this.usePagination = builder.config.isUsePagination();
		this.pageSize = builder.config.getPageSize();
		this.pageNumber = builder.pageNumber;
		this.sorting = builder.config.getSorting();
		this.startIndex = deriveStartIndex(pageSize, pageNumber);
		this.numbersAsString = builder.config.getNumbersAsString();
	}

	private ListCustomResourceRequest(ListCustomResourceRequest source, int pageNumber) {
		this.customResource = source.customResource;
		this.filterName = source.filterName;
		this.parameters = source.parameters;
		this.usePagination = source.usePagination;
		this.pageSize = source.pageSize;
		this.pageNumber = pageNumber;
		this.sorting = source.sorting;
		this.startIndex = deriveStartIndex(this.pageSize, this.pageNumber);
		this.numbersAsString = source.getNumbersAsString();
	}

	private int deriveStartIndex(int pageSize, int pageNumber) {
		return pageSize * (pageNumber - 1);
	}

	public ListCustomResourceRequest forPageNumber(int pageNumber) {
		return new ListCustomResourceRequest(this, pageNumber);
	}

	@Override
	public String buildUri() {
		StringBuilder uriBuilder = new StringBuilder("/customResources/");
		uriBuilder.append(customResource).append("/records/filter/").append(filterName);
		uriBuilder.append("?_lineCount=").append(pageSize).append("&_lineStart=").append(startIndex);
		if (!parameters.isEmpty()) {
			uriBuilder.append("&")
				.append(parameters.entrySet().stream()
				.map(entry -> String.format("%s_parameter=%s", entry.getKey(), entry.getValue()))
				.collect(Collectors.joining("&")));
		}
		String sortParameter = sorting.stream().map(SortParameter::toAcsString).collect(Collectors.joining(","));
		if (StringUtils.hasText(sortParameter)) {
			uriBuilder.append("&_order=").append(sortParameter);
		}
		return uriBuilder.toString();
	}

	@Override
	public CustomResourceParameters getCustomResourceParameters(String company) {
		return CustomResourceParameters.builder()
			.company(company)
			.name(customResource)
			.numberAsString(numbersAsString)
			.build();
	}

	public static Builder builder(ListCustomResourceConfig config) { return new Builder(config); }

	public static class Builder {

		private final ListCustomResourceConfig config;

		private int pageNumber;

		private final Map<String, String> parameters = new HashMap<>();

		private Builder(ListCustomResourceConfig config) {
			this.config = config;
		}

		private Builder self() { return this; }

		public Builder pageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
			return self();
		}

		public Builder addParameter(String name, String value) {
			this.parameters.put(name, value);
			return self();
		}

		public ListCustomResourceRequest build() {
			ListCustomResourceRequest result = new ListCustomResourceRequest(this);
			validate(result);
			return result;
		}

		private void validate(ListCustomResourceRequest obj) {
			// TODO: add validation
		}
	}
}
