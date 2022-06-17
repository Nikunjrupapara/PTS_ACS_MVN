package com.yesmarketing.ptsacs.admin.dto;


import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.util.FormConfigTestHelper;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFormConfigMapping {

	private final AdminModelMapperHelper modelMapper = new AdminModelMapperHelper(new ModelMapper());

	@Test
	void updateFormConfigDto_to_FormConfig() {
		UpdateFormConfigDto dto = new UpdateFormConfigDto();
		dto.setAuthorities(Collections.singletonList(
			FormConfigTestHelper.getAuthorityDto(ServicesConstants.GA_CREATE_PROFILE_NM)));
		dto.setCode("TESTFORM");
		dto.setDescription("A test form");
		dto.setDomains(Collections.singletonList("email.ymnewsolutions.com"));
		dto.setEffectiveFrom(LocalDateTime.now());
		dto.setEffectiveTo(LocalDateTime.now().plusMonths(1L));
		dto.setJwts(getJwts());
		FormConfig form = modelMapper.fromDto(dto, FormConfig.class);
		assertNotNull(form);
		assertAll(() -> {
			assertEquals(1, form.getAuthorities().size());
			assertEquals(new ServicesGrantedAuthority(ServicesConstants.GA_CREATE_PROFILE_NM,
				ServicesConstants.GA_CREATE_PROFILE_DS), form.getAuthorities().get(0));
			assertNotNull(form.getCode());
			assertNull(form.getCompany());
			assertNull(form.getCreated());
			assertNotNull(form.getDescription());
			assertFalse(form.getDomains().isEmpty());
			assertNotNull(form.getEffectiveFrom());
			assertNotNull(form.getEffectiveTo());
			assertEquals(2, form.getJwts().size());
			assertNull(form.getLastUpdated());
			assertNull(form.getUuid());
		});
	}

	@Test
	void formConfig_to_FormConfigDto() {
		FormConfig form = FormConfigTestHelper.getByUuid("bd5a824d-e5f9-4dcb-b3d2-a3be44a4ada0")
			.orElseThrow(ObjectNotFoundException::new);
		FormConfigDto dto = modelMapper.toDto(form, FormConfigDto.class);
		assertNotNull(dto);
		assertAll(() -> {
			assertTrue(dto.getAuthorities().size() > 1);
			assertEquals(ServicesConstants.GA_COMPOSITE_SVC_NM, dto.getAuthorities().get(0).getAuthority());
			assertFalse(dto.getCode().isEmpty());
			assertNotNull(dto.getCreated());
			assertFalse(dto.getDescription().isEmpty());
			assertFalse(dto.getDomains().isEmpty());
			assertNotNull(dto.getEffectiveFrom());
			assertNotNull(dto.getEffectiveTo());
			assertFalse(dto.getJwts().isEmpty());
			assertNotNull(dto.getLastUpdated());
			assertNotNull(dto.getUuid());
		});
	}

	private List<FormJsonWebTokenDto> getJwts() {
		return Stream.of(
			getJwt(LocalDateTime.now(), true, LocalDateTime.now().plusMonths(1L), LocalDateTime.now(), LocalDateTime.now(),
				"token1"),
			getJwt(LocalDateTime.now().minus(Duration.of(100000L, ChronoUnit.MILLIS)), true, LocalDateTime.now().plusDays(50L), LocalDateTime.now(),
				LocalDateTime.now().minusMonths(1L), "token2")
		).collect(Collectors.toList());
	}

	private FormJsonWebTokenDto getJwt(LocalDateTime created, boolean enabled, LocalDateTime expirationTime, LocalDateTime lastUpdated,
									   LocalDateTime notBefore, String token) {
		FormJsonWebTokenDto dto = new FormJsonWebTokenDto();
		dto.setCreated(created);
		dto.setEnabled(enabled);
		dto.setExpirationTime(expirationTime);
		dto.setLastUpdated(lastUpdated);
		dto.setNotBefore(notBefore);
		dto.setToken(token);
		dto.setTokenId(UUID.randomUUID().toString());
		return dto;
	}
}
