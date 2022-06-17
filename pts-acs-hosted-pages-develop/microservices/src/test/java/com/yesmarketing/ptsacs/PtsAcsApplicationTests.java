package com.yesmarketing.ptsacs;

import com.yesmarketing.ptsacs.configuration.EnableEmbeddedMongo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("mongotest")
@EnableEmbeddedMongo
class PtsAcsApplicationTests {

	@Test
	void contextLoads() {
	}

}
