package com.dataaxle.pts.acscustompages.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.dataaxle.pts.acscustompages.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles(profiles = { "development" })
@ContextConfiguration(initializers = {ConfigDataApplicationContextInitializer.class})
public class TestJwtConfiguration {

	@Autowired
	private JwtService jwtService;

	@Test
	void ymnewsolutions_marketing_signup() {
		assertNotNull(jwtService.getToken("ymnewsolutions", "marketing/signup"));
	}
}
