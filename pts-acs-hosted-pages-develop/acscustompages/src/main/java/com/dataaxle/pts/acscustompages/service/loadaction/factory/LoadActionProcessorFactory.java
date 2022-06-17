package com.dataaxle.pts.acscustompages.service.loadaction.factory;

import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.service.loadaction.LoadActionProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a mechanism to build LoadActionProcessor instances via a static method call.  It does this by
 * holding a map of LoadActionProcessorBuilder instances.  The appropriate builder to use is determined from the
 * provided LoadActionConfiguration.
 * LoadActionProcessorBuilders register themselves with the factory when they are initialised.
 */
@Component(value = "loadActionProcessorFactory")
public class LoadActionProcessorFactory {


	private static final Map<LoadActionConfig.ActionType, LoadActionProcessorBuilder> builders = new HashMap<>();

	public static LoadActionProcessor<?> of(LoadActionConfig config) {
		if (builders.containsKey(config.getActionType())) {
			return builders.get(config.getActionType()).build(config);
		}
		throw new IllegalStateException(String.format("No builder registered for LoadActionConfig ActionType %s", config.getActionType()));
	};

	public static void register(LoadActionConfig.ActionType action, LoadActionProcessorBuilder builder) {
		builders.putIfAbsent(action, builder);
	}
}
