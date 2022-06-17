package com.yesmarketing.ptsacs.services.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.yesmarketing.ptsacs.services.authentication.TokenDetail;
import com.yesmarketing.ptsacs.services.model.FormClaim;
import com.yesmarketing.ptsacs.services.service.impl.FormJwtTokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFormJwtTokenService {

	private FormJwtTokenService formJwtTokenService;

	private static String token;

	private static LocalDateTime notBefore;

	private static LocalDateTime expirationTime;

	private static Date issuedAt;

	private static String jti;

	@BeforeEach
	void setup() {
		String keyStr = "My Local JWT Secret Key for Testing which must be at least 64 bytes long";
		formJwtTokenService = new FormJwtTokenServiceImpl(keyStr);
	}

	@Nested
	@DisplayName("Generate token tests")
	class BasicFormTokenTests {

		@Test
		@DisplayName("Create a new token")
		@Order(10)
		void createToken() {
			FormClaim claim = new FormClaim("ymnewsolutions", "615704bd-5312-4bd3-89ea-fd1c38ee428b");
			notBefore = LocalDateTime.of(2020, 6, 3, 22, 0, 0, 0);
			expirationTime = LocalDateTime.of(2099, 12, 31, 0, 0, 0, 0);
			issuedAt = new Date();
			jti = "eddfb14e-a74e-4c38-82e3-99a55a0ad229";
			TokenDetail tokenDetail = new TokenDetail(claim, notBefore, expirationTime, issuedAt, jti);
			assertAll(() -> {
				try {
					token = formJwtTokenService.createToken(tokenDetail);
					LOG.info("token: {}", token);
					assertThat(token).as("Token is populated").isNotEmpty();
				} catch (Exception e) {
					fail(e.getMessage(), e);
				}
			});
		}

		@Test
		@DisplayName("Decode the token")
		@Order(20)
		void decodeToken() {
			TokenDetail tokenDetail = formJwtTokenService.validateToken(token);
			assertNotNull(tokenDetail);
			FormClaim claim = tokenDetail.getFormClaim();
			assertNotNull(claim);
			assertAll(() -> {
				assertEquals("ymnewsolutions", claim.getCompany(), "Claim company");
				assertEquals("615704bd-5312-4bd3-89ea-fd1c38ee428b", claim.getUuid(), "Claim uuid");
				assertEquals("eddfb14e-a74e-4c38-82e3-99a55a0ad229", tokenDetail.getJti());
				assertEquals(notBefore, tokenDetail.getNotBefore(), "notBefore");
				assertEquals(expirationTime, tokenDetail.getExpirationTime(), "ExpirationTime");
				// Divide by 1000 because the issuedAt returned by JWT library excludes fractional seconds
				assertEquals(issuedAt.getTime()/1000, tokenDetail.getIssuedAt().getTime()/1000, "issuedAt");
			});
		}
	}
}
