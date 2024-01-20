package com.taco.customtacoapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String returnHomepage() {
        return "home";
    }
}
