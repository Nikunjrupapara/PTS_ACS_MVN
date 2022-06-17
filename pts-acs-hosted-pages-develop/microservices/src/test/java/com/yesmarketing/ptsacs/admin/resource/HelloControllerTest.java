package com.yesmarketing.ptsacs.admin.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yesmarketing.ptsacs.common.authentication.AuthenticationConfigurationForControllerTests;
import com.yesmarketing.ptsacs.common.exception.PtsResponseEntityExceptionHandler;
import com.yesmarketing.ptsacs.services.configuration.HelloControllerConfiguration;
import com.yesmarketing.ptsacs.services.configuration.ResourceTestsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HelloController.class)
@ContextConfiguration(classes = { ResourceTestsConfiguration.class,	HelloControllerConfiguration.class})
@ImportAutoConfiguration({ AuthenticationConfigurationForControllerTests.class, PtsResponseEntityExceptionHandler.class })
public class HelloControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void authenticated() throws Exception {
		mockMvc.perform(get("/api/hello")
		.accept(MediaType.TEXT_PLAIN))
		.andDo(print())
		.andExpect(status().is2xxSuccessful());
	}
}
