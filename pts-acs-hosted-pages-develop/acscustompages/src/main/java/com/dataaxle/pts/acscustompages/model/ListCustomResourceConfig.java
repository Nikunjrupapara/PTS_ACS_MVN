package com.dataaxle.pts.acscustompages.model;

import static com.google.common.base.Preconditions.checkArgument;

import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Value
public class ListCustomResourceConfig implements LoadActionConfig, Serializable {

	private static final long serialVersionUID = -811763955786523043L;

	/**
	 * The order in which this load action is performed
	 */
	int order;

	/**
	 * The name of the list on the client page.
	 * TODO: not sure if this is still required.
	 */
	String listFieldName;

	/**
	 * The name of the custom resource to list records from
	 */
	String customResource;

	/**
	 * The name of the filter to use to list the records
	 */
	String filterName;

	/**
	 * The names of the parameters required by the filter
	 */
	List<String> parameterNames;

	/**
	 * If true then only pageSize records will be retrieved by each request to the Microservices API before returning
	 * to the client.
	 * If false then the ListCustomResourceRecords service will retrieve all matching records from the custom resource
	 * before returning to the client, even if that necessitates multiple requests to the Microservices API.
	 */
	boolean usePagination;

	/**
	 * The number of records to retrieve in each request to the Microservices API, which corresponds to the number of
	 * records to display on each page in the client.  Defaults to 25 (as per ACS).  Must be a positive value.
	 */
	int pageSize;

	/**
	 * A list of fields to use for sorting the result set.  Fields will be sorted in the order they are added to the list.
	 */
	List<SortParameter> sorting;

	/**
	 * TODO: may not need this if the controller is refactored to load list on a GET request instead of POST
	 * Specifies when the list request is executed.
	 * ON_LOAD: when the page is displayed.  This allows us to retrieve data to use in initialising the page.
	 * ON_SUBMIT: when the page is submitted.
	 */
	ExecutionPoint executionPoint;

	/**
	 * Defines how the app should behave when the Load Action's request returns no data.  Defaults to LOG_AND_SWALLOW.
	 */
	DataNotFoundStrategy dataNotFoundStrategy;

	/**
	 *
	 */
	List<String> numbersAsString;

	private ListCustomResourceConfig(Builder builder) {
		this.customResource = builder.customResource;
		this.dataNotFoundStrategy = builder.dataNotFoundStrategy;
		this.executionPoint = builder.executionPoint;
		this.filterName = builder.filterName;
		this.listFieldName = builder.listFieldName;
		this.numbersAsString = builder.numbersAsString;
		this.order = builder.order;
		this.pageSize = builder.pageSize;
		this.parameterNames = builder.parameterNames;
		this.sorting = builder.sorting;
		this.usePagination = builder.usePagination;
	}

	@Override
	public ActionType getActionType() { return ActionType.LIST_CUSTOM_RESOURCE; }

	@Override
	public int getOrder() { return order; }

	@Override
	public DataNotFoundStrategy getDataNotFoundStrategy() { return dataNotFoundStrategy; }

	public static Builder builder() { return new Builder(); }

	public enum ExecutionPoint {
		ON_LOAD("onLoad"),
		ON_SUBMIT("onSubmit");

		private final String code;

		ExecutionPoint(String code) { this.code = code; }

		public static ExecutionPoint of(String name) {
			return Arrays.stream(values())
				.filter(val -> val.code.equals(name))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown execution point: " + name));
		}
	}

	public static class Builder implements LoadActionConfig.Builder {
		private Builder() {}

		private int order = 0;

		private String listFieldName;

		private String customResource;

		private String filterName;

		private final List<String> parameterNames = new ArrayList<>();

		boolean usePagination = true;

		private int pageSize = 25;

		final List<SortParameter> sorting = new ArrayList<>();

		private ExecutionPoint executionPoint = ExecutionPoint.ON_SUBMIT;

		private DataNotFoundStrategy dataNotFoundStrategy = LoadActionConfig.DataNotFoundStrategy.LOG_AND_SWALLOW;

		private final List<String> numbersAsString = new ArrayList<>();

		private Builder self() { return this; }

		public Builder order(int order) {
			this.order = order;
			return self();
		}

		public Builder listFieldName(String name) {
			this.listFieldName = name;
			return self();
		}

		public Builder customResource(String name) {
			this.customResource = name;
			return self();
		}

		public Builder filterName(String name) {
			this.filterName = name;
			return self();
		}

		public Builder parameterName(String name) {
			this.parameterNames.add(name);
			return self();
		}

		public Builder pagination() {
			this.usePagination = true;
			return self();
		}

		public Builder noPagination() {
			this.usePagination = false;
			return self();
		}

		public Builder pageSize(int pageSize) {
			this.pageSize = pageSize;
			return self();
		}

		public Builder sortParameter(String name) {
			return this.sortParameter(SortParameter.builder().fieldName(name).ascending().build());
		}

		public Builder sortParameter(SortParameter parameter) {
			this.sorting.add(parameter);
			return self();
		}

		public Builder sortParameter(String fieldName, String direction) {
			return this.sortParameter(SortParameter.builder().fieldName(fieldName).sortDirection(direction).build());
		}

		public Builder sortParameter(String fieldName, SortDirection direction) {
			return this.sortParameter(SortParameter.builder().fieldName(fieldName).sortDirection(direction).build());
		}

		public Builder executionPoint(ExecutionPoint executionPoint) {
			this.executionPoint = executionPoint;
			return self();
		}

		public Builder executionPoint(String name) {
			return this.executionPoint(ExecutionPoint.of(name));
		}

		public Builder dataNotFoundStrategy(DataNotFoundStrategy strategy) {
			this.dataNotFoundStrategy = strategy;
			return self();
		}

		public Builder numberAsString(String name) {
			this.numbersAsString.add(name);
			return self();
		}

		public ListCustomResourceConfig build() {
			ListCustomResourceConfig result = new ListCustomResourceConfig(this);
			validate(result);
			return result;
		}

		private void validate(ListCustomResourceConfig obj) {
			checkArgument(StringUtils.hasText(obj.listFieldName), "List field name must be provided!");
			checkArgument(StringUtils.hasText(obj.customResource), "Custom Resource must be provided!");
			checkArgument(StringUtils.hasText(obj.filterName), "Filter Name must be provided!");
			checkArgument(!obj.parameterNames.isEmpty(), "At least one parameter must be provided");
			obj.parameterNames.forEach(name -> checkArgument(StringUtils.hasText(name), "Parameter Name must be supplied!"));
			checkArgument(obj.pageSize > 0 && obj.pageSize <= 200, "Page Size must be between 1 and 200");
		}
	}
}
