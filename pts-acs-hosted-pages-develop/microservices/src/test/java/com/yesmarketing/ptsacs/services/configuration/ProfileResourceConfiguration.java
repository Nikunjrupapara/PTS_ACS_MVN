package com.yesmarketing.ptsacs.services.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.yesmail.api.common.exception.ObjectNotFoundException;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import com.yesmarketing.acsapi.model.ResponseModel;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;
import com.yesmarketing.ptsacs.services.resource.ProfileResource;
import com.yesmarketing.ptsacs.services.service.ProfileService;
import com.yesmarketing.ptsacs.services.util.MessageKeys;
import com.yesmarketing.ptsacs.services.util.ProfileModelTestHelper;
import com.yesmarketing.ptsacs.services.util.ServicesModelMapperHelper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

public class ProfileResourceConfiguration {

	@Autowired
	private ServicesModelMapperHelper servicesModelMapperHelper;

	@Bean
	public ProfileResource profileResource() {
		return new ProfileResource(profileService(), servicesModelMapperHelper);
	}

	@Bean
	public ProfileService profileService() {
		ProfileService profileService = Mockito.mock(ProfileService.class);
		Mockito
				.when(profileService.getProfileBySecureLink(anyString(), anyString(), anyString(), any(View.class)))
				.thenAnswer(invocation -> {
					String company = invocation.getArgument(0);
					String lookupValue = invocation.getArgument(1);
					String securityValue = invocation.getArgument(2);
					View view = invocation.getArgument(3);
					if (lookupValue.equals("XXX")) {
						throw new ObjectNotFoundException(MessageKeys.PROFILE_NOT_FOUND_BY_ENC_ID_MSG, new Object[] {},
								MessageKeys.PROFILE_NOT_FOUND_INFO, new Object[] {});
					}
					ProfileModel profileModel = ProfileModelTestHelper.getProfileModelByCustomerIdHash(company, lookupValue);
					List<ProfileSubscriptionModel> currentServices;
					if (view.equals(View.SERVICES)) {
						ResponseModel<ProfileSubscriptionModel> rm = ProfileModelTestHelper.getProfileSubscriptions(company, profileModel.getCusCustomerId());
						currentServices = rm.getContent();
					} else {
						currentServices = Collections.emptyList();
					}
					return new GetProfileResponse(profileModel, currentServices, Collections.emptyList());
				});

		Mockito
				.when(profileService.createProfile(any(String.class),any(ProfileModel.class)))
				.thenReturn(ProfileModelTestHelper.getProfileModel("musicnotes", "cb-ym"));

		return profileService;
	}
}
