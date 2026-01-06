package com.ing.mortgage.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }
}
