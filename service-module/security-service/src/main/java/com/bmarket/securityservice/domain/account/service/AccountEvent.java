package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEvent {

    private final ApplicationEventPublisher publisher;
    private final AccountRepository accountRepository;

    public void create(int addressCode){
        log.info("address code={}",addressCode);
        publisher.publishEvent(addressCode);
    }
}
