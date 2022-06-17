package com.dataaxle.pts.acscustompages.service.loadaction.factory;

import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.service.loadaction.LoadActionProcessor;

import javax.annotation.PostConstruct;

/**
 * Builds an instance of a LoadActionProcessor based upon the supplied configuration.  The builder must be a Spring
 * Component as it requires that any service dependencies for the LoadActionprocessor are injected into it during its
 * construction. Once the LoadActionProcessorBuilder is built it must register itself with the LoadActionProcessorFactory
 * using the register method.
 */
public interface LoadActionProcessorBuilder {

	/**
	 * Constructs an instance of a LoadActionProcessor using the provided configuration
	 * @param loadActionConfig
	 * @return
	 */
	LoadActionProcessor<?> build(LoadActionConfig loadActionConfig);

	/**
	 * Registers the LoadActionProcessorBuilder instance with the LoadActionProcessorFactory
	 */
	@PostConstruct
	void register();
}
