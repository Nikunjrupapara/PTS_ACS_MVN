package com.yesmarketing.ptsacs.admin.resource;

import com.yesmarketing.ptsacs.admin.dto.FormClaimDto;
import com.yesmarketing.ptsacs.admin.dto.FormConfigCollectionDto;
import com.yesmarketing.ptsacs.admin.dto.FormConfigDto;
import com.yesmarketing.ptsacs.admin.dto.FormStatusDto;
import com.yesmarketing.ptsacs.admin.dto.ServicesGrantedAuthorityDto;
import com.yesmarketing.ptsacs.admin.dto.ServicesJwtDetailsDto;
import com.yesmarketing.ptsacs.admin.dto.ServicesTokenDto;
import com.yesmarketing.ptsacs.admin.dto.UpdateFormConfigDto;
import com.yesmarketing.ptsacs.admin.service.FormConfigService;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.service.AuthorityService;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import com.yesmarketing.ptsacs.services.service.impl.FormJwtTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController("formConfigResource")
@RequestMapping(path = "/api/formConfigs")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class FormConfigResource {

	@Autowired
	private AdminModelMapperHelper modelMapper;

	@Autowired
	private FormConfigService formConfigService;

	@Autowired
	private AuthorityService authorityService;

	@GetMapping(path = "/{company}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public FormConfigCollectionDto getFormConfigs(@PathVariable String company) {
		List<FormConfig> formConfigs = formConfigService.getForCompany(company);
		return modelMapper.toCollectionDto(company, formConfigs, FormConfigCollectionDto.SUPPLIER, FormConfigDto.class);
	}

	@GetMapping(path = "/{company}/form/{uuid}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public FormConfigDto getFormConfig(@PathVariable String company, @PathVariable String uuid) {
		return modelMapper.toDto(formConfigService.getFormConfig(uuid), FormConfigDto.class);
	}

	@PostMapping(path = "/{company}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public FormConfigDto createFormConfig(@PathVariable String company, @RequestBody UpdateFormConfigDto dto) {
		FormConfig formConfig = modelMapper.fromDto(dto, FormConfig.class);
		formConfig.setCompany(company);
		return modelMapper.toDto(formConfigService.createFormConfig(formConfig), FormConfigDto.class);
	}

	@PutMapping(path = "/{company}/form/{uuid}", consumes = { MediaType.APPLICATION_JSON_VALUE },
		produces = { MediaType.APPLICATION_JSON_VALUE })
	public FormConfigDto updateFormConfig(@PathVariable String company, @PathVariable String uuid,
									   @RequestBody UpdateFormConfigDto dto) {
		FormConfig formConfig = modelMapper.fromDto(dto, FormConfig.class);
		formConfig.setCompany(company);
		formConfig.setUuid(uuid);
		return modelMapper.toDto(formConfigService.updateFormConfig(formConfig), FormConfigDto.class);
	}

	@DeleteMapping(path = "/{company}/form/{uuid}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFormConfig(@PathVariable String company, @PathVariable String uuid) {
		formConfigService.delete(uuid);
	}

	@PostMapping(path = "/{company}/form/{uuid}/enabled", consumes = { MediaType.APPLICATION_JSON_VALUE},
		produces = { MediaType.APPLICATION_JSON_VALUE })
	public FormConfigDto updateFormConfigEnabled(@PathVariable String company, @PathVariable String uuid,
												 @RequestBody FormStatusDto formStatusDto) {
		return modelMapper.toDto(formConfigService.setFormStatus(uuid, formStatusDto.isEnabled()), FormConfigDto.class);
	}

	@GetMapping(path = "/authorities", produces = { MediaType.APPLICATION_JSON_VALUE})
	public List<ServicesGrantedAuthorityDto> getAuthorities() {
		return authorityService.getAuthorities().stream()
			.map(authority -> modelMapper.toDto(authority, ServicesGrantedAuthorityDto.class))
			.collect(Collectors.toList());
	}

	@PostMapping(path = "/token/generate", consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ServicesTokenDto generateFormJsonWebToken(@RequestBody ServicesJwtDetailsDto jwtDto) {
		FormClaimDto formClaimDto = jwtDto.getClaim();
		FormClaim formClaim = new FormClaim(formClaimDto.getCompany(), formClaimDto.getUuid());
		Date issuedAt = jwtDto.getIssuedAt();
		if (issuedAt == null) {
			issuedAt = new Date();
		}
		TokenDetail tokenDetail = new TokenDetail(formClaim, jwtDto.getNotBefore(),
				jwtDto.getExpirationTime(), issuedAt, jwtDto.getJti());
		String token;
		if (StringUtils.hasText(jwtDto.getOverrideKey())) {
			FormJwtTokenService tokenSvc = new FormJwtTokenServiceImpl(jwtDto.getOverrideKey());
			token = tokenSvc.createToken(tokenDetail);
		} else {
			token = formConfigService.generateToken(tokenDetail);
		}
		ServicesTokenDto tokenDto = new ServicesTokenDto();
		tokenDto.setToken(token);
		return tokenDto;
	}

	@PostMapping(path = "/token/validate", consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ServicesJwtDetailsDto validateFormJsonWebToken(@RequestBody ServicesTokenDto servicesTokenDto) {
		if (StringUtils.hasText(servicesTokenDto.getOverrideKey())) {
			FormJwtTokenService tokenSvc = new FormJwtTokenServiceImpl(servicesTokenDto.getOverrideKey());
			return modelMapper.toDto(tokenSvc.validateToken(servicesTokenDto.getToken()), ServicesJwtDetailsDto.class);
		}
		return modelMapper.toDto(formConfigService.validateToken(servicesTokenDto.getToken()), ServicesJwtDetailsDto.class);
	}
}
