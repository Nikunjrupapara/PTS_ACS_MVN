package com.yesmarketing.ptsacs.admin.service;

import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;
import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.model.FormConfig;

import java.util.List;

public interface FormConfigService {

	FormConfig getFormConfig(FormClaim formClaim);

	FormConfig getFormConfig(String uuid);

	List<FormConfig> getForCompany(String company);

	FormConfig createFormConfig(FormConfig formConfig);

	FormConfig updateFormConfig(FormConfig formConfig);

	FormConfig setFormStatus(String uuid, boolean enabled);

	void delete(String uuid);

	String generateToken(TokenDetail tokenDetail);

	TokenDetail validateToken(String token);
}
