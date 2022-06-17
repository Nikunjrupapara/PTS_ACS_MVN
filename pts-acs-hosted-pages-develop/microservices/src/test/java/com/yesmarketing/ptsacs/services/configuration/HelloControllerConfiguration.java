package com.yesmarketing.ptsacs.services.configuration;

import com.yesmarketing.ptsacs.admin.resource.HelloController;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

public class HelloControllerConfiguration {

	@Bean
	public HelloController helloController() {
		return new HelloController();
	}
}
