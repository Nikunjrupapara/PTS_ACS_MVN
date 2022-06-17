package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.acsapi.model.Email;
import com.yesmarketing.acsapi.model.EmailResponse;
import com.yesmarketing.acsapi.model.ProfileModel;
import com.yesmarketing.acsapi.model.ProfileUpdateResponse;
import com.yesmarketing.acsapi.model.ServiceModel;
import com.yesmarketing.acsapi.sagas.EmailSaga;
import com.yesmarketing.ptsacs.services.dto.EmailDto;
import com.yesmarketing.ptsacs.services.dto.ProfileSearchDto;
import com.yesmarketing.ptsacs.services.dto.SubscriptionDto;
import com.yesmarketing.ptsacs.services.enums.View;
import com.yesmarketing.ptsacs.services.model.GetProfileResponse;

import java.util.List;

public interface EmailService extends EmailSaga {

	EmailResponse validateAndSendEmail(String company, EmailDto request);
	EmailResponse sendEmail(String company, EmailDto request);
	void validateSendEmail(String company, EmailDto request);

}
