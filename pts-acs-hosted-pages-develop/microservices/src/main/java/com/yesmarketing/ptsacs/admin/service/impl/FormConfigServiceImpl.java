package com.yesmarketing.ptsacs.admin.service.impl;

import com.yesmarketing.ptsacs.admin.service.FormConfigService;
import com.yesmarketing.ptsacs.common.exception.DuplicateFormConfigException;
import com.yesmarketing.ptsacs.common.exception.FormConfigNotFoundException;
import com.yesmarketing.ptsacs.common.repository.FormConfigRepository;
import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.model.FormJsonWebToken;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FormConfigServiceImpl implements FormConfigService {

	private final FormConfigRepository formConfigRepository;

	private final FormJwtTokenService tokenService;

	public FormConfigServiceImpl(FormConfigRepository formConfigRepository, FormJwtTokenService tokenService) {
		this.formConfigRepository = formConfigRepository;
		this.tokenService = tokenService;
	}

	@Override
	public FormConfig getFormConfig(FormClaim formClaim) {
		return getFormConfig(formClaim.getUuid());
	}

	@Override
	public FormConfig getFormConfig(String uuid) {
		return formConfigRepository.findByUuid(uuid)
				   .orElseThrow(() -> new FormConfigNotFoundException(uuid));
	}

	@Override
	public List<FormConfig> getForCompany(String company) {
		return formConfigRepository.findByCompany(company);
	}

	@Override
	public FormConfig createFormConfig(FormConfig formConfig) {
		String uuid = UUID.randomUUID().toString();
		formConfig.setUuid(uuid);
		formConfig.setCreated(LocalDateTime.now());
		formConfig.setLastUpdated(LocalDateTime.now());
		processJwts(formConfig);
		try {
			return formConfigRepository.save(formConfig);
		} catch (DuplicateKeyException e) {
			throw new DuplicateFormConfigException(formConfig.getCompany(), formConfig.getCode());
		}
	}

	@Override
	public FormConfig updateFormConfig(FormConfig formConfig) {
		FormConfig existing = formConfigRepository.findByUuid(formConfig.getUuid())
			.orElseThrow(() -> new FormConfigNotFoundException(formConfig.getUuid()));
		formConfig.setEnabled(existing.isEnabled());
		formConfig.setCreated(existing.getCreated());
		formConfig.setLastUpdated(LocalDateTime.now());
		processJwts(formConfig);
		try {
			return formConfigRepository.save(formConfig);
		} catch (DuplicateKeyException e) {
			throw new DuplicateFormConfigException(formConfig.getCompany(), formConfig.getCode());
		}
	}

	@Override
	public FormConfig setFormStatus(String uuid, boolean enabled) {
		FormConfig formConfig = formConfigRepository.findByUuid(uuid)
								  .orElseThrow(() -> new FormConfigNotFoundException(uuid));
		formConfig.setEnabled(enabled);
		formConfig.setLastUpdated(LocalDateTime.now());
		return formConfigRepository.save(formConfig);
	}

	@Override
	public void delete(String uuid) {
		FormConfig formConfig = formConfigRepository.findByUuid(uuid)
								  .orElseThrow(() -> new FormConfigNotFoundException(uuid));
		formConfigRepository.delete(formConfig);
	}

	@Override
	public String generateToken(TokenDetail tokenDetail) {
		return tokenService.createToken(tokenDetail);
	}

	@Override
	public TokenDetail validateToken(String token) {
		return tokenService.validateToken(token);
	}

	private void processJwts(FormConfig formConfig) {
		formConfig.getJwts().forEach(jwt -> {
			if (StringUtils.isEmpty(jwt.getTokenId())) {
				jwt.setTokenId(UUID.randomUUID().toString());
			}
			if (jwt.getCreated() == null) {
				jwt.setCreated(LocalDateTime.now());
			}
			jwt.setLastUpdated(LocalDateTime.now());
			if (StringUtils.isEmpty(jwt.getToken())) {
				jwt.setToken(generateToken(formConfig, jwt));
			}
		});
	}

	private String generateToken(FormConfig formConfig, FormJsonWebToken jwt) {
		LOG.info("Generating JWT for FormConfig: company: {}, uuid: {}", formConfig.getCompany(), formConfig.getUuid());
		FormClaim formClaim = new FormClaim(formConfig.getCompany(), formConfig.getUuid());
		TokenDetail tokenDetail = new TokenDetail(formClaim, jwt.getNotBefore(), jwt.getExpirationTime(),
			new Date(), jwt.getTokenId());
		return tokenService.createToken(tokenDetail);
	}
}
