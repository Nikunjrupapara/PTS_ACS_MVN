package com.yesmarketing.ptsacs.services.model;

import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileSubscriptionModel;
import lombok.Data;

import java.util.List;

@Data
public class GetProfileResponse {

    private final ProfileModel profile;

    private final List<ProfileSubscriptionModel> currentServices;

    private final List<Object> historicServices;
}
