package com.learn.ems.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.learn.ems.constants.WelcomeApiEndPointsConstants.WELCOME;

@Tag(
        name = "Welcome API",
        description = "API for testing and welcoming users"
)
@RestController
public class WelcomeController {

    @Operation(
            summary = "Welcome API",
            description = "Returns a simple welcome message. Useful for checking if the service is up and running."
    )
    @GetMapping(WELCOME)
    public String sayWelcome() {
        return "Hello World";
    }

}

