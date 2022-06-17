package com.yesmarketing.ptsacs.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FormJsonWebTokenDto implements Serializable {
	private static final long serialVersionUID = 1894387463238209241L;

	String tokenId;

	LocalDateTime notBefore;

	LocalDateTime expirationTime;

	String token;

	boolean enabled;

	LocalDateTime created;

	LocalDateTime lastUpdated;
}
