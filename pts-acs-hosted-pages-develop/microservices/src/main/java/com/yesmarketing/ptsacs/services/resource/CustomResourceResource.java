package com.yesmarketing.ptsacs.services.resource;

import com.yesmarketing.acsapi.admin.model.CustomResourceRecord;
import com.yesmarketing.ptsacs.services.authentication.CurrentForm;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordCollectionDto;
import com.yesmarketing.ptsacs.services.dto.CustomResourceRecordDto;
import com.yesmarketing.ptsacs.services.dto.UpdateResourceDto;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.service.CustomResourceService;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/services/customResources")
public class CustomResourceResource {

	private static final Logger LOG = LoggerFactory.getLogger(CustomResourceResource.class);

	private final CustomResourceService resourcesService;
	private final ServicesModelMapperHelper servicesModelMapperHelper;

	@Autowired
	public CustomResourceResource(CustomResourceService resourcesService, ServicesModelMapperHelper servicesModelMapperHelper) {
		this.resourcesService = resourcesService;
		this.servicesModelMapperHelper = servicesModelMapperHelper;
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/listAssociated", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_METADATA_NM + "')")
	public List<String> listAssociated(@CurrentForm FormConfig formConfig
	) {
		return resourcesService.getAssociatedList(formConfig.getCompany());
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/listUnassociated", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_METADATA_NM + "')")
	public List<String> listUnassociated(@CurrentForm FormConfig formConfig
	) {
		return resourcesService.getUnassociatedList(formConfig.getCompany());
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/metadata", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_METADATA_NM + "')")
	public String getMetadata(@PathVariable("resource") String resource,
											  @CurrentForm FormConfig formConfig
	) {
		return resourcesService.getMetadataString(formConfig.getCompany(),resource);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/count", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_NM + "')")
	public String getCount(@PathVariable("resource") String resource,
							  @CurrentForm FormConfig formConfig
	) {
		return resourcesService.getCount(formConfig.getCompany(),resource);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/records/filter/{filter}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_FIND_RESOURCE_NM + "')")
	CustomResourceRecordCollectionDto getRecords(@PathVariable("resource") String resource,
												 @PathVariable("filter") String filter,
												 @RequestParam Map<String,String> allRequestParams,
												 @CurrentForm FormConfig formConfig
	) {
		return resourcesService.getRecords(formConfig.getCompany(),resource,filter,allRequestParams);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/records/pkey/{pKey}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_NM + "')")
	CustomResourceRecord getRecords(@PathVariable("resource") String resource,
										  @PathVariable("pKey") String pKey,
										  @CurrentForm FormConfig formConfig
	) {
		return resourcesService.getRecord(formConfig.getCompany(),resource,pKey);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value="/{resource}",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_CREATE_RESOURCE_NM + "')")
	CustomResourceRecord createRecords(@PathVariable("resource") String resource,
	 @RequestBody CustomResourceRecordDto customResourceRecordDto,
	 @CurrentForm FormConfig formConfig
	) {

		CustomResourceRecord customResourceRecord = servicesModelMapperHelper.fromDto(customResourceRecordDto,CustomResourceRecord.class);
		resourcesService.validateCreateRecord(formConfig.getCompany(),resource,customResourceRecord);
		return resourcesService.createRecord(formConfig.getCompany(),resource,customResourceRecord);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PutMapping(value="/{resource}/records/pkey/{pKey}",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_UPDATE_RESOURCE_NM + "')")
	UpdateResourceDto updateRecords(@PathVariable("resource") String resource,
									@RequestBody CustomResourceRecordDto customResourceRecordDto,
									@PathVariable("pKey") String pKey,
									@CurrentForm FormConfig formConfig
	) {
		CustomResourceRecord customResourceRecord = servicesModelMapperHelper.fromDto(customResourceRecordDto,CustomResourceRecord.class);
		return resourcesService.updateRecord(formConfig.getCompany(),resource,pKey,customResourceRecord);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@DeleteMapping(value="/{resource}/records/pkey/{pKey}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_REMOVE_RESOURCE_NM + "')")
	void deleteRecords(@PathVariable("resource") String resource,
									@PathVariable("pKey") String pKey,
									@CurrentForm FormConfig formConfig
	) {
		 resourcesService.deleteRecord(formConfig.getCompany(),resource,pKey);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/records/id", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_NM + "')")
	CustomResourceRecord getRecordsByAcsId(@PathVariable("resource") String resource,
									@RequestParam("id") String id,
									@CurrentForm FormConfig formConfig
	) {
		return resourcesService.getRecordByAcsId(formConfig.getCompany(),resource,id);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/records/find", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_RESOURCE_NM + "')")
	CustomResourceRecord getByBusinessKey(@PathVariable("resource") String resource,
										  @RequestParam Map<String,String> allRequestParams,
										  @CurrentForm FormConfig formConfig
	) {
		return resourcesService.getRecordByBusinessKey(formConfig.getCompany(),resource,allRequestParams);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/{resource}/records/search/{filter}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_FIND_RESOURCE_NM + "')")
	List<CustomResourceRecord> searchRecords(@PathVariable("resource") String resource,
										  @PathVariable("filter") String filter,
										  @RequestParam Map<String,String> allRequestParams,
										  @CurrentForm FormConfig formConfig
	) {
		return resourcesService.searchRecords(formConfig.getCompany(), resource, filter, allRequestParams);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PutMapping(value="/{resource}/records/id/{id}",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_UPDATE_RESOURCE_NM + "')")
	CustomResourceRecord updateRecordByAcsId(@PathVariable("resource") String resource,
									@RequestBody CustomResourceRecordDto customResourceRecordDto,
									@PathVariable("id") String acsId,
									@CurrentForm FormConfig formConfig
	) {
		CustomResourceRecord customResourceRecord = servicesModelMapperHelper.fromDto(customResourceRecordDto,CustomResourceRecord.class);
		resourcesService.validateUpdateRecordByAcsId(formConfig.getCompany(),resource, acsId, customResourceRecord);
		return resourcesService.updateRecordByAcsId(formConfig.getCompany(), resource, acsId, customResourceRecord);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@DeleteMapping(value="/{resource}/records/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_REMOVE_RESOURCE_NM + "')")
	void deleteRecordByAcsId(@PathVariable("resource") String resource,
					   @PathVariable("id") String acsId,
					   @CurrentForm FormConfig formConfig
	) {
		resourcesService.validateDeleteRecordByAcsId(formConfig.getCompany(), resource, acsId);
		resourcesService.deleteRecordByAcsId(formConfig.getCompany(), resource, acsId);
	}


}
