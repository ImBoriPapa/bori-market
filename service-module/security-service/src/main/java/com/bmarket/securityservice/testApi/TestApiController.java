package com.bmarket.securityservice.testApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestApiController {

    @GetMapping("/test-profile-image")
    public String getTestProfileImages(){
        return "http://localhost:8080/test-profile-image";
    }
}
