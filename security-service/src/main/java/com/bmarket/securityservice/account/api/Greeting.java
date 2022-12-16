package com.bmarket.securityservice.account.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Greeting {

    @GetMapping("/hello")
    public void greeting(){
        log.info("Hello");
    }
}
