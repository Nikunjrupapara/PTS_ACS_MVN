package com.yesmarketing.ptsacs.services.resource;

import com.yesmarketing.ptsacs.admin.dto.FormConfigDto;
import com.yesmarketing.ptsacs.admin.util.AdminModelMapperHelper;
import com.yesmarketing.ptsacs.services.authentication.CurrentForm;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/services/demo")
@Slf4j
public class DemoResource {

	private final AdminModelMapperHelper adminModelMapperHelper;

	public DemoResource(AdminModelMapperHelper adminModelMapperHelper) {
		this.adminModelMapperHelper = adminModelMapperHelper;
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_PROFILE_SECURE_LINK_NM + "')")
	public FormConfigDto getValue(@CurrentForm FormConfig formConfig) {
		LOG.info("formConfig={}", formConfig);
		return adminModelMapperHelper.toDto(formConfig, FormConfigDto.class);
	}

}
