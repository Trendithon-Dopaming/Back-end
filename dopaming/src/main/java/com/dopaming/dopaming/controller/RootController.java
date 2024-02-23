package com.dopaming.dopaming.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RootController {

    @GetMapping("/")
    public RedirectView redirectToHealth() {
        return new RedirectView("/health");
    }
    
    @GetMapping("/health")
    public String HealthCheck() {
        return "I'm Healthy";
    }
}
