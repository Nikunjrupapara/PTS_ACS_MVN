package integrationtests.security;

import integrationtests.testdata.form.JsonWebTokenRepository;
import integrationtests.testdata.form.TokenStateException;
import io.cucumber.java8.En;
import io.cucumber.java8.Scenario;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.springframework.http.HttpStatus;

@Slf4j
public class AuthenticationSteps implements En {

	private RequestSpecification request;

	private ValidatableResponse response;
	private final String acsId = "bdb64076-2052-4762-90da-8ab56c35c30c";
	private final String emailSha = "1fac74c1f1a03f4b095dc7703e5bbd1096af6b624eac91592008f35c263db46aef59eb6e1ffdd1b94dd9977a2218932225edcde7aa6357bec88ccf2f795be372";

	public AuthenticationSteps() {

		Before(10, () -> {
			request = RestAssured
						  .with()
						  .baseUri("http://localhost:8080")
						  .accept(ContentType.JSON)
						  .log().all();
		});

		Given("^a request to the Microservices API$", () -> {
			request.given();
		});
		And("^the request has no Services Token$", () -> {
			// No need to do anything
		});
		And("the request contains a Services Token which {string}", (String tokenState) -> {
			String token = JsonWebTokenRepository.getToken(tokenState)
							   .orElseThrow(() -> new TokenStateException(tokenState));
			log.info("tokenState: {}, token: {}", tokenState, token);
			request.header(new Header("x-services-token", token));
			// This header is not required but used to make it easier to find requests in the server logs
			request.header(new Header("x-token-state", tokenState));
		});
		When("^the request is submitted$", () -> {
			response = request.when().get(String.format("/v1/services/profiles?s=%s&l=%s", acsId, emailSha))
						   .then().log().all();
		});
		Then("^it returns an Unauthorized response$", () -> {
			response.statusCode(HttpStatus.UNAUTHORIZED.value());
		});
		Then("^it returns a Success response$", () -> {
			response.statusCode(HttpStatus.OK.value());
		});
	}

	private void initialiseForms() {
		log.info("initialiseForms()...");
	}

	private void deleteForms() {
		log.info("deleteForms()...");
	}
}
