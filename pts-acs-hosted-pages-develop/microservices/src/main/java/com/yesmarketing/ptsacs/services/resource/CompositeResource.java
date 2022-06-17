package com.yesmarketing.ptsacs.services.resource;

import com.yesmarketing.ptsacs.services.authentication.CurrentForm;
import com.yesmarketing.ptsacs.services.dto.CompositeServiceDto;
import com.yesmarketing.ptsacs.services.dto.CompositeServiceResponseDto;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.service.CompositeService;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/services/composite")
public class CompositeResource {

	private final ServicesModelMapperHelper servicesModelMapperHelper;
	private final CompositeService compositeService;

	@Autowired
	public CompositeResource(CompositeService compositeService, ServicesModelMapperHelper servicesModelMapperHelper) {
		this.compositeService = compositeService;
		this.servicesModelMapperHelper = servicesModelMapperHelper;
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_COMPOSITE_SVC_NM + "')")
	public CompositeServiceResponseDto updateOrCreate(
			@RequestBody CompositeServiceDto compositeServiceDto,
			@CurrentForm FormConfig formConfig
	) {
		compositeService.validateUpdateOrCreate(compositeServiceDto, formConfig);

		return compositeService.updateOrCreate(compositeServiceDto, formConfig);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value = "/{customerIdHash}", consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_COMPOSITE_SVC_NM + "')")
	public CompositeServiceResponseDto updateByHash(
			@RequestBody CompositeServiceDto compositeServiceDto,
			@PathVariable(name = "customerIdHash") String customerIdHash,
			@CurrentForm FormConfig formConfig
	) {
		compositeService.validateUpdateByHash(compositeServiceDto, customerIdHash, formConfig);

		return compositeService.updateByHash(compositeServiceDto, customerIdHash, formConfig);
	}

}
