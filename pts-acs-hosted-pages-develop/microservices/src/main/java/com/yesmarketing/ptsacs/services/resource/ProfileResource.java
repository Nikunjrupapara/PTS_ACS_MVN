package com.yesmarketing.ptsacs.services.resource;

import com.yesmail.api.common.exception.BadRequestException;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileUpdateResponse;
import com.yesmarketing.ptsacs.services.authentication.CurrentForm;
import com.yesmarketing.ptsacs.services.dto.CreateProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileDto;
import com.yesmarketing.ptsacs.services.dto.GetProfileResponseDto;
import com.yesmarketing.ptsacs.services.dto.GetServiceSubscriptionDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchByFilterDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.dto.SubscriptionDto;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.FormConfig;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.service.ProfileService;
import com.yesmarketing.ptsacs.services.util.MessageKeys;
import com.yesmarketing.ptsacs.services.util.ServicesConstants;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/services/profiles")
public class ProfileResource {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileResource.class);

	private final ProfileService profileService;

	private final ServicesModelMapperHelper servicesModelMapperHelper;

	@Autowired
	public ProfileResource(ProfileService profileService, ServicesModelMapperHelper servicesModelMapperHelper) {
		this.profileService = profileService;
		this.servicesModelMapperHelper = servicesModelMapperHelper;
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_PROFILE_SECURE_LINK_NM + "')")
	public GetProfileResponseDto getProfile(@RequestParam(name="l", required = true) String lookupValue,
											@RequestParam(name = "s", required = true) String securityValue,
											@RequestParam(name = "view", required = false) String viewStr,
											@CurrentForm FormConfig formConfig) {

		LOG.info("lookupValue: {}, securityValue: {}, viewStr: {}", lookupValue, securityValue, viewStr);
		View view = View.fromString(viewStr).orElseThrow(() -> new BadRequestException(MessageKeys.INVALID_REQUEST_PARAMETER_MSG,
				MessageKeys.INVALID_REQUEST_PARAMETER_INFO));
		GetProfileResponse getProfileResponse = profileService.getProfileBySecureLink(formConfig.getCompany(), lookupValue,
				securityValue, view);

		return servicesModelMapperHelper.toDto(getProfileResponse, GetProfileResponseDto.class);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value="/customer",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_PROFILE_CUSTOMERID_NM + "')")
	public GetProfileResponseDto getProfileByCustomerId(@RequestBody ProfileSearchDto profileSearchDto,
														@RequestParam(name = "view", required = false) String viewStr,
														@CurrentForm FormConfig formConfig) {
		View view = View.fromString(viewStr).orElseThrow(() -> new BadRequestException(MessageKeys.INVALID_REQUEST_PARAMETER_MSG,
				MessageKeys.INVALID_REQUEST_PARAMETER_INFO));
		GetProfileResponse profile = profileService.getProfileByCustomerId(formConfig.getCompany(), profileSearchDto, view);
		return servicesModelMapperHelper.toDto(profile, GetProfileResponseDto.class);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value="/search",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_GET_PROFILE_FILTER_NM + "')")
	public GetProfileResponseDto getProfileByFilter(@RequestBody ProfileSearchByFilterDto profileSearchByFilterDto,
													@RequestParam(name = "view", required = false) String viewStr,
													@CurrentForm FormConfig formConfig) {
		View view = View.fromString(viewStr).orElseThrow(() -> new BadRequestException(MessageKeys.INVALID_REQUEST_PARAMETER_MSG,
				MessageKeys.INVALID_REQUEST_PARAMETER_INFO));
		GetProfileResponse profile = profileService.getProfileByFilter(formConfig.getCompany(), profileSearchByFilterDto, view);
		return servicesModelMapperHelper.toDto(profile, GetProfileResponseDto.class);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PostMapping(value="/create",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_CREATE_PROFILE_NM + "')")
	public GetProfileDto createProfile(@RequestBody CreateProfileDto createProfileDto, @CurrentForm FormConfig formConfig) {
		ProfileModel profileModel = servicesModelMapperHelper.fromDto(createProfileDto, ProfileModel.class);

		profileService.validateCreateProfile(formConfig.getCompany(), profileModel);
		ProfileModel profile = profileService.createProfile(formConfig.getCompany(), profileModel);

		return servicesModelMapperHelper.toDto(profile, GetProfileDto.class);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PutMapping(value="/updateonly/{customerIdHash}",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_UPDATE_PROFILE_NM + "')")
	public ProfileUpdateResponse updateProfileOnly(@RequestBody CreateProfileDto createProfileDto, @PathVariable("customerIdHash") String customerIdHash, @CurrentForm FormConfig formConfig) {

		profileService.validateUpdateProfile(formConfig.getCompany(), customerIdHash, createProfileDto);

		ProfileModel model = servicesModelMapperHelper.fromDto(createProfileDto, ProfileModel.class);
		ProfileUpdateResponse profileUpdateResponse = profileService.updateProfile(formConfig.getCompany(), customerIdHash, model);

		return profileUpdateResponse;
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@PutMapping(value="/update/{customerIdHash}",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_UPDATE_PROFILE_NM + "')")
	public GetProfileDto updateProfile(@RequestBody CreateProfileDto createProfileDto, @PathVariable("customerIdHash") String customerIdHash,@CurrentForm FormConfig formConfig) {

		profileService.validateUpdateProfileAndGet(formConfig.getCompany(), customerIdHash, createProfileDto);

		ProfileModel model = servicesModelMapperHelper.fromDto(createProfileDto, ProfileModel.class);
		ProfileModel updatedProfileModel = profileService.updateProfileAndGet(formConfig.getCompany(), customerIdHash, model);

		return servicesModelMapperHelper.toDto(updatedProfileModel, GetProfileDto.class);
	}


	@CrossOrigin(value = "*", allowCredentials = "true")
	@PutMapping(value="/{customerIdHash}/services",consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_ADD_SERVICE_NM + "')")
	public GetProfileResponseDto updateServices(@RequestBody SubscriptionDto subscriptionDto, @PathVariable("customerIdHash") String customerIdHash, @CurrentForm FormConfig formConfig) {

		profileService.validateUpdateSubscription(formConfig.getCompany(), subscriptionDto);

		GetProfileResponse profile = profileService.updateSubscription(formConfig.getCompany(), customerIdHash, subscriptionDto);

		return servicesModelMapperHelper.toDto(profile, GetProfileResponseDto.class);
	}

	@CrossOrigin(value = "*", allowCredentials = "true")
	@GetMapping(value="/company/services", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasAuthority('" + ServicesConstants.GA_ADD_SERVICE_NM + "')")
	public List<GetServiceSubscriptionDto> getServices(@CurrentForm FormConfig formConfig) {

		List<GetServiceSubscriptionDto> services = servicesModelMapperHelper.toDto(profileService.getCompanySubscriptions(formConfig.getCompany()), GetServiceSubscriptionDto.class);
		return services;
	}

}
