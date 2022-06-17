package com.yesmarketing.ptsacs.services.util;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.ptsacs.admin.dto.ServicesGrantedAuthorityDto;
import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.model.FormJsonWebToken;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FormConfigTestHelper {

	private static final Map<String, FormConfig> formConfigs = new HashMap<>();

	private static final LocalDateTime now = LocalDateTime.now();

	static  {
		addConfig(validConfig());
		addConfig(disabledConfig());
		addConfig(notStartedConfig());
		addConfig(expiredConfig());
		addConfig(invalidDomainConfig());
		addConfig(invalidRole());
		addConfig(unknownCompany());
	}

	private static FormConfig validConfig() {
		return new FormConfig("bd5a824d-e5f9-4dcb-b3d2-a3be44a4ada0",
			"ymnewsolutions", "VALIDTESTFORM", "A valid test form", now.minusMonths(1L),
			now.plusMonths(1L), Collections.singletonList("localhost"), ServicesConstants.SERVICES_AUTHORITIES,
			getJwts("ebaef1ce-4662-46db-a108-d96132b5b62f"), true, LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig disabledConfig() {
		return new FormConfig("cb25d1b5-745c-4aa7-9e90-54d39f753a55", "ymnewsolutions", "DISABLEDFORM",
			"A disabled form config", now.minusMonths(1L), now.plusMonths(1L), Collections.singletonList("localhost"),
			ServicesConstants.SERVICES_AUTHORITIES, getJwts("9ccffa0b-80a6-4daa-9938-7f39ebe978d3"), false,
				LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig notStartedConfig() {
		return new FormConfig("e88f1a37-1657-4d93-97f4-dbd9b320a42b", "ymnewsolutions", "NOTSTARTEDFORM",
			"Form not yet valid", now.plusMonths(1L), now.plusMonths(3L), Collections.singletonList("localhost"),
			ServicesConstants.SERVICES_AUTHORITIES, getJwts("759590d9-b522-4191-a8f3-d5d3c6d3fbee"), true,
				LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig expiredConfig() {
		return new FormConfig("b6b4d357-1486-4d7d-b0a4-299bb1141e09", "ymnewsolutions", "EXPIREDFORM",
			"Form expired", now.minusMonths(6L), now.minusMonths(1L), Collections.singletonList("localhost"),
			ServicesConstants.SERVICES_AUTHORITIES, getJwts("1dddafa9-8076-4540-8755-86ffc4f24485"), true,
				LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig invalidDomainConfig() {
		return new FormConfig("5a903f1b-916b-4d78-94c6-913eb165f6de", "ymnewsolutions", "INVALIDDOMAINFORM",
			"Form not yet valid", now.minusMonths(1L), now.plusMonths(3L), Collections.singletonList("mydomain.com"),
			ServicesConstants.SERVICES_AUTHORITIES, getJwts("e15df971-8061-4eb5-b9d4-c88d7ceeee9f"), true,
				LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig invalidRole() {
		return new FormConfig("0aca349b-699c-4852-a468-300f54705da9",
				"ymnewsolutions", "INVALIDROLE", "A form with the incorrect role", now.minusMonths(1L),
				now.plusMonths(1L), Collections.singletonList("localhost"), getAuthorities(ServicesConstants.GA_GET_PROFILE_CUSTOMERID_NM),
				getJwts("b5661511-4f9b-4f82-b05f-126541bcc592"), true, LocalDateTime.now(), LocalDateTime.now());
	}

	private static FormConfig unknownCompany() {
		return new FormConfig("c25addd3-1c9c-40c9-bd3e-74f6bf3a655d",
				"ymnewsolutions", "UNKNOWNCOMPANY", "JWT company does not match Form Config",
				now.minusMonths(1L), now.plusMonths(1L), Collections.singletonList("localhost"),
				ServicesConstants.SERVICES_AUTHORITIES, getJwts("f9c1783b-8ded-4ce6-91ad-c44d9b5151ca"),
				true, LocalDateTime.now(), LocalDateTime.now());
	}

	private static List<ServicesGrantedAuthority> getAuthorities(String authorityStr) {
		return Collections.singletonList(ServicesConstants.SERVICES_AUTHORITIES.stream()
				.filter(authority -> authority.getAuthority().equals(authorityStr))
				.findFirst()
				.orElseThrow(ObjectNotFoundException::new));
	}

	private static void addConfig(FormConfig formConfig) {
		formConfigs.put(formConfig.getUuid(), formConfig);
	}

	public static Optional<FormConfig> getByUuid(String uuid) {
		if (formConfigs.containsKey(uuid)) {
			return Optional.of(formConfigs.get(uuid));
		}
		return Optional.empty();
	}

	public static Optional<FormConfig> getByCompanyAndCode(String company, String code) {
		return formConfigs.values().stream()
				.filter(formConfig -> formConfig.getCompany().equals(company) && formConfig.getCode().equals(code))
				.findFirst();
	}

	private static List<FormJsonWebToken> getJwts(String uuid) {
		return Collections.singletonList(new FormJsonWebToken(uuid, now.minusMonths(1L), now.plusMonths(1L),
				"token", true, LocalDateTime.now(), LocalDateTime.now()));
	}

	public static ServicesGrantedAuthorityDto getAuthorityDto(String authorityStr) {
		ServicesGrantedAuthority authority = ServicesConstants.SERVICES_AUTHORITIES.stream()
												 .filter(item -> item.getAuthority().equals(authorityStr))
												 .findFirst()
												 .orElse(null);
		if (authority == null) { return null; }
		ServicesGrantedAuthorityDto dto = new ServicesGrantedAuthorityDto();
		dto.setAuthority(authority.getAuthority());
		dto.setDescription(authority.getDescription());
		return dto;
	}

	private FormConfigTestHelper() {

	}
}
