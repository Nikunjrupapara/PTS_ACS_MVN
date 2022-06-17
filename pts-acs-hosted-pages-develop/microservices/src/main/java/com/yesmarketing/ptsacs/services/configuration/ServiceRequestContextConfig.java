package com.yesmarketing.ptsacs.services.configuration;

import com.google.common.collect.Lists;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import com.yesmarketing.ptsacs.services.service.impl.FormJwtTokenServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Deprecated
@Configuration
public class ServiceRequestContextConfig {

	private static final String THREAD_LOCAL_NAME = "servicesRequestContext";

	@Value("${jwt.secretKey.form}")
	private String formJwtSecretKey;

	//@Bean
	public FormJwtTokenService formJwtTokenService() {
		return new FormJwtTokenServiceImpl(formJwtSecretKey);
	}

	@Bean
	public ServicesRequestContextFilter servicesRequestContextFilter() {
		return new ServicesRequestContextFilter(formJwtTokenService()/*, formConfigService*//*authenticationManager*/);
	}

	@Bean
	public FilterRegistrationBean<ServicesRequestContextFilter> requestContextFilterRegistration() {
		FilterRegistrationBean<ServicesRequestContextFilter> result = new FilterRegistrationBean<>();
		result.setEnabled(false);
		result.setFilter(servicesRequestContextFilter());
		result.setUrlPatterns(Lists.newArrayList("/v1/services/*"));
		result.setName("Services Request Context Filter");
		result.setOrder(1);
		return result;
	}

	@Bean(destroyMethod = "destroy")
	public ThreadLocalTargetSource threadLocalServicesRequestContext() {
		ThreadLocalTargetSource result = new ThreadLocalTargetSource();
		result.setTargetBeanName(THREAD_LOCAL_NAME);
		return result;
	}

	@Primary
	@Bean(name = "proxiedThreadLocalTargetSource")
	public ProxyFactoryBean proxiedThreadLocalTargetSource(ThreadLocalTargetSource threadLocalTargetSource) {
		ProxyFactoryBean result = new ProxyFactoryBean();
		result.setTargetSource(threadLocalTargetSource);
		return result;
	}

	@Bean(name = THREAD_LOCAL_NAME)
	@Scope(scopeName = "prototype")
	public ServicesRequestContext servicesRequestContext() {
		return new ServicesRequestContext();
	}
}
