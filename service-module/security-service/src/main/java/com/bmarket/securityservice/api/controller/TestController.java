package com.bmarket.securityservice.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public void test(){
        log.info("test ok");
    }

    @GetMapping("/jwt-test1")
    public void test1(){
        log.info("test1 ok");
    }

    @GetMapping("/jwt-test2")
    public void test2(){
        log.info("test2 ok");
    }
}
