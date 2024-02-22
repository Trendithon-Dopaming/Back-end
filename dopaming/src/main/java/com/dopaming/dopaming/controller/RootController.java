package com.dopaming.dopaming.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/health")
    public String HealthCheck() {
        return "I'm Healthy";
    }
}
