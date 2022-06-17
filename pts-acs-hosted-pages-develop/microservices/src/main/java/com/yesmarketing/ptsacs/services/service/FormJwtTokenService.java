package com.yesmarketing.ptsacs.services.service;

import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;

public interface FormJwtTokenService {

	@Deprecated
	String createToken(FormClaim claim);

	@Deprecated
	FormClaim validateTokenOld(String token);

	String createToken(TokenDetail tokenDetail);

	TokenDetail validateToken(String token);
}
