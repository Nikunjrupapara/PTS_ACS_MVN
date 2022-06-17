package com.yesmarketing.ptsacs.services.authentication;

import com.yesmarketing.ptsacs.services.model.FormClaim;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Date;

@Value
public class TokenDetail {
	FormClaim formClaim;

	LocalDateTime notBefore;

	LocalDateTime expirationTime;

	Date issuedAt;

	String jti;
}
