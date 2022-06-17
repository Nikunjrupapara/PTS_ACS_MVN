package com.dataaxle.pts.acscustompages.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String printWelcome(ModelMap model) {
        model.addAttribute("title", "Data Axle PTS ACS Custom Hosted Pages");
        model.addAttribute("pageHeading", "Spring MVC with Thymeleaf");
        model.addAttribute("imgUrl", "https://www.thymeleaf.org/doc/images/thymeleaf.png");
        return "index";
    }
}
