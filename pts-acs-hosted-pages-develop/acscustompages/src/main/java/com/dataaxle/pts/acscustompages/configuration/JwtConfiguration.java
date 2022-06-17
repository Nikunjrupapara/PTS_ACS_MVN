package com.dataaxle.pts.acscustompages.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@Data
public class JwtConfiguration {

	private List<Jwt> jwts;

	@Data
	public static class Jwt {
		String company;

		String contextPath;

		String token;
	}
}
