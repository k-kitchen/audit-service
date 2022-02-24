package com.klasha.audits.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @RequestMapping("/health")
    public ResponseEntity<Object> loadBalancerHealthCheck() {
        return ResponseEntity.ok().build();
    }
}
