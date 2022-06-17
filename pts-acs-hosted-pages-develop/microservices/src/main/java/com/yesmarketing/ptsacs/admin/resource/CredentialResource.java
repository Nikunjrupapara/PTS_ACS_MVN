package com.yesmarketing.ptsacs.admin.resource;

import com.yesmarketing.acsapi.auth.dto.CreateCredentialModelDto;
import com.yesmarketing.acsapi.auth.dto.CredentialModelDto;
import com.yesmarketing.acsapi.auth.dto.CredentialStatusDto;
import com.yesmarketing.acsapi.auth.dto.UpdateCredentialModelDto;
import com.yesmarketing.acsapi.auth.model.AccessToken;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.auth.service.CredentialManagementService;
import com.yesmarketing.ptsacs.admin.service.PtsCredentialManagementService;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController("credentialsResource")
@RequestMapping(path = "/api/credentials")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CredentialResource {

	@Autowired
	private AdminModelMapperHelper modelMapper;

	@Autowired
	private CredentialManagementService credentialManagementService;

	@Autowired
	private PtsCredentialManagementService ptsCredentialManagementService;

	@GetMapping(path = "/{company}", produces = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public CredentialModelDto getCredentialModel(@PathVariable String company) {
		CredentialModel credentialModel = credentialManagementService.getCredentialForCompany(company);
		return modelMapper.toDto(credentialModel, CredentialModelDto.class);
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },
		produces = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public CredentialModelDto createCredential(@RequestBody CreateCredentialModelDto credentialModelDto) {
		return modelMapper.toDto(credentialManagementService.createCredential(
			modelMapper.fromDto(credentialModelDto, CredentialModel.class)), CredentialModelDto.class);
	}

	@PutMapping(path = "/{company}", consumes = { MediaType.APPLICATION_JSON_VALUE },
		produces = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public CredentialModelDto updateCredential(@PathVariable String company,
											   @RequestBody UpdateCredentialModelDto credentialModelDto) {
		CredentialModel credentialModel = modelMapper.fromDto(credentialModelDto, CredentialModel.class);
		credentialModel.setCompany(company);
		return modelMapper.toDto(credentialManagementService.updateCredential(company, credentialModel),
			CredentialModelDto.class);
	}

	@DeleteMapping(path = "/{company}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCredential(@PathVariable String company) {
		ptsCredentialManagementService.delete(company);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Collection<CredentialModelDto> getCredentials() {
		Collection<CredentialModel> credentials = credentialManagementService.getCredentials();
		if (CollectionUtils.isEmpty(credentials)) {
			return Collections.emptyList();
		}
		return credentials
				   .stream()
				   .sorted(Comparator.comparing(CredentialModel::getCompany))
				   .map(credentialModel -> modelMapper.toDto(credentialModel, CredentialModelDto.class))
				   .collect(Collectors.toList());
	}

	@PostMapping(path ="/test")
	@ResponseBody
	public AccessToken testCredential (@RequestBody CredentialModelDto dto) {
		CredentialModel cm = modelMapper.fromDto(dto, CredentialModel.class);
		return credentialManagementService.testCredential(cm);
	}


	@PostMapping(path = "/{company}/enabled", consumes = { MediaType.APPLICATION_JSON_VALUE },
		produces = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public CredentialModelDto updateCredentialEnabled(@PathVariable String company, @RequestBody CredentialStatusDto credentialStatusDto) {
		CredentialModel credentialModel = credentialManagementService.setCredentialStatus(company, credentialStatusDto);
		return modelMapper.toDto(credentialModel, CredentialModelDto.class);
	}
}
