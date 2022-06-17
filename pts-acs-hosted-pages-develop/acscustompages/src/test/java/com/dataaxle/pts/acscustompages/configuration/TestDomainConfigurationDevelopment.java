package com.dataaxle.pts.acscustompages.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dataaxle.pts.acscustompages.exception.DomainNotFoundException;
import com.dataaxle.pts.acscustompages.model.Domain;
import com.dataaxle.pts.acscustompages.service.DomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = { "development" })
@ContextConfiguration(initializers = { ConfigDataApplicationContextInitializer.class })
public class TestDomainConfigurationDevelopment {

	@Autowired
	private DomainService domainService;

	private Domain domain;

	@Test
	void resideo() {
		domain = domainService.getDomain("test.resideo.com");
		assertEquals("resideo", domain.getBrand(), "brand");
		assertEquals("resideo", domain.getCompany(), "company");
	}

	@Test
	void ymnewsolutions() {
		domain = domainService.getDomain("test.ymnewsolutions.com");
		assertEquals("ymnewsolutions", domain.getBrand(), "brand");
		assertEquals("ymnewsolutions", domain.getCompany(), "company");
	}

	@Test
	void usbank() {
		domain = domainService.getDomain("test.mybmwcreditcard.com");
		assertEquals("BMW", domain.getBrand());
		assertEquals("usbank", domain.getCompany());
	}

	@Test
	void notFound() {
		assertThrows(DomainNotFoundException.class, () -> domainService.getDomain("somedomain.com"));
	}
}
