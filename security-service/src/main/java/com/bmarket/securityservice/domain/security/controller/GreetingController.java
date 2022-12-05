package com.bmarket.securityservice.domain.security.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(){

        return "hi";
    }
}
