package com.yesmarketing.ptsacs.services.configuration;

import com.yesmail.api.common.util.MessageService;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.core.MongoTemplate;

public class ResourceTestsConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ServicesModelMapperHelper servicesModelMapperHelper() {
		return new ServicesModelMapperHelper(modelMapper());
	}

	@Bean
	public AdminModelMapperHelper adminModelMapperHelper() { return new AdminModelMapperHelper(modelMapper()); }

	@Bean
	public MessageService messageService() { return new MessageService(); }

	@Bean
	public MongoTemplate mongoTemplate() { return Mockito.mock(MongoTemplate.class); }
}
