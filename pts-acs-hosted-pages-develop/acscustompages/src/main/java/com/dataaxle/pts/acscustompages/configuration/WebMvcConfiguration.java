package com.dataaxle.pts.acscustompages.configuration;

import com.dataaxle.pts.acscustompages.exception.PtsSimpleMappingExceptionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		LOG.debug("Configuring SimpleMappingExceptionResolver...");
		SimpleMappingExceptionResolver r = new PtsSimpleMappingExceptionResolver();
		Properties mapping = new Properties();
		mapping.setProperty("AppDetailsNotFoundException", "notfound");
		r.setExceptionMappings(mapping);
		r.setDefaultErrorView("error");
		r.setExceptionAttribute("e");
		r.setWarnLogCategory("com.dataaxle.pts.acscustompages.exception.SimpleMappingExceptionHandler");
		return r;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/companies-static/**")
			.addResourceLocations("classpath:/static/companies-static/");
	}
}
