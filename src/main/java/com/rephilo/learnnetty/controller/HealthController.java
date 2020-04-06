package com.rephilo.learnnetty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rephilo
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Object healthCheck() {
        return "hello world";
    }
}
