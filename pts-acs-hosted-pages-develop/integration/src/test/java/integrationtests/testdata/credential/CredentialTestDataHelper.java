package integrationtests.testdata.credential;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import integrationtests.testdata.RequestUtils;
import io.restassured.http.Cookies;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CredentialTestDataHelper {

	private static List<String> companies = new ArrayList<>();

	public static void createData() {
		List<Map<String, Object>> credentials = toCreate();
		credentials.forEach(credential -> {
			companies.add((String)credential.get("company"));
			RequestUtils.post("/credentials", credential);
		});
	}

	private static List<Map<String, Object>> toCreate() {
		return Stream.of(
			ymnewsolutions()
		).collect(Collectors.toList());
	}

	private static Map<String, Object> ymnewsolutions() {
		Map<String, Object> map = new HashMap<>();
		map.put("company", "ymnewsolutions");
		map.put("clientId", "6c3fe03bfba046c1a1c5db52a74817e0");
		map.put("enabled", Boolean.TRUE);
		map.put("organization", "yesmarketing-mkt-stage3");
		map.put("organizationId", "108F02175D4CBB8C0A495CE7@AdobeOrg");
		String privateKey = "-----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDSq0JlrIA9nMmFt1ATN7y7hRUdRBVcqcy1h64XKxdquvrZu3LEqxqrJ9GvKJCOdxAYKuMN/rMzKE4ZwevjeTaikvPBYfbZELA3cOGpsXJi/CFWLjI3OeYkIMlXu8djW51az2ZzlBpn+DJi1dpTW+T7U1v7IFGPt2dSAtHkdsjEa14WmuUHxBUh7WA8lfjvaCxoaew+BJMvw1CM5OdYqvTdsFt5LB85Zo1hAzhHRqocyPuVNG/R1sCEFk6JH8drNrfEbEdXl7uqqhfaRVylqM6furrY9OGqzryN6iEy6v46M4E8uhfMkr55KdU7X47uDpBs36toCUkicjwR1RiUTwrZAgMBAAECggEBALlM1Ck0UtRezU8lT3Sn6QUT6N+GHDgWTq45eqJ8T6ucbbsISsm8KsfzTVeBGa2GAo0PZAnSR3Mu94tmbAL4glr7YRZvAb4qFKWfT46AOznfxb7VAWxcLBcJE7BsxdOaKM1QcjAlTBi5UjBnJBOAV+fMibJY2gTMcD/NpSsMZEmHAeHAuNxPNVYpkaLxIIQ/yMQ47r7L4v4DBq5lqfqJQXdbm0Fd/ROcuF1E4ix88d8Xqs69qQK3v16B7rEOVXFxNsfgkLi3m28YQxIzsmRIfY6zhCHHzYijSqn6NvUyH1m7GE5nwIPAb2rXH+SwSyOmJRcDx3pF9Ry5uMHy7CZatdECgYEA9WVWUp83o6PZGeXgSpuitfDsYSCik6dLyKBCtHhUXR2uNlfkgxys9rHe3eaZoTZnFEzr3/4vJxhYP61r8rTO2i18SQkFvtczqXv6UL/7P5oR1tCg6/lHaVn7uw+/a6P/z5nVMexL32R6/8HlWMPGEFxXJLES7u8m83UCo/ZIbUUCgYEA28XCoXVue00L+/6Y7HInuCGIQDE6Cgp2ir821iS4JvAKqPyyxePmLjgZvvtZoJFkopVGSOKNAH2F0jLw3Gsp72kr8+jKYODJcg6FXNsyohmE+jI05xFACqUflCBVAkhHUqVk5okZyS+BB+XvHdeatDAGW+vVqkE55SQBvPfqjoUCgYBzSTF9zWkxSrei8BkoU+fkPBSBKPjYD1+OGKRd7q4LtPb0duRXKHCl0TiX3U9NqqZQpjamW99BKbHD8LBnLMSFQZtDIKb/WlDeDe2yZA8geTxqREdltQ30k56s6iAO7NQwbt/2/UFD6QvT/f7GbesuIoOaLEpt0BLni5A7AWhTVQKBgQC95MOGZQRgb7NCOruB2iUtuXOUG7TNhjrlKdFE+RdRNanQWABUctWp6/l7Bo1r6xTsUlUXlwFEgjEF7dQmcEYcwmAi8WskiDYnYhqjoBbA75Hx6T3CxinyyLhfcgxh23gdFiH3/FCWgIBaBbg1rdzTgoeMF1tYL+HJdvI1iHVYTQKBgFi5nDG6cdvMzRCw2fm9zwJhABCUUQwUvWTWhPFI5RyWPltqpgyk6SI83dIL1cL2GqIGQ5eeX37yBIayiYl+QXmc4tTjSQuB3ILPm45NEbxx1nSW2XjAJ07IcEAmfLvnhPntDw8nHis7OD/D1JtdjRnShF/o6q5bh7DuenyPuk5D-----END PRIVATE KEY-----";
		map.put("privateKey", privateKey);
		map.put("secret", "d31f36f0-49b8-47cb-916c-44c3abe9446f");
		map.put("transactionalApi", "yesmarketing");
		map.put("username", "6E5947685E21A3F00A495E3B@techacct.adobe.com");
		return map;
	}

	public static void deleteData() {
		String jsonStr = RequestUtils.get("/credentials");
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode json = mapper.readTree(jsonStr);
			for (JsonNode node : json) {
				String company = node.get("company").asText();
				RequestUtils.delete(String.format("/credentials/%s", company));
			}
		} catch (JsonProcessingException e) {
			log.error("JSON parsing error", e);
		}
	}

	public static List<String> getCompanies() {
		return companies;
	}
}
