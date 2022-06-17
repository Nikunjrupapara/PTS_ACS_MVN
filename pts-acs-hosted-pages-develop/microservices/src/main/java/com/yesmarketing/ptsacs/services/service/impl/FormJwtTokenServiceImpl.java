package com.yesmarketing.ptsacs.services.service.impl;

import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.service.FormJwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service("formJwtToken")
public class FormJwtTokenServiceImpl implements FormJwtTokenService {

	private final SecretKey secretKey;

	public FormJwtTokenServiceImpl(@Value("${jwt.secretKey.form}") String keyStr) {
		byte[] keyByteArray = keyStr.getBytes();
		secretKey = new SecretKeySpec(keyByteArray, 0, keyByteArray.length, "HmacSHA512");
	}

	@Override
	public String createToken(FormClaim claim) {
		return Jwts.builder()
			.claim("form", claim)
			.setIssuedAt(new Date())
			.signWith(secretKey, SignatureAlgorithm.HS512)
			.compact();
	}

	@Override
	public FormClaim validateTokenOld(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.deserializeJsonWith(new JacksonDeserializer(Maps.of("form", FormClaim.class).build()))
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("form", FormClaim.class);
	}

	@Override
	public String createToken(TokenDetail tokenDetail) {
		ZoneOffset utc = ZoneOffset.UTC;
		Date notBefore = new Date(tokenDetail.getNotBefore().toEpochSecond(utc) * 1000);
		Date expirationTime = new Date(tokenDetail.getExpirationTime().toEpochSecond(utc) * 1000);
		FormClaim formClaim = tokenDetail.getFormClaim();
		return Jwts.builder()
				   .claim("form", formClaim)
				   .setNotBefore(notBefore)
				   .setExpiration(expirationTime)
				   .setIssuedAt(tokenDetail.getIssuedAt())
				   .setId(tokenDetail.getJti())
				   .signWith(secretKey, SignatureAlgorithm.HS512)
				   .compact();
	}

	@Override
	public TokenDetail validateToken(String token) {
		Claims claim = Jwts.parserBuilder()
						   .setSigningKey(secretKey)
						   .deserializeJsonWith(new JacksonDeserializer(Maps.of("form", FormClaim.class).build()))
						   .build()
						   .parseClaimsJws(token)
						   .getBody();
		FormClaim formClaim = claim.get("form", FormClaim.class);
		Date issuedAt = claim.getIssuedAt();
		Date notBefore = claim.getNotBefore();
		LocalDateTime notBeforeLdt = LocalDateTime.ofEpochSecond(notBefore.getTime()/1000, 0, ZoneOffset.UTC);
		Date expirationTime = claim.getExpiration();
		LocalDateTime expirationTimeLdt = LocalDateTime.ofEpochSecond(expirationTime.getTime()/1000, 0, ZoneOffset.UTC);
		String jti = claim.getId();
		return new TokenDetail(formClaim, notBeforeLdt, expirationTimeLdt, issuedAt, jti);
	}

}
