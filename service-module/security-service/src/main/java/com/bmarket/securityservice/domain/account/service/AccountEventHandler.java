package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountEventHandler {

    @EventListener
    public void createProfile(RequestSignUpForm form){
        log.info("프로필 파일이 만들어 졋어");
    }
}
