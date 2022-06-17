package com.yesmarketing.ptsacs.services.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormJsonWebToken {

	@Id
	private String tokenId;

	private LocalDateTime notBefore;

	private LocalDateTime expirationTime;

	private String token;

	private boolean enabled;

	private LocalDateTime created;

	private LocalDateTime lastUpdated;
}
