package integrationtests.testdata.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import integrationtests.testdata.RequestUtils;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormConfigTestDataHelper {

	private static final Logger LOG = LoggerFactory.getLogger(FormConfigTestDataHelper.class);

	private static final LocalDateTime now = LocalDateTime.now();

	private static final Map<String, String> tokenStateMap = new HashMap<>();

	private static final Table<String, String, String> companyForms = HashBasedTable.create();

	private static final Multimap<String, String> formsToEnable = MultimapBuilder.treeKeys().arrayListValues().build();

	public static void createData() {
		List<Map<String, Object>> forms = toCreate();
		forms.forEach(form -> {
			String company = (String) form.get("company");
			form.remove("company");
			String uri = String.format("/formConfigs/%s", company);
			String formStr = RequestUtils.post(uri, form);
			processForm(formStr);
		});
		JsonWebTokenRepository.addToken("is invalid", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ5bW5ld3NvbHV0aW9ucyIsInV1aWQiOiJlZDFlZTY2Yi1kNzk4LTQwYzAtOTRlZS0zOWEzNWNjYzg5YzcifSwibmJmIjoxNTkxNzQ3MjAwLCJleHAiOjE2MDk0NTkxOTksImlhdCI6MTU5OTc1NTAzNywianRpIjoiODUxYjQwNzUtYWIyMi00YzJmLTkxNzgtODBiZGUyYThiYjQ4In0.KEoHk5cCbn2sSxwRUo-89Yq_CTlaWDrxTEsV6Pn-q42kMQffH2F4VTp4uLH_5pxTL7oBPqA1er-lV96SPPp6WgX");
		JsonWebTokenRepository.addToken("uses an unknown Form UUID", "eyJhbGciOiJIUzUxMiJ9.eyJmb3JtIjp7ImNvbXBhbnkiOiJ1bmtub3duY29tcGFueSIsInV1aWQiOiJmM2Q1ZWQ4NC0yODcyLTQwODctYjBiNi0xOThiYTg1NGY2YjgifSwibmJmIjoxNTkxODMwMDAwLCJleHAiOjQxMDIzNTg0MDAsImlhdCI6MTYwMDE3OTMxNSwianRpIjoiYjU2NjE1MTEtNGY5Yi00ZjgyLWIwNWYtMTI2NTQxYmNjNTkyIn0.0Lj7BAIQFWFsDduGZldWaDWXtzHZzU_eUOMAFlrbl-Ydn6ObHkQyXvI4FZiuble2ZaMTpIQ6vu9fJzfyX8o-Gg");
		JsonWebTokenRepository.addToken("has mismatched FormConfig and Token UUIDs", tokenIdMismatch_jwt());
	}

	private static void enableForm(String company, String uuid) {
		String uri = String.format("/formConfigs/%s/form/%s/enabled", company, uuid);
		Map<String, Object> payload = new HashMap<>();
		payload.put("enabled", Boolean.TRUE);
		RequestUtils.post(uri, payload);
	}

	public static void deleteData() {
		companyForms.cellSet().forEach(entry -> RequestUtils.delete(
			String.format("/formConfigs/%s/form/%s", entry.getRowKey(), entry.getValue())));
	}

	private static List<Map<String, Object>> toCreate() {
		return Stream.of(
			ymnewsolutions_invalidTokens(),
			ymnewsolutions_disabledForm(),
			ymnewsolutions_notEffective(),
			ymnewsolutions_expiredForm(),
			ymnewsolutions_invalidDomain(),
			ymnewsolutions_incorrectRole(),
			ymnewsolutions_validForm(),
			ymnewsolutions_tokenIdMismatch(),
			unknownCompany()
		).collect(Collectors.toList());
	}

	private static Map<String, Object> ymnewsolutions_validForm() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "VALIDFORM");
		form.put("description", "Valid Form config.");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_validTokens_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_set_authorities() {
		Map<String, Object> getProfileBySecureLink = new HashMap<>();
		getProfileBySecureLink.put("authority", "GET_PROFILE_BY_SECURE_LINK");
		getProfileBySecureLink.put("description", "Get Profile by Secure Link");

		Map<String, Object> updateProfile = new HashMap<>();
		updateProfile.put("authority", "UPDATE_PROFILE");
		updateProfile.put("description", "Update Profile");

		Map<String, Object> createProfile = new HashMap<>();
		createProfile.put("authority", "CREATE_PROFILE");
		createProfile.put("description", "Create Profile");

		Map<String, Object> getProfileByCustomerId = new HashMap<>();
		getProfileByCustomerId.put("authority", "GET_PROFILE_BY_CUSTOMERID");
		getProfileByCustomerId.put("description", "Get Profile by CustomerId");

		Map<String, Object> addService = new HashMap<>();
		addService.put("authority", "ADD_SERVICE");
		addService.put("description", "Add Service");

		Map<String, Object> removeService = new HashMap<>();
		removeService.put("authority", "REMOVE_SERVICE");
		removeService.put("description", "Remove Service");

		Map<String, Object> triggerEmail = new HashMap<>();
		triggerEmail.put("authority", "TRIGGER_EMAIL");
		triggerEmail.put("description", "Trigger Email");

		Map<String, Object> compositeService = new HashMap<>();
		compositeService.put("authority", "COMPOSITE_SERVICE");
		compositeService.put("description", "Composite Service");

		return Stream.of(getProfileBySecureLink, updateProfile, createProfile, getProfileByCustomerId,
				addService, removeService, triggerEmail, compositeService).collect(Collectors.toList());
	}

	private static List<Map<String, Object>> ymnewsolutions_validTokens_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusMonths(2L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "is valid");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> ymnewsolutions_invalidTokens() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "INVALIDTOKENS");
		form.put("description", "Form config for invalid JWT tests.");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_invalidTokens_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_invalidTokens_jwts() {
		Map<String, Object> disabledJwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		disabledJwt.put("tokenId", tokenId);
		disabledJwt.put("notBefore", now.minusMonths(2L));
		disabledJwt.put("expirationTime", now.plusMonths(2L));
		disabledJwt.put("enabled", Boolean.FALSE);
		tokenStateMap.put(tokenId, "is disabled");
		Map<String, Object> isNotYetEffective = new HashMap<>();
		tokenId = UUID.randomUUID().toString();
		isNotYetEffective.put("tokenId", tokenId);
		isNotYetEffective.put("notBefore", now.plusDays(1L));
		isNotYetEffective.put("expirationTime", now.plusMonths(2L));
		isNotYetEffective.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "is not yet effective");
		Map<String, Object> isExpired = new HashMap<>();
		tokenId = UUID.randomUUID().toString();
		isExpired.put("tokenId", tokenId);
		isExpired.put("notBefore", now.minusMonths(3L));
		isExpired.put("expirationTime", now.minusDays(1L));
		isExpired.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "is expired");
		return Stream.of(
			disabledJwt,
			isNotYetEffective,
			isExpired
		).collect(Collectors.toList());
	}

	private static Map<String, Object> ymnewsolutions_disabledForm() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "DISABLEDFORM");
		form.put("description", "Form config is disabled.");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_disabledForm_jwts());
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_disabledForm_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusMonths(2L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "uses a disabled Form UUID");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> ymnewsolutions_notEffective() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "NOTEFFECTIVEYET");
		form.put("description", "Form config that is not effective yet.");
		form.put("effectiveFrom", now.plusDays(1L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_notEffective_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_notEffective_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusDays(2L));
		jwt.put("expirationTime", now.plusMonths(3L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "uses a FormConfig Effective From in the future");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> ymnewsolutions_expiredForm() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "EXPIREDFORM");
		form.put("description", "Form config that is expired.");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.minusDays(1L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_expiredForm_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_expiredForm_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusDays(1L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "uses a FormConfig Effective To in the past");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> ymnewsolutions_invalidDomain() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "INVALIDDOMAIN");
		form.put("description", "Form config that does not include the request domain");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(2L));
		form.put("domains", Collections.singletonList("pages.ymnewsolutions.com"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_invalidDomain_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_invalidDomain_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusMonths(2L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "has a FormConfig which does not include the request domain");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> ymnewsolutions_incorrectRole() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "INCORRECTROLE");
		form.put("description", "Form config that does not have the required role.");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(2L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_incorrectRole_authorities());
		form.put("jwts", ymnewsolutions_incorrectRole_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_incorrectRole_authorities() {
		Map<String, Object> getProfileBySecureLink = new HashMap<>();
		getProfileBySecureLink.put("authority", "GET_PROFILE_BY_CUSTOMERID");
		getProfileBySecureLink.put("description", "Get Profile by CustomerId");
		return Stream.of(getProfileBySecureLink).collect(Collectors.toList());
	}

	private static List<Map<String, Object>> ymnewsolutions_incorrectRole_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusDays(1L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "has a FormConfig that does not include the role for the requested service");
		return Collections.singletonList(jwt);
	}

	private static Map<String, Object> unknownCompany() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "UNKNOWNCOMPANYINJWT");
		form.put("description", "JWT company does not match Form Config");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", Collections.emptyList());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static Map<String, Object> ymnewsolutions_tokenIdMismatch() {
		Map<String, Object> form = new HashMap<>();
		form.put("company", "ymnewsolutions");
		form.put("code", "TOKENIDMISMATCH");
		form.put("description", "Token Id on JWT does not belong to Form Config");
		form.put("effectiveFrom", now.minusMonths(3L));
		form.put("effectiveTo", now.plusMonths(3L));
		form.put("domains", Collections.singletonList("localhost"));
		form.put("authorities", ymnewsolutions_set_authorities());
		form.put("jwts", ymnewsolutions_tokenIdMismatch_jwts());
		formsToEnable.put((String) form.get("company"), (String) form.get("code"));
		return form;
	}

	private static List<Map<String, Object>> ymnewsolutions_tokenIdMismatch_jwts() {
		Map<String, Object> jwt = new HashMap<>();
		String tokenId = UUID.randomUUID().toString();
		jwt.put("tokenId", tokenId);
		jwt.put("notBefore", now.minusMonths(2L));
		jwt.put("expirationTime", now.plusMonths(2L));
		jwt.put("enabled", Boolean.TRUE);
		tokenStateMap.put(tokenId, "is valid");
		return Collections.singletonList(jwt);
	}

	private static void processForm(String formStr) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode formNode = mapper.readTree(formStr);
			String company = formNode.get("company").asText();
			String uuid = formNode.get("uuid").asText();
			String code = formNode.get("code").asText();
			if (code.equals("UNKNOWNCOMPANYINJWT")) {
				formStr = unknownCompanyInJwt(formNode);
				formNode = mapper.readTree(formStr);
				company = formNode.get("company").asText();
				uuid = formNode.get("uuid").asText();
			}
			if (formsToEnable.containsEntry(company, code)) {
				enableForm(company, uuid);
			}
			companyForms.put(company, code, uuid);
			JsonNode jwtsNode = formNode.get("jwts");
			for (JsonNode jwtNode : jwtsNode) {
				String tokenUuid = jwtNode.get("tokenId").asText();
				String token = jwtNode.get("token").asText();
				String tokenState = tokenStateMap.get(tokenUuid);
				JsonWebTokenRepository.addToken(tokenState, token);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private static String unknownCompanyInJwt(JsonNode formNode) throws JsonProcessingException {
		// generate a JWT with form UUID and incorrect company name
		Map<String, Object> claim = new HashMap<>();
		claim.put("company", "unknowncompany");
		String formUuid = formNode.get("uuid").asText();
		claim.put("uuid", formUuid);
		Map<String, Object> tokenDetail = new HashMap<>();
		tokenDetail.put("claim", claim);
		//String notBefore = now.minusMonths(2L).toString();
		//String expirationTime = now.plusMonths(2L).toString();
		LocalDateTime notBefore = now.minusMonths(2L);
		LocalDateTime expirationTime = now.minusMonths(2L);

		tokenDetail.put("notBefore", notBefore);
		tokenDetail.put("expirationTime", expirationTime);
		String tokenId = UUID.randomUUID().toString();
		tokenDetail.put("jti", tokenId);
		String tokenDtoStr = RequestUtils.post("/formConfigs/token/generate", tokenDetail);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode tokenNode = objectMapper.readTree(tokenDtoStr);
		String jwtStr = tokenNode.get("token").asText();
		// add JWT to Form Config
		Map<String, Object> formConfig = new HashMap<>();
		formConfig.put("code", formNode.get("code").asText());
		formConfig.put("description", formNode.get("description").asText());
		formConfig.put("effectiveTo", formNode.get("effectiveTo").asText());
		formConfig.put("effectiveFrom", formNode.get("effectiveTo").asText());
		JsonNode domainsNode = formNode.get("domains");
		List<String> domainsList = new ArrayList<>();
		for (JsonNode domain : domainsNode) {
			domainsList.add(domain.asText());
		}
		formConfig.put("domains", domainsList);
		JsonNode authoritiesNode = formNode.get("authorities");
		List<Map<String, Object>> authoritiesList = new ArrayList<>();
		for (JsonNode authorityNode : authoritiesNode) {
			String authority = authorityNode.get("authority").asText();
			String description = authorityNode.get("description").asText();
			Map<String, Object> authorityMap = new HashMap<>();
			authorityMap.put("authority", authority);
			authorityMap.put("description", description);
			authoritiesList.add(authorityMap);
		}
		formConfig.put("authorities", authoritiesList);
		Map<String, Object> jwtMap = new HashMap<>();
		jwtMap.put("tokenId", tokenId);
		jwtMap.put("notBefore", notBefore);
		jwtMap.put("expirationTime", expirationTime);
		jwtMap.put("enabled", Boolean.TRUE);
		jwtMap.put("token", jwtStr);
		formConfig.put("jwts", Collections.singletonList(jwtMap));
		tokenStateMap.put(tokenId, "uses an incorrect company");
		String company = formNode.get("company").asText();
		return RequestUtils.put(String.format("/formConfigs/%s/form/%s", company, formUuid), formConfig);
	}

	private static String tokenIdMismatch_jwt() {
		String company = "ymnewsolutions";
		String uuid = companyForms.get(company, "TOKENIDMISMATCH");
		Map<String, Object> claim = new HashMap<>();
		claim.put("company", company);
		claim.put("uuid", uuid);
		Map<String, Object> tokenDetail = new HashMap<>();
		tokenDetail.put("claim", claim);
		LocalDateTime notBefore = now.minusMonths(2L);
		LocalDateTime expirationTime = now.plusMonths(2L);
		tokenDetail.put("notBefore", notBefore);
		tokenDetail.put("expirationTime", expirationTime);
		tokenDetail.put("jti", UUID.randomUUID().toString());
		String tokenDtoStr = RequestUtils.post("/formConfigs/token/generate", tokenDetail);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode tokenNode = null;
		try {
			tokenNode = objectMapper.readTree(tokenDtoStr);
			return tokenNode.get("token").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Error parsing token string!";
		}
	}

}
