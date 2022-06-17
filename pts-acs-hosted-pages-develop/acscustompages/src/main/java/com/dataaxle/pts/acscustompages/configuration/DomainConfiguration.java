package com.dataaxle.pts.acscustompages.configuration;

import com.dataaxle.pts.acscustompages.model.Domain;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "config")
@Slf4j
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@Data
public class DomainConfiguration {

	private List<Domain> domains;

}
