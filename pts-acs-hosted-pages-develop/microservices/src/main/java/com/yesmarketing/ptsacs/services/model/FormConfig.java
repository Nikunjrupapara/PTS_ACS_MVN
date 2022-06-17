package com.yesmarketing.ptsacs.services.model;

import com.yesmarketing.ptsacs.services.authentication.ServicesGrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormConfig {

	@Id
	private String uuid;

	private String company;

	private String code;

	private String description;

	private LocalDateTime effectiveFrom;

	private LocalDateTime effectiveTo;

	private List<String> domains;

	private List<ServicesGrantedAuthority> authorities;

	private List<FormJsonWebToken> jwts;

	private boolean enabled;

	private LocalDateTime created;

	private LocalDateTime lastUpdated;

	public Optional<FormJsonWebToken> getToken(String tokenId) {
		return jwts.stream()
				   .filter(jwt -> jwt.getTokenId().equals(tokenId))
				   .findFirst();
	}
}
