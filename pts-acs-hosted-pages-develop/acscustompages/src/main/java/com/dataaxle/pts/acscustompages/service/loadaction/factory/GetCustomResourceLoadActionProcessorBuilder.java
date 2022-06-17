package com.dataaxle.pts.acscustompages.service.loadaction.factory;

import com.dataaxle.pts.acscustompages.model.loadaction.GetCustomResourceLoadActionConfig;
import com.dataaxle.pts.acscustompages.model.loadaction.LoadActionConfig;
import com.dataaxle.pts.acscustompages.service.CustomResourceService;
import com.dataaxle.pts.acscustompages.service.loadaction.GetCustomResourceLoadActionProcessor;
import com.dataaxle.pts.acscustompages.service.loadaction.LoadActionProcessor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Builds an instance of a GetCustomResourceLoadActionProcessor.
 */
@Component
@DependsOn(value = { "loadActionProcessorFactory" })
public class GetCustomResourceLoadActionProcessorBuilder implements LoadActionProcessorBuilder {

	private final CustomResourceService customResourceService;

	public GetCustomResourceLoadActionProcessorBuilder(CustomResourceService customResourceService) {
		this.customResourceService = customResourceService;
	}

	@Override
	public LoadActionProcessor<?> build(LoadActionConfig loadActionConfig) {
		GetCustomResourceLoadActionConfig config = (GetCustomResourceLoadActionConfig) loadActionConfig;
		return new GetCustomResourceLoadActionProcessor(customResourceService, config);
	}

	@Override
	@PostConstruct
	public void register() {
		LoadActionProcessorFactory.register(LoadActionConfig.ActionType.GET_CUSTOM_RESOURCE, this);
	}
}
