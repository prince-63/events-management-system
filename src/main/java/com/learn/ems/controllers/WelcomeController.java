package com.learn.ems.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.learn.ems.constants.WelcomeApiEndPointsConstants.WELCOME;

@RestController
public class WelcomeController {

    @GetMapping(WELCOME)
    public String sayWelcome() {
        return "Hello World";
    }

}
