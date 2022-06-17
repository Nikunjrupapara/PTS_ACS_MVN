package com.dataaxle.pts.acscustompages.model.loadaction;

// TODO: for v2 move this class to hostedpages-api module
public interface LoadActionConfig {

	ActionType getActionType();

	/**
	 * Return the order in which this action is to be executed
	 * @return int
	 */
	int getOrder();

	DataNotFoundStrategy getDataNotFoundStrategy();

	/**
	 * Defines the different Load Actions that are available
	 */
	enum ActionType {
		GET_CUSTOM_RESOURCE,
		LIST_CUSTOM_RESOURCE
	}

	/**
	 * Defines the source of a parameter value for a Load Action request
	 */
	enum ParameterSource {
		CUSTOM_RESOURCE_RECORD,
		QUERY_PARAMETER
	}

	/**
	 * Define strategies to use when a Load Action request returns no data
	 */
	enum DataNotFoundStrategy {
		LOG_AND_SWALLOW,
		RETHROW
	}

	interface Builder {
		Builder order(int order);

		LoadActionConfig build();
	}
}
