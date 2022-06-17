package com.dataaxle.pts.acscustompages.json.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.dataaxle.pts.acscustompages.configuration.JacksonConfiguration;
import com.dataaxle.pts.acscustompages.exception.UnsupportedNodeTypeException;
import com.dataaxle.pts.acscustompages.json.deserialize.CustomResourceRecordDeserializerFactory;
import com.dataaxle.pts.acscustompages.model.CustomResourceParameters;
import com.dataaxle.pts.acscustompages.model.CustomResourceRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class TestCustomResourceRecordDeserializer {

	private ObjectMapper objectMapper;

	private CustomResourceRecord.Deserializer deserializer;

	@BeforeEach
	void setup() {
		objectMapper = new JacksonConfiguration().objectMapper();
		deserializer = new CustomResourceRecord.Deserializer(objectMapper);
	}

	@Test
	void resideo_coupon_code() throws Exception {
		String json = "{\n" +
						  "    \"item_code\": \"\",\n" +
						  "    \"assigned_date\": \"2021-05-10 14:40:34.900Z\",\n" +
						  "    \"promo_description\": \"\",\n" +
						  "    \"order_number\": \"\",\n" +
						  "    \"acsId\": \"d624374e-fbc8-4fea-be0c-a798e356cc0f\",\n" +
						  "    \"PKey\": \"@j0EolflNeHge9EZrq0SpCqybvr32D8ciux5Z1Nz7-kuCro1uJGkLbqHySpPtYpMe9e3ggcxuOcbzwy_uzT9qvJ33S3XYu-6bJh_k42jY9tKe-2kC\",\n" +
						  "    \"promo_code\": \"vr8rqbxhi\",\n" +
						  "    \"expiration_date\": \"\",\n" +
						  "    \"title\": \"vr8rqbxhi\",\n" +
						  "    \"alias_promo_code\": \"vr8rqbxhi\",\n" +
						  "    \"href\": \"https://mc.adobe.io/resideo-mkt-stage1/campaign/customResources/cusCoupon_codes/@j0EolflNeHge9EZrq0SpCqybvr32D8ciux5Z1Nz7-kuCro1uJGkLbqHySpPtYpMe9e3ggcxuOcbzwy_uzT9qvJ33S3XYu-6bJh_k42jY9tKe-2kC\",\n" +
						  "    \"sku\": \"\",\n" +
						  "    \"first_name\": \"Charles\",\n" +
						  "    \"product\": \"FreeFilter\",\n" +
						  "    \"email_type\": \"\",\n" +
						  "    \"item_desc\": \"\",\n" +
						  "    \"item_id\": \"\",\n" +
						  "    \"created\": \"2021-04-28 09:26:38.840Z\",\n" +
						  "    \"ship_email\": \"\",\n" +
						  "    \"delivery_label\": \"\",\n" +
						  "    \"last_name\": \"Berger\",\n" +
						  "    \"billing_code\": \"\",\n" +
						  "    \"redeemed_date\": \"2021-05-10\",\n" +
						  "    \"customerID\": \"charlesb@yesmail.com\",\n" +
						  "    \"order_source\": \"\",\n" +
						  "    \"order_total\": \"0\",\n" +
						  "    \"lastModified\": \"2021-05-10 13:41:45.525Z\",\n" +
						  "    \"coupon_created\": \"\",\n" +
						  "    \"ship_date\": \"\",\n" +
						  "    \"status\": \"R\"\n" +
						  "}";
		CustomResourceRecord result = objectMapper.readValue(json, CustomResourceRecord.class);
		assertNotNull(result, "result not null");
		Map<String, Object> values = result.getValues();
		assertNotNull(values, "values map is not null");
		assertFalse(values.isEmpty(), "values is not empty");
		assertEquals("vr8rqbxhi", values.get("promo_code"), "promo_code");
		assertTrue(values.get("redeemed_date") instanceof LocalDate, "redeemed_date is LocalDate");
		assertTrue(values.get("lastModified") instanceof LocalDateTime, "lastModified is LocalDateTime");
		assertEquals(0, (Integer) values.get("order_total"));
	}

	@Test
	void array_unsupported() {
		String json = "{\"foo\": [\"bar\"]}";
		assertThrows(UnsupportedNodeTypeException.class, () -> objectMapper.readValue(json, CustomResourceRecord.class));
	}

	@ParameterizedTest(name = "{index} => {0}")
	@DisplayName("Deserialize numeric to string")
	@MethodSource
	void deserializeWithParameters(String name, CustomResourceParameters parameters, String jsonStr, String fieldName,
								   String expectedZipcode) throws JsonProcessingException {
		ObjectMapper mapper = CustomResourceRecordDeserializerFactory.getMapper(objectMapper, parameters);
		CustomResourceRecord result = mapper.readValue(jsonStr, CustomResourceRecord.class);
		assertNotNull(result, "result not null");
		Map<String, Object> values = result.getValues();
		assertNotNull(values, "values map is not null");
		assertFalse(values.isEmpty(), "values is not empty");
		Optional<Object> value = result.getValue(fieldName);
		assertTrue(value.isPresent(), String.format("Expected to find field %s but not present", fieldName));
		assertTrue(value.get() instanceof String, String.format("Field %s expected to be a String", fieldName));
		assertEquals(expectedZipcode, value.get());
	}

	public static Stream<Arguments> deserializeWithParameters() {
		CustomResourceParameters cusNpOrganizations = CustomResourceParameters.builder().name("cusNpOrganizations")
			.numberAsString("zipcode").company("boscovs").build();
		CustomResourceParameters cusClosestStore = CustomResourceParameters.builder().name("cusClosestStore")
			.numberAsString("zipcode").company("boscovs").build();
		CustomResourceParameters cusRegistration = CustomResourceParameters.builder().name("cusRegistration")
			.numberAsString("NP_ZIP_CD").company("boscovs").build();
		return Stream.of(
			arguments(
				"NP Org - 08757",
				cusNpOrganizations,
				"{\"orgName\":\"DEBORAH HOSPITAL\",\"address2\":\"\",\"city\":\"TOMS RIVER\",\"address1\":\"113 SEAPORT BLVD\",\"created\":\"2021-09-08 13:49:11.355Z\",\"acsId\":\"0def41f0-5fca-454a-a468-768510d8556e\",\"PKey\":\"@A5Ovrea4ntWUC75w7ZxDpF9Qzj8zpkjYgvhf4P5H2RO8MWBaIy8jtV46K2E3268Nay_S0g7I9U9MszmEC5-tUKILWhk\",\"title\":\"4369\",\"npID\":\"4369\",\"zipcode\":\"08757\",\"stateCode\":\"NJ\",\"href\":\"https://mc.adobe.io/boscovsdepartmentstorellc-mkt-stage1/campaign/customResources/cusNpOrganizations/@A5Ovrea4ntWUC75w7ZxDpF9Qzj8zpkjYgvhf4P5H2RO8MWBaIy8jtV46K2E3268Nay_S0g7I9U9MszmEC5-tUKILWhk\",\"lastModified\":\"2021-09-08 13:49:11.355Z\"}",
				"zipcode", "08757"
			),
			arguments(
				"NP Org - 26003",
				cusNpOrganizations,
				"{\"orgName\":\"AIDS TASK FORCE\",\"address2\":\"\",\"city\":\"WHEELING\",\"address1\":\"P.O. BOX 6360\",\"created\":\"2021-09-08 13:49:11.355Z\",\"acsId\":\"67b9a091-aeeb-406b-83da-ceea861e5d6f\",\"PKey\":\"@A5Ovrea4ntWUC75w7ZxDpEaf474wB9k5citu1BrY-LbIK58G1uDznejqHR1ZQGHcMEzRwLT-mtUCUh6mu-B4yqfZLhs\",\"title\":\"291\",\"npID\":\"291\",\"zipcode\":\"26003\",\"stateCode\":\"WV\",\"href\":\"https://mc.adobe.io/boscovsdepartmentstorellc-mkt-stage1/campaign/customResources/cusNpOrganizations/@A5Ovrea4ntWUC75w7ZxDpEaf474wB9k5citu1BrY-LbIK58G1uDznejqHR1ZQGHcMEzRwLT-mtUCUh6mu-B4yqfZLhs\",\"lastModified\":\"2021-09-08 13:49:11.355Z\"},{\"orgName\":\"BETHLEHEM ELEMENTARY SCHOOL\",\"address2\":\"\",\"city\":\"WHEELING\",\"address1\":\"22 CHAPEL RD\",\"created\":\"2021-09-08 13:49:11.355Z\",\"acsId\":\"cd5bb9b6-918e-4d08-acfa-7a9de2f6d301\",\"PKey\":\"@A5Ovrea4ntWUC75w7ZxDpA8JtO0lSKLIXFwOYwN2NM9-mPvz15JmUotckkFTXwZSFMu8_hfvzrRSCdWBjj5HPjNhF4Q\",\"title\":\"1672\",\"npID\":\"1672\",\"zipcode\":\"26003\",\"stateCode\":\"WV\",\"href\":\"https://mc.adobe.io/boscovsdepartmentstorellc-mkt-stage1/campaign/customResources/cusNpOrganizations/@A5Ovrea4ntWUC75w7ZxDpA8JtO0lSKLIXFwOYwN2NM9-mPvz15JmUotckkFTXwZSFMu8_hfvzrRSCdWBjj5HPjNhF4Q\",\"lastModified\":\"2021-09-08 13:49:11.355Z\"}",
				"zipcode", "26003"
			),
			arguments(
				"Closest Store - 08757",
				cusClosestStore,
				"{\"zipcode\":\"08757\",\"closestStoreName\":\"Toms River - Store 74\",\"storeNumber\":\"74\",\"created\":\"2022-03-04 17:16:43.262Z\",\"acsId\":\"58d93940-7f8f-4526-83b6-ae1626322ab2\",\"PKey\":\"@DghnApqcBY02iqVvCpV_qTIfqybXOD0sgb-8qkwGmaLt3ONOGhXlk03h2-rZDcRXpR4FpCNlYlAesQ-LQR0u2_VDxMo\",\"href\":\"https://mc.adobe.io/boscovsdepartmentstorellc-mkt-stage1/campaign/customResources/cusClosestStore/@DghnApqcBY02iqVvCpV_qTIfqybXOD0sgb-8qkwGmaLt3ONOGhXlk03h2-rZDcRXpR4FpCNlYlAesQ-LQR0u2_VDxMo\",\"lastModified\":\"2022-03-04 17:16:43.262Z\",\"title\":\"08757\"}",
				"zipcode", "08757"
			),
			arguments(
				"Registration - 08757",
				cusRegistration,
				"{\"PICKUP_STOREID\":\"74\",\"PHONE\":\"+16312832118\",\"NP_ID\":\"5684\",\"acsId\":\"4f0c3849-b2ef-41f2-85eb-3e5c45978e30\",\"PKey\":\"@s5QJImkuL5z0bNRS3i7DEdlpKDOm_081Jlro-awR6r6TEz1bxMwojtZxbgEten628YU0_RS8bWJFp4-8-vfQtJ5b5_A\",\"EXPORTED\":\"\",\"EMAIL\":\"charlesb@yesmail.com\",\"LAST_NAME\":\"Axle\",\"NP_ORG_NM\":\"FIRST UNITED METHODIST CHURCH\",\"title\":\"807,971\",\"PASS_COUNT\":\"400\",\"FIRST_NAME\":\"Data\",\"CREATED\":\"2022-04-19 18:41:25.373Z\",\"NP_STATE_CD\":\"NJ\",\"NP_CITY_NM\":\"TOMS RIVER\",\"NP_ADDR2\":\"\",\"NP_ZIP_CD\":\"08757\",\"href\":\"https://mc.adobe.io/boscovsdepartmentstorellc-mkt-stage1/campaign/customResources/cusRegistration/@s5QJImkuL5z0bNRS3i7DEdlpKDOm_081Jlro-awR6r6TEz1bxMwojtZxbgEten628YU0_RS8bWJFp4-8-vfQtJ5b5_A\",\"NP_ADDR1\":\"40 NORTHUMBERLAND DR\",\"SENT\":\"false\"}",
				"NP_ZIP_CD", "08757"
			)
		);
	}
}
