package com.yesmarketing.ptsacs.common.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

	private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private Integer jwtExpirationInMs;

	private SecretKey jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateToken(Authentication authentication) {
		//UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		User user = (User)authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
			.setSubject(user.getUsername())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(SignatureAlgorithm.HS512, jwtSecretKey)
			.compact();
	}

	public String getUsernameFromJwt(String token) {

		Claims claims = Jwts.parserBuilder()
			.setSigningKey(jwtSecretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			LOG.error("Invalid JWT signature", e);
		} catch (MalformedJwtException e) {
			LOG.error("Invalid JWT token", e);
		} catch (ExpiredJwtException e) {
			LOG.error("Expired JWT token", e);
		} catch (UnsupportedJwtException e) {
			LOG.error("Unsupported JWT token", e);
		} catch (IllegalArgumentException e) {
			LOG.error("JWT claims string is empty", e);
		}
		return false;
	}
}
