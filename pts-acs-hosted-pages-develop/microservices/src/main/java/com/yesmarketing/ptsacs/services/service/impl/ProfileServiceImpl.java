package com.yesmarketing.ptsacs.services.service.impl;

import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_ID;
import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_ID_HASH;
import static com.yesmarketing.ptsacs.services.util.ServicesConstants.CUSTOMER_UUID;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmail.api.common.util.MessageService;
import com.yesmarketing.acsapi.auth.model.CredentialModel;
import com.yesmarketing.acsapi.dao.ProfileMetadataDao;
import com.yesmarketing.acsapi.exception.SagasRuntimeException;

import com.yesmarketing.acsapi.model.Link;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.model.ProfileUpdateResponse;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.acsapi.model.ServiceModel;
import com.yesmarketing.acsapi.sagas.SagasHelper;
import com.yesmarketing.acsapi.sagas.SubscriberSagasImpl;
import com.yesmarketing.acsapi.sagas.SubscriptionSagas;
import com.yesmarketing.ptsacs.common.exception.BadRequestException;
import com.yesmarketing.ptsacs.services.dto.CreateProfileDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchByFilterDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.dto.SubscriptionDto;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.service.CustomerIdService;
import com.yesmarketing.ptsacs.services.service.ProfileService;
import com.yesmarketing.ptsacs.services.util.MessageKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.UUID;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl extends SubscriberSagasImpl implements ProfileService {

	private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceImpl.class);

	private final SubscriptionSagas subscriptionSagas;

	private final CustomerIdService customerIdService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	public ProfileServiceImpl(SagasHelper sagasHelper, ProfileMetadataDao profileMetadataDao,
							  SubscriptionSagas subscriptionSagas,
							  CustomerIdService customerIdService) {
		super(sagasHelper, profileMetadataDao);
		this.subscriptionSagas = subscriptionSagas;
		this.customerIdService = customerIdService;
	}

	@Override
	public GetProfileResponse getProfileBySecureLink(String company, String lookupValue, String securityValue, View view) {
		if (StringUtils.isEmpty(lookupValue) || StringUtils.isEmpty(securityValue)) {
			LOG.info("Missing l or s value. l: {}, s: {}", lookupValue, securityValue);
			throw new BadRequestException("Missing l or s value.");
		}
		ResponseModel<ProfileModel> responseModel = getProfileByCustomerIdHash(company, lookupValue);
		if (responseModel.getContent().isEmpty()) {
			LOG.info("Profile not found for l: {}, s: {}", lookupValue, securityValue);
			throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, MessageKeys.PROFILE_NOT_FOUND_INFO);
		}
		ProfileModel profileModel = responseModel.getContent().get(0);
		String cusCustomerUUID = (String)profileModel.getAttribute(CUSTOMER_UUID).orElse("");
		if (!cusCustomerUUID.equals(securityValue)) {
			LOG.error("Security value mismatch: provided: {}, from profile: {}", securityValue, cusCustomerUUID);
			throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, MessageKeys.PROFILE_NOT_FOUND_INFO);
		}
		List<ProfileSubscriptionModel> currentServices;
		if (view.equals(View.SERVICES) || view.equals(View.FULL)) {
			Link subscriptionLink = profileModel.getSubscriptions();
			ResponseModel<ProfileSubscriptionModel> servicesResponse = subscriptionSagas.getProfileSubscriptions(company, subscriptionLink);
			currentServices = servicesResponse.getContent();
		} else {
			currentServices = Collections.emptyList();
		}

		return new GetProfileResponse(profileModel, currentServices, Collections.emptyList());
	}

	@Override
	public GetProfileResponse getProfileByCustomerId(String company, ProfileSearchDto profileSearchDto, View view) {
		String customerIdHash = customerIdService.getCustomerIdHash(company, profileSearchDto);
		ResponseModel<ProfileModel> responseModel = getProfileByCustomerIdHash(company, customerIdHash);
		ProfileModel profileModelOut = responseModel.getContent().get(0);

		List<ProfileSubscriptionModel> currentServices;
		if (view.equals(View.SERVICES) || view.equals(View.FULL)) {
			Link subscriptionLink = profileModelOut.getSubscriptions();
			ResponseModel<ProfileSubscriptionModel> servicesResponse = subscriptionSagas.getProfileSubscriptions(company, subscriptionLink);
			currentServices = servicesResponse.getContent();
		} else {
			currentServices = Collections.emptyList();
		}

		return new GetProfileResponse(profileModelOut, currentServices, Collections.emptyList());
	}

	@Override
	public GetProfileResponse getProfileByFilter(
			String company, ProfileSearchByFilterDto profileSearchByFilterDto, View view) {

		CredentialModel credentialModel = sagasHelper.getCredential(company);
		RestTemplate restTemplate = sagasHelper.createAuthenticatedRestTemplate(credentialModel);

		ResponseModel<ProfileModel> responseModel = acsGetProfileByFilter(company, profileSearchByFilterDto, credentialModel, restTemplate);

		if (responseModel.getContent().isEmpty()) {
			LOG.info("Profile not found using filter {}", profileSearchByFilterDto.getFilterName());
			throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, MessageKeys.PROFILE_NOT_FOUND_INFO);
		}

		ProfileModel profileModelOut = responseModel.getContent().get(0);

		List<ProfileSubscriptionModel> currentServices;
		if (view.equals(View.SERVICES) || view.equals(View.FULL)) {
			Link subscriptionLink = profileModelOut.getSubscriptions();
			ResponseModel<ProfileSubscriptionModel> servicesResponse = subscriptionSagas.getProfileSubscriptions(company, subscriptionLink);
			currentServices = servicesResponse.getContent();
		} else {
			currentServices = Collections.emptyList();
		}

		return new GetProfileResponse(profileModelOut, currentServices, Collections.emptyList());
	}

	@Override
	public ResponseModel<ProfileModel> getProfileByCustomerIdHash(String company, String customerIdHash) {
		CredentialModel credentialModel = sagasHelper.getCredential(company);
		RestTemplate restTemplate = sagasHelper.createAuthenticatedRestTemplate(credentialModel);
		ResponseModel<ProfileModel> responseModel;

		responseModel = acsGetProfileByHash(company, customerIdHash, credentialModel, restTemplate);

		// If profile is not found with supplied customerIdHash,try again with uppercase version
		// Note that mixed case will still result in error
		if (responseModel.getContent().isEmpty()) {
			responseModel = acsGetProfileByHash(company, customerIdHash.toUpperCase(), credentialModel, restTemplate);
			if (responseModel.getContent().isEmpty()) {
				LOG.info("Profile not found for customerIdHash: {}", customerIdHash);
				throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, MessageKeys.PROFILE_NOT_FOUND_INFO);
			}
		}

		return responseModel;
	}

	@Override
	public ResponseModel<ProfileModel> getProfileByCustomerId(ProfileModel profileModel, String company) {
		CredentialModel credentialModel = sagasHelper.getCredential(company);
		RestTemplate restTemplate = sagasHelper.createAuthenticatedRestTemplate(credentialModel);
		ResponseModel<ProfileModel> responseModel;

		responseModel = acsGetProfileByCustomerId(profileModel, company, credentialModel, restTemplate);

		if (responseModel.getContent().isEmpty()) {
			throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, MessageKeys.PROFILE_NOT_FOUND_INFO);
		}

		return responseModel;
	}

	/*  FilterName and parameter names are expected verbatim as defined in ACS  */
	private ResponseModel<ProfileModel> acsGetProfileByFilter(
			String company, ProfileSearchByFilterDto profileSearchByFilterDto,
			CredentialModel credentialModel, RestTemplate restTemplate) {

		try {
			String filterName = profileSearchByFilterDto.getFilterName();
			StringBuilder methodStr = new StringBuilder("/profileAndServicesExt/profile/");
			StringBuilder paramsStr = new StringBuilder("");

			methodStr.append(filterName).append("?");
			Map<String, String> map = profileSearchByFilterDto.getSearchFields();
			map.forEach((k, v) -> paramsStr.append("&").append(k).append("=").append(v));
			methodStr.append(paramsStr.substring(1));

			String uri = sagasHelper.formUrl(credentialModel.getACSBaseUrl(), methodStr.toString());
			String responseStr = restTemplate.getForObject(uri, String.class);
			return parseProfilesResponse(company, responseStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}

	private ResponseModel<ProfileModel> acsGetProfileByHash(
			String company, String customerIdHash,
			CredentialModel credentialModel, RestTemplate restTemplate){
		try {
			String method = String.format("/profileAndServicesExt/profile/%s?%s=%s", "byCustomeridhash", "customerIdHash_parameter", customerIdHash);
			String uri = sagasHelper.formUrl(credentialModel.getACSBaseUrl(), method);
			String responseStr = restTemplate.getForObject(uri, String.class);
			return parseProfilesResponse(company, responseStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}

	private ResponseModel<ProfileModel> acsGetProfileByCustomerId(
			ProfileModel profileModel, String company,
			CredentialModel credentialModel, RestTemplate restTemplate){
		try {
			String customerId = customerIdService.getCustomerId(company, profileModel);
			String method = String.format("/profileAndServicesExt/profile/%s?%s=%s", "byCustomerid", "customerId_parameter", customerId);
			String uri = sagasHelper.formUrl(credentialModel.getACSBaseUrl(), method);
			String responseStr = restTemplate.getForObject(uri, String.class);
			return parseProfilesResponse(company, responseStr);
		} catch (Exception e) {
			throw new SagasRuntimeException(e);
		}
	}

	/**
	 * plain text getCustomerId() =>  Customer ID (cusCustomerId)
	 * random UUID => Customer UUID (cusCustomerUUID)
	 * hashed getCustomerId() => Customer ID Hash (cusCustomerIdHash)
	 * IGNORE cusCustomerIdSHA
	 */

	@Override
	public ProfileModel createProfile(String company, ProfileModel profileModel) {
		String customerId = customerIdService.getCustomerId(company, profileModel);
		String customerIdHash = customerIdService.getCustomerIdHash(company, profileModel);

		profileModel.setAttribute(CUSTOMER_ID, customerId);
		profileModel.setAttribute(CUSTOMER_ID_HASH, customerIdHash);
		profileModel.setAttribute(CUSTOMER_UUID, UUID.randomUUID().toString());

		return super.createProfile(company, profileModel);
	}

	@Override
	public ProfileUpdateResponse updateProfile(String company, String customerIdHash, ProfileModel profileModel){
		ResponseModel<ProfileModel> responseModel = getProfileByCustomerIdHash(company, customerIdHash);
		ProfileModel profileModelOut = responseModel.getContent().get(0);

		String pKey = profileModelOut.getPKey();
		return super.updateProfile(company, pKey, profileModel);
	}

	@Override
	public ProfileModel updateProfileAndGet(String company, String customerIdHash, ProfileModel profileModel){
		ResponseModel<ProfileModel> responseModel = getProfileByCustomerIdHash(company, customerIdHash);
		ProfileModel profileModelOut = responseModel.getContent().get(0);

		String pKey = profileModelOut.getPKey();
		ProfileUpdateResponse profileUpdateResponse = super.updateProfile(company, pKey, profileModel);

		// re-acquire the pKey again after update in case it is different
		String pKey_update = profileUpdateResponse.getPKey();
		return super.getProfileByPkey(company, pKey_update);
	}

	@Override
	public ProfileModel updateProfileAndGetByPKey(String company, String pKey, ProfileModel profileModel){
		String customerId = customerIdService.getCustomerId(company, profileModel);
		String customerIdHash = customerIdService.getCustomerIdHash(company, profileModel);

		profileModel.setAttribute(CUSTOMER_ID, customerId);
		profileModel.setAttribute(CUSTOMER_ID_HASH, customerIdHash);
		profileModel.setAttribute(CUSTOMER_UUID, UUID.randomUUID().toString());

		ProfileUpdateResponse profileUpdateResponse = super.updateProfile(company, pKey, profileModel);

		// re-acquire the pKey again after update in case it is different
		String pKey_update = profileUpdateResponse.getPKey();
		return super.getProfileByPkey(company, pKey_update);
	}

	@Override
	public GetProfileResponse updateSubscription(String company, String customerIdHash, SubscriptionDto subscriptionDto){

		Map<String, ServiceModel> companySubscriptionMap = getCompanySubscriptions(company).stream().collect(Collectors.toMap(ServiceModel::getName, Function.identity()));

		/*
			2. Load and subscribe/unsubscribe the user
		*/
		ResponseModel<ProfileModel> rm = getProfileByCustomerIdHash(company, customerIdHash);
		ProfileModel profileModel = rm.getContent().get(0);
		Link link = profileModel.getSubscriptions();

		ResponseModel<ProfileSubscriptionModel> responseModel = subscriptionSagas.getProfileSubscriptions(company, link);
		List<ProfileSubscriptionModel>profileService = responseModel.getContent();

		Map<String,ProfileSubscriptionModel> userSubscriptionMap = new HashMap<>();
		for(ProfileSubscriptionModel element:profileService ){
			userSubscriptionMap.put(element.getService().getName(), element);
		}

		// Create Map of Subscriptions to remove so that we don't have to Subscribe/Unsubscribe from same subscription
		Set<String> removeSet = (subscriptionDto.getRemove()==null)?new HashSet():subscriptionDto.getRemove().stream().collect(Collectors.toSet());


		// add service (only if service is missing)
		if(subscriptionDto.getAdd()!=null){
			subscriptionDto.getAdd().stream().forEach(serviceName -> {
				if(!userSubscriptionMap.containsKey(serviceName) && !removeSet.contains(serviceName))
					subscriptionSagas.subscribe(company,link,companySubscriptionMap.get(serviceName));
			});
		}

		// remove service
		if(subscriptionDto.getRemove()!=null){
			subscriptionDto.getRemove().stream().forEach(serviceName -> {
				if(userSubscriptionMap.containsKey(serviceName))
					subscriptionSagas.unsubscribe(company,userSubscriptionMap.get(serviceName));
			});
		}

		/*
			3. Reload the user with modified subscriptions
		*/

		return getUserSubscriptions(company, customerIdHash);
	}

	public GetProfileResponse getUserSubscriptions(String company, String customerIdHash){
		ResponseModel<ProfileModel> rm2 = getProfileByCustomerIdHash(company, customerIdHash);
		ProfileModel profileModelOut = rm2.getContent().get(0);

		List<ProfileSubscriptionModel> currentServices;
		Link subscriptionLink = profileModelOut.getSubscriptions();
		ResponseModel<ProfileSubscriptionModel> servicesResponse = subscriptionSagas.getProfileSubscriptions(company, subscriptionLink);
		currentServices = servicesResponse.getContent();

		return new GetProfileResponse(profileModelOut, currentServices, Collections.emptyList());
	}

	@Override
	@Cacheable("subscriptions")
	public List<ServiceModel> getCompanySubscriptions(String company){
		return subscriptionSagas.getSubscriptions(company);
	}

	@Override
	public void validateGetProfileBySecureLink(String company, String lookupValue, String securityValue, View view) {

	}

	@Override
	public void validateGetProfileByCustomerId(String company, ProfileSearchDto profileSearchDto, View view) {

	}

	@Override
	public void validateCreateProfile(String company, ProfileModel profileModel) {
		customerIdService.matchCustomerIdDefinition(company, profileModel);
	}

	@Override
	public void validateUpdateProfile(String company, String customerIdHash, CreateProfileDto createProfileDto) {

	}

	@Override
	public void validateUpdateProfileAndGet(String company, String customerIdHash, CreateProfileDto createProfileDto) {

	}

	@Override
	public void validateUpdateSubscription(String company, SubscriptionDto subscriptionDto) {
		Map<String, ServiceModel> companySubscriptionMap = getCompanySubscriptions(company).stream().collect(Collectors.toMap(ServiceModel::getName, Function.identity()));

		validateServiceNames(subscriptionDto, companySubscriptionMap);
	}

	public void validateServiceNames(SubscriptionDto subscriptionDto, Map<String, ServiceModel> companySubscriptionMap){
		List<String> allServiceNames = new LinkedList<>();

		if(!CollectionUtils.isEmpty(subscriptionDto.getAdd())) {
			allServiceNames.addAll(subscriptionDto.getAdd());
		}

		if(!CollectionUtils.isEmpty(subscriptionDto.getRemove())){
			allServiceNames.addAll(subscriptionDto.getRemove());
		}

		if (allServiceNames.isEmpty()) {
			throw new BadRequestException("Missing Service names. Please provide at least one service to add or remove");
		}

		allServiceNames.stream().forEach(serviceName ->{
			if (!companySubscriptionMap.containsKey(serviceName)) {
				throw new BadRequestException(String.format("Invalid service name: %s.", serviceName));
			}
		});

	}


}
