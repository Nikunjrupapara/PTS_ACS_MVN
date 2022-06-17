package com.yesmarketing.ptsacs.admin.resource;

import com.yesmarketing.ptsacs.admin.dto.CustomerIdConfigDto;
import com.yesmarketing.ptsacs.admin.service.CustomerIdDefinitionService;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.model.CustomerIdDefinition;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController("companyIdConfigResource")
@RequestMapping(path = "/api/customerId")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@ResponseBody
public class CustomerIdConfigResource {

	private final CustomerIdDefinitionService customerIdDefinitionService;

	private final AdminModelMapperHelper adminModelMapperHelper;

	public CustomerIdConfigResource(CustomerIdDefinitionService customerIdDefinitionService, AdminModelMapperHelper adminModelMapperHelper) {
		this.customerIdDefinitionService = customerIdDefinitionService;
		this.adminModelMapperHelper = adminModelMapperHelper;
	}

	@GetMapping(path = "/{company}", produces = { MediaType.APPLICATION_JSON_VALUE})
	CustomerIdConfigDto getConfig(@PathVariable String company) {
		return adminModelMapperHelper.toDto(customerIdDefinitionService.get(company), CustomerIdConfigDto.class);
	}

	@PutMapping(path = "/{company}", consumes = { MediaType.APPLICATION_JSON_VALUE},
		produces = { MediaType.APPLICATION_JSON_VALUE })
	CustomerIdConfigDto createOrUpdateConfig(@PathVariable String company, @RequestBody CustomerIdConfigDto dto) {
		CustomerIdDefinition customerIdDefinitionIn = adminModelMapperHelper.fromDto(dto, CustomerIdDefinition.class);
		customerIdDefinitionIn.setCompany(company);
		return adminModelMapperHelper.toDto(customerIdDefinitionService.save(customerIdDefinitionIn),
			CustomerIdConfigDto.class);
	}
}
