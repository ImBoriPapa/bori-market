package com.bmarket.securityservice.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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

    @GetMapping("/redirect1")
    public ResponseEntity test3(){


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/redirect2"));
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).headers(httpHeaders).body("");
    }
    @GetMapping("/redirect2")
    public void test4(){
        log.info("redirect success");
    }
}
