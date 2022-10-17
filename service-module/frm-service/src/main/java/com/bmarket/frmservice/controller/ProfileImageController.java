package com.bmarket.frmservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileImageController {

    @PostMapping("/frm/profile")
    public void createProfileImage() {

        log.info("profile Image 생성 요청");

    }
}
