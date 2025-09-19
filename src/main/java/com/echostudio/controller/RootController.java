package com.echostudio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class RootController {
    @GetMapping
    public String healthCheck() {
        return "EchoStudio API is up";
    }
}
