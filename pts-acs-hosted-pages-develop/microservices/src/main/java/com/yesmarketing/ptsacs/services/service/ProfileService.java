package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileUpdateResponse;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.acsapi.model.ServiceModel;
import com.yesmarketing.acsapi.sagas.SubscriberSagas;
import com.yesmarketing.ptsacs.services.dto.CreateProfileDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchByFilterDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.dto.SubscriptionDto;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;

import java.util.List;

public interface ProfileService extends SubscriberSagas {

	/**
	 *
	 * @param lookupValue The encrypted customerId from a customer's profile in ACS
	 * @param securityValue The emailSHA from a customer's profile in ACS
	 * @return
	 */
	GetProfileResponse getProfileBySecureLink(String company, String lookupValue, String securityValue, View view);
	GetProfileResponse getProfileByCustomerId(String company, ProfileSearchDto profileSearchDto, View view);
	ResponseModel<ProfileModel> getProfileByCustomerIdHash(String company, String customerIdHash);
	ResponseModel<ProfileModel> getProfileByCustomerId(ProfileModel profileModel, String company);
	GetProfileResponse getProfileByFilter(String company, ProfileSearchByFilterDto profileSearchByFilterDto, View view);

	ProfileModel createProfile(String company,ProfileModel profileModel);
	ProfileUpdateResponse updateProfile(String company, String customerIdHash,ProfileModel profileModel);
	ProfileModel updateProfileAndGet(String company, String customerIdHash,ProfileModel profileModel);
	ProfileModel updateProfileAndGetByPKey(String company, String pKey, ProfileModel profileModel);
	GetProfileResponse updateSubscription(String company, String customerIdHash, SubscriptionDto subscriptionDto);
	List<ServiceModel> getCompanySubscriptions(String company);

	GetProfileResponse getUserSubscriptions(String company, String customerIdHash);

	void validateGetProfileBySecureLink(String company, String lookupValue, String securityValue, View view);
	void validateGetProfileByCustomerId(String company, ProfileSearchDto profileSearchDto, View view);

	void validateCreateProfile(String company, ProfileModel profileModel);
	void validateUpdateProfile(String company, String customerIdHash,CreateProfileDto createProfileDto);
	void validateUpdateProfileAndGet(String company, String customerIdHash,CreateProfileDto createProfileDto);
	void validateUpdateSubscription(String company, SubscriptionDto subscriptionDto);
}
