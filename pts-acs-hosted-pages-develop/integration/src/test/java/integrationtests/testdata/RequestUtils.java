package integrationtests.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestUtils {

	private static Cookies cookies;

	private static final io.restassured.mapper.ObjectMapper objectMapper;

	static {
		cookies = authenticate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.SSS'Z'");
		LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(formatter);
		LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS'Z'");
		DateSerializer dateSerializer = new DateSerializer(true, dateFormat);
		objectMapper = new Jackson2Mapper(((type, charset) -> {
			return new ObjectMapper()
					.registerModule(new Jdk8Module())
					.registerModule(new JavaTimeModule()
							.addDeserializer(LocalDateTime.class, localDateTimeDeserializer)
							.addSerializer(LocalDateTime.class, localDateTimeSerializer))
					.setDateFormat(dateFormat)
					.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
					.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}));
	}

	public static String post(String uri, Map<String, Object> payload) {
		RequestSpecification request = RestAssured
				.with()
				.baseUri("http://localhost:8080/api")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.cookies(cookies)
				.body(payload, objectMapper)
				.log().all();
		Response response = request.post(uri);
		response.then().log().all();
		return response.getBody().asString();
	}

	public static String put(String uri, Map<String, Object> payload) {
		RequestSpecification request = RestAssured
				.with()
				.baseUri("http://localhost:8080/api")
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.cookies(cookies)
				.body(payload, objectMapper)
				.log().all();
		Response response = request.put(uri);
		response.then().log().all();
		return response.getBody().asString();
	}

	public static String get(String uri) {
		RequestSpecification request = RestAssured
				.with()
				.baseUri("http://localhost:8080/api")
				.accept(ContentType.JSON)
				.cookies(cookies)
				.log().all();
		Response response = request.get(uri);
		response.then().log().all();
		return response.getBody().asString();
	}

	public static void delete(String uri) {
		RequestSpecification request = RestAssured
				.with()
				.baseUri("http://localhost:8080/api")
				.cookies(cookies)
				.log().all();
		Response response = request.delete(uri);
		response.then().log().all();
	}

	private static Cookies authenticate() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("usernameOrEmail", "admin");
		parameters.put("password", "adminPassword");
		RequestSpecification authReq = RestAssured
				.with()
				.baseUri("http://localhost:8080")
				.contentType(ContentType.JSON)
				.accept(ContentType.TEXT)
				.body(parameters)
				.log().all();
		Response authResponse = authReq.post("/authenticate");
		authResponse.then().log().all();
		return authResponse.getDetailedCookies();
	}
}
