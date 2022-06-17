package com.yesmarketing.ptsacs.services.resource;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yesmarketing.ptsacs.common.repository.FormConfigRepository;
import com.yesmarketing.ptsacs.services.util.FormConfigTestHelper;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("mongotest")
@AutoConfigureMockMvc
public class TestServicesAuthentication {

	private static final String BASE_URI = "/v1/services";

	private String token;

	private String resourceUri;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FormConfigRepository formConfigRepository;

	@BeforeEach
	void setup() {
		Mockito
			.when(formConfigRepository.findByUuid(anyString()))
			.thenAnswer(invocation -> FormConfigTestHelper.getByUuid(invocation.getArgument(0)));
	}

	@Test
	void valid_jwt() throws Exception {
		resourceUri = String.format("%s/demo", BASE_URI);
		token = "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiJiZDVhODI0ZC1lNWY5LTRkY2ItYjNkMi1hM2JlNDRhNGFkYTAifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImp0aSI6ImViYWVmMWNlLTQ2NjItNDZkYi1hMTA4LWQ5NjEzMmI1YjYyZiJ9._zO2XLjL-kj8Hb1D-RcdPxp7NKC3VHgxLFYnp28MmZIbKCkVCgByI-QLX2ZcepGwHgEtw3if11W9nPeHl4_54g";
		mockMvc.perform(get(resourceUri)
			.accept(MediaType.APPLICATION_JSON)
			.header(ServicesConstants.SERVICES_TOKEN_HDR_NAME, token))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.company", equalTo("ymnewsolutions")))
			.andExpect(jsonPath("$.uuid", equalTo("bd5a824d-e5f9-4dcb-b3d2-a3be44a4ada0")))
			//.andExpect(header().exists(ServicesConstants.CSRF_TOKEN_HDR_NAME))
		;
	}

	@Test
	void missingToken() throws Exception {
		resourceUri = String.format("%s/demo", BASE_URI);
		mockMvc.perform(
			get(resourceUri)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	void invalid_jwt() throws Exception {
		resourceUri = String.format("%s/demo", BASE_URI);
		token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiI2MTU3MDRiZC01MzEyLTRiZDMtODllYS1mZDFjMzhlZTQyOGIifSwiaWF0IjoxNTE2MjM5MDIyfQ.2uh55azDKuNf9OURfSphyPcKSgmcmjWQIM8ZOqB79w25XROp3umgGUQZSz6OlNyO-pr4ehTEGqIg1rl2A9Xo9AXXX";
		mockMvc.perform(get(resourceUri)
			.accept(MediaType.APPLICATION_JSON)
			.header(ServicesConstants.SERVICES_TOKEN_HDR_NAME, token))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@DisplayName("Unauthorized token tests")
	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("unauthorizedTokenTestParameters")
	void unauthorizedForms(String name, String token) throws Exception {
		String resourceUri = String.format("%s/demo", BASE_URI);
		mockMvc.perform(get(resourceUri)
			.accept(MediaType.APPLICATION_JSON)
			.header(ServicesConstants.SERVICES_TOKEN_HDR_NAME, token))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	private static Stream<Arguments> unauthorizedTokenTestParameters() {
		return Stream.of(
			arguments("Disabled form", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiJjYjI1ZDFiNS03NDVjLTRhYTctOWU5MC01NGQzOWY3NTNhNTUifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImp0aSI6IjljY2ZmYTBiLTgwYTYtNGRhYS05OTM4LTdmMzllYmU5NzhkMyJ9.6_wS6y3w-jCcshO5qKMl2MtYGTBMUJxtcdblW2PMWJSOHekuCEx1pqk81tsjmv4_Ftq_2fnU-w5D-Fa7rNK2Yw"),
			arguments("Not started form", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiJlODhmMWEzNy0xNjU3LTRkOTMtOTdmNC1kYmQ5YjMyMGE0MmIifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImp0aSI6Ijc1OTU5MGQ5LWI1MjItNDE5MS1hOGYzLWQ1ZDNjNmQzZmJlZSJ9.olZFu83yABHwbnO-qV6OQALnLBj6Q3SMhPekZulf9viPLqF5AxD2GYnU3jJ1LptCOsWepXZxl4KdudbMoiuJhg"),
			arguments("Expired form", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiJiNmI0ZDM1Ny0xNDg2LTRkN2QtYjBhNC0yOTliYjExNDFlMDkifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImp0aSI6IjFkZGRhZmE5LTgwNzYtNDU0MC04NzU1LTg2ZmZjNGYyNDQ4NSJ9.ReyDBJt5IHQ0D5JElpmIgSY7U8ZByAtdhPrVssjMO29uc3WrDSg_hKB34hzLm4coTLnZ72NOrmYcl_k7hKIp5w"),
			arguments("Invalid domain", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiI1YTkwM2YxYi05MTZiLTRkNzgtOTRjNi05MTNlYjE2NWY2ZGUifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImp0aSI6ImUxNWRmOTcxLTgwNjEtNGViNS1iOWQ0LWM4OGQ3Y2VlZWU5ZiJ9.PTiP_j9SBQMPWozgzNpAi_J51UMjTh3_ZMeM-Zbg7aQtE2zr9lZFGbrp--b-SpKoXAfvZbvp6cLE0IwS8dCeeQ"),
			arguments("Invalid Role", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiIwYWNhMzQ5Yi02OTljLTQ4NTItYTQ2OC0zMDBmNTQ3MDVkYTkifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImlhdCI6MTYwMDE3MDExMSwianRpIjoiYjU2NjE1MTEtNGY5Yi00ZjgyLWIwNWYtMTI2NTQxYmNjNTkyIn0.mdzAYdqLzuI--UFgEYFOdoV8XAWX6wpaICLWo0TZ2qLMjzCP-iUMVBqzj5kxo8B5v-7R09lICctPAr1kACs8DA"),
			arguments("Unknown Company", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ1bmtub3duY29tcGFueSIsInV1aWQiOiJjMjVhZGRkMy0xYzljLTQwYzktYmQzZS03NGY2YmYzYTY1NWQifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImlhdCI6MTYwMDI0NzMyNCwianRpIjoiZjljMTc4M2ItOGRlZC00Y2U2LTkxYWQtYzQ0ZDliNTE1MWNhIn0.C0vdRs58cn-d0136mtbTpmkLeOUlJyq_cfVMJSR9Llm0KtFUg7Dr3t0puMPBN3IYKzmBzgKYT5jaB3JmtOBbuA")
		);
	}
}
