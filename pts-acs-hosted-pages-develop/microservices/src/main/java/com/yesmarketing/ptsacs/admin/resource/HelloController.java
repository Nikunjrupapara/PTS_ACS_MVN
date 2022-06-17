package com.yesmarketing.ptsacs.admin.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(value = "/api")
public class HelloController {

	@GetMapping(path = "/hello")
	public String hello() {
		return String.format("Hello, the time at the server is now %s%n", Instant.now());
	}
}
