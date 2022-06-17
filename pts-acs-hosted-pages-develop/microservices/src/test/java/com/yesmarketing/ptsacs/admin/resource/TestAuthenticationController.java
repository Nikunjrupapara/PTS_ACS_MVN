package com.yesmarketing.ptsacs.admin.resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yesmarketing.ptsacs.common.exception.PtsResponseEntityExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({ PtsResponseEntityExceptionHandler.class  })
@ActiveProfiles("mongotest")
public class TestAuthenticationController {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void valid_credentials() throws Exception {
		String body = "{\"usernameOrEmail\": \"user\",\"password\": \"password\"}";
		mockMvc.perform(post("/authenticate")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.with(csrf())
			.content(body))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string("Authenticated"))
			.andExpect(cookie().exists("accessToken"))
			.andExpect(cookie().secure("accessToken", true));
	}

	@Test
	void invalid_credentials() throws Exception {
		String body = "{\"usernameOrEmail\": \"user\",\"password\": \"password1\"}";
		mockMvc.perform(post("/authenticate")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(body)
			.with(csrf()))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.status", equalTo(401)))
			.andExpect(jsonPath("$.message", equalTo("Invalid username or password.")))
			.andExpect(jsonPath("$.moreInfo", equalTo("Please check your credentials and try again.")));
	}
}
