package com.dataaxle.pts.acscustompages.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(path = {"/p/page/csp"})
public class ContentSecurityPolicyController {

	@GetMapping(value = { "/reporting" })
	public ResponseEntity<String> cspReport(@RequestBody String body) {
		LOG.info("CSP report: {}", body);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
