package com.dataaxle.pts.acscustompages.model.loadaction;

import java.util.List;
import java.util.Objects;

// TODO: In V2 move this interface to hostedpages-api module

/**
 * This interface is implemented by classes that can provide the value for a query parameter to be added to the URI for
 * a request to the Microservices API.
 */
public interface ValueProvider {

	LoadActionConfig.ParameterSource getParameterSource();

	/**
	Returns the value of the data point in the implementing class identified by the provided name.
	 */
	String provideValue(String name);

	static ValueProvider getValueProvider(LoadActionConfig.ParameterSource source, ValueProvider... sources) {
		return ValueProvider.sourcesList(sources).stream()
			.filter(Objects::nonNull)
			.filter(vp -> vp.getParameterSource().equals(source))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException(String.format("No value provider found for source %s", source)));
	}

	static List<ValueProvider> sourcesList(ValueProvider... sources) {
		return List.of(sources);
	}
}
