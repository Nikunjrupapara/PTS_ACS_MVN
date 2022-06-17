package com.dataaxle.pts.acscustompages.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
//@Controller
public class ErrorsController {

	//@GetMapping(path = "/notfound")
	public ModelAndView notfound() {
		LOG.debug("ErrorsController.notfound()...");
		ModelAndView notFound = new ModelAndView();
		notFound.setViewName("notfound");
		return notFound;
	}

	//@GetMapping(path = "/error")
	public ModelAndView error() {
		LOG.debug("ErrorsController.error()...");
		ModelAndView error = new ModelAndView();
		error.setViewName("error");
		return error;
	}
}
