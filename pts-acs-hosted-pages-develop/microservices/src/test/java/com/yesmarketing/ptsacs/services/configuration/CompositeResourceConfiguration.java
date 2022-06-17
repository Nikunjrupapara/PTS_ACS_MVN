package com.yesmarketing.ptsacs.services.configuration;

import com.yesmarketing.ptsacs.services.resource.CompositeResource;
import com.yesmarketing.ptsacs.services.service.CompositeService;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class CompositeResourceConfiguration {

	@Autowired
	private ServicesModelMapperHelper servicesModelMapperHelper;

	@Bean
	public CompositeResource compositeResource() {
		return new CompositeResource(compositeService(), servicesModelMapperHelper);
	}

	@Bean
	public CompositeService compositeService() {
		CompositeService compositeService = Mockito.mock(CompositeService.class);
		// Add method mocks here
		return compositeService;
	}
}
