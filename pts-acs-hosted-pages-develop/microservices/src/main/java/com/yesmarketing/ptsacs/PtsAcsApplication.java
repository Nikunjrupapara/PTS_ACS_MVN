package com.yesmarketing.ptsacs;

import com.yesmarketing.acsapi.admin.dao.YesmailProfileMetadataDao;
import com.yesmarketing.acsapi.admin.resource.CustomResourceMappingResource;
import com.yesmarketing.acsapi.admin.resource.SagasAdminController;
import com.yesmarketing.acsapi.admin.service.CustomResourceMappingService;
import com.yesmarketing.acsapi.admin.service.DataSetService;
import com.yesmarketing.acsapi.admin.service.ReferenceDataApiService;
import com.yesmarketing.acsapi.admin.service.SubscriberApiService;
import com.yesmarketing.acsapi.auth.resource.CredentialResource;
import com.yesmarketing.acsapi.auth.service.impl.CredentialServiceImpl;
import com.yesmarketing.acsapi.health.AdobeCampaignHealthContributor;
import com.yesmarketing.acsapi.service.AttributeMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableCaching
@ComponentScan(
	value = { "com.yesmarketing.ptsacs", "com.yesmarketing.acsapi", "com.yesmail.api.common.config",
			"com.yesmail.api.common.util" },
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AttributeMappingService.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CredentialResource.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CredentialServiceImpl.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SubscriberApiService.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = YesmailProfileMetadataDao.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomResourceMappingResource.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CustomResourceMappingService.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReferenceDataApiService.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DataSetService.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SagasAdminController.class),
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = com.yesmarketing.acsapi.configuration.MongoRepositoryConfiguration.class),
		// temporary exclusion until AdobeCampaignHealthContributor is fixed to not depend on specific credentials existing
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AdobeCampaignHealthContributor.class)
	}
)
public class PtsAcsApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PtsAcsApplication.class);
		String pidFile = System.getProperty("pid.file", "./acs-microservices.pid");
		app.addListeners(new ApplicationPidFileWriter(pidFile));
		app.run(args);
	}

}
