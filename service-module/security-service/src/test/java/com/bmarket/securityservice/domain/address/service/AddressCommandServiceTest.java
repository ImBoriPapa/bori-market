package com.bmarket.securityservice.domain.address.service;

import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
@Transactional
class AddressCommandServiceTest {

    @Autowired
    AccountCommandService accountCommandService;

    @Test
    @DisplayName("dsda")
    void search() throws Exception{
        //given



        //when

        //then

    }

}