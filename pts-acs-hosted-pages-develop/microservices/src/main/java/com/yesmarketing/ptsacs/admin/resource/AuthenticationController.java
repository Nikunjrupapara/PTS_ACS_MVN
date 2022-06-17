package com.yesmarketing.ptsacs.admin.resource;

import com.yesmarketing.ptsacs.admin.util.AdminConstants;
import com.yesmarketing.ptsacs.common.authentication.JwtTokenProvider;
import com.yesmarketing.ptsacs.common.authentication.payload.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
public class AuthenticationController {


	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider jwtTokenProvider;

	private final Environment env;

	public AuthenticationController(@Qualifier(AdminConstants.ADMIN_API_AUTH_MGR)AuthenticationManager authenticationManager,
									JwtTokenProvider jwtTokenProvider, Environment env) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.env = env;
	}

	@PostMapping(path = "/authenticate")
	public ResponseEntity<String> authenticate(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		boolean useSecureCookie = true;
		if (isDevOrIntTest()) {
			useSecureCookie = request.isSecure();
		}

		String jwt = jwtTokenProvider.generateToken(authentication);
		HttpCookie httpCookie = ResponseCookie.from("accessToken", jwt)
			.httpOnly(true)
			.maxAge(86400)
			.path("/")
			.secure(useSecureCookie)
			.build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, httpCookie.toString())
				   .body("Authenticated");
	}

	private boolean isDevOrIntTest() {
		boolean devOrIntTest = false;
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		if (!profiles.isEmpty()) {
			// if the active profile is "development" or "integrationtest" return true, else return false.
			devOrIntTest = profiles.stream().anyMatch(name -> name.equals("development") || name.equals("integrationtest"));
		}
		LOG.debug("profiles = {}, devOrIntTest = {}", profiles, devOrIntTest);
		return devOrIntTest;
	}
}
