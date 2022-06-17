package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;

public interface CustomerIdService {
    String getCustomerId(String company, ProfileSearchDto profileSearchDto);
    String getCustomerIdHash(String company, ProfileSearchDto profileSearchDto);

    String getCustomerId(String company, ProfileModel profileModel);
    String getCustomerIdHash(String company, ProfileModel profileModel);

    void matchCustomerIdDefinition(String company, ProfileModel profileModel);
}
