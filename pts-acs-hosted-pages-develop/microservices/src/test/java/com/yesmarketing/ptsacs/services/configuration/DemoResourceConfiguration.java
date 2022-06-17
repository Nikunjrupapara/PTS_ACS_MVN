package com.yesmarketing.ptsacs.services.configuration;

import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.resource.DemoResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class DemoResourceConfiguration {

	@Autowired
	private AdminModelMapperHelper adminModelMapperHelper;

	@Bean
	public DemoResource demoResource() {
		return new DemoResource(adminModelMapperHelper);
	}
}
