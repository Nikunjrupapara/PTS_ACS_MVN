package com.dataaxle.pts.acscustompages.model.loadaction;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration for an action to load data from a custom resource when a page is loaded.  This is intended for use when
 * there is a requirement to load data that is used to help initialise a form but that data is from a different custom
 * resource from the one that is the main focus of a form.  See Boscov's Friends Helping Friends as an example.
 */
@Value
@EqualsAndHashCode
@ToString
public class GetCustomResourceLoadActionConfig implements LoadActionConfig, Serializable {

	private static final long serialVersionUID = 8300908107035909259L;

	/**
	 * Used to determine the order in which this LoadActionConfig is executed
	 */
	int order;

	/**
	 * The name of the custom resource to retrieve data from
	 */
	String customResource;

	/**
	 * The name of the ACS filter to use
	 */
	String filterName;

	/**
	 * This map defines how to provide the query parameters for the ACS filter.  The key is the name of a query parameter
	 * and the value is a ParameterSource object which provides the name and source, e.g. a query parameter, for the value
	 * used in the query parameter.
	 */
	Map<String, ParameterConfig> parameters;

	/**
	 * This map defines how to map the custom resource field names to a field on the form that is displayed.  The key is
	 * the name of a field on the custom resource and the value is the name of a PageField on the HTML template.
	 */
	Map<String, String> fieldMap;

	/**
	 * Defines how the app should behave when the Load Action's request returns no data.  Defaults to LOG_AND_SWALLOW.
	 */
	DataNotFoundStrategy dataNotFoundStrategy;

	/**
	 * A list of fields on the custom resource that contain numeric data that should be treated as a string
	 */
	List<String> numbersAsString;

	private GetCustomResourceLoadActionConfig(Builder builder) {
		this.customResource = builder.customResource;
		this.dataNotFoundStrategy = builder.dataNotFoundStrategy;
		this.fieldMap = builder.fieldMap;
		this.filterName = builder.filterName;
		this.numbersAsString = builder.numbersAsString;
		this.order = builder.order;
		this.parameters = builder.parameters;
	}

	@Override
	public ActionType getActionType() { return ActionType.GET_CUSTOM_RESOURCE; }

	@Override
	public int getOrder() { return order; }

	@Override
	public DataNotFoundStrategy getDataNotFoundStrategy() { return dataNotFoundStrategy; }

	public static Builder builder() { return new Builder(); }

	public static class Builder implements LoadActionConfig.Builder {
		private Builder() {}

		private int order = 0;

		private String customResource;

		private String filterName;

		private final Map<String, ParameterConfig> parameters = new HashMap<>();

		private final Map<String, String> fieldMap = new HashMap<>();

		private DataNotFoundStrategy dataNotFoundStrategy = LoadActionConfig.DataNotFoundStrategy.LOG_AND_SWALLOW;

		private final List<String> numbersAsString = new ArrayList<>();

		private Builder self() { return this; }

		@Override
		public Builder order(int order) {
			this.order = order;
			return self();
		}

		public Builder customResource(String customResource) {
			this.customResource = customResource;
			return self();
		}

		public Builder filterName(String filterName) {
			this.filterName = filterName;
			return self();
		}

		public Builder parameter(String name, ParameterConfig parameterConfig) {
			this.parameters.put(name, parameterConfig);
			return self();
		}

		public Builder fieldMapping(String target, String source) {
			this.fieldMap.put(target, source);
			return self();
		}

		public Builder dataNotFoundStrategy(DataNotFoundStrategy strategy) {
			this.dataNotFoundStrategy = strategy;
			return self();
		}

		public Builder numberAsString(String name) {
			this.numbersAsString.add(name);
			return self();
		}

		@Override
		public GetCustomResourceLoadActionConfig build() {
			GetCustomResourceLoadActionConfig result = new GetCustomResourceLoadActionConfig(this);
			validate(result);
			return result;
		}

		private void validate(GetCustomResourceLoadActionConfig obj) {
			checkArgument(obj.order >= 0, "Order must be greter or equal to 0");
			checkArgument(StringUtils.hasText(obj.customResource), "Custom Resource must be provided!");
			checkArgument(StringUtils.hasText(obj.filterName), "Filter name must be provided!");
			checkArgument(!CollectionUtils.isEmpty(obj.parameters), "At least one parameter is required.");
			checkArgument(!CollectionUtils.isEmpty(obj.fieldMap), "At least one field mapping must be provided");
		}
	}
}
