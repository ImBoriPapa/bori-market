package com.bmarket.securityservice.api.security.service;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserDetailServiceImplTest {

    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void afterEach(){
        Account account = accountRepository.findByLoginId("login").get();
        accountRepository.delete(account);
    }

    @Test
    @DisplayName("UserDetails 생성 테스트")
    void loadUserByUsernameTest() throws Exception {
        //given
        Account account = Account.createAccount()
                .loginId("login")
                .name("name")
                .password("1234")
                .email("email@email.com")
                .contact("010-1213-1213")
                .build();
        Account savedAccount = accountRepository.save(account);
        //when
        UserDetails userDetails = userDetailService.loadUserByUsername(savedAccount.getClientId());
        //then
        assertThat(userDetails.getUsername()).isEqualTo(savedAccount.getClientId());
        assertThat(userDetails.getPassword()).isEqualTo(savedAccount.getPassword());
        assertThat(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .containsExactly(Authority.USER.ROLL);
        
    }
    
    @Test
    @DisplayName("clientId 로 Authentication 객체 생성 테스트")
    void Authentication() throws Exception{
        //given
        Account account = Account.createAccount()
                .loginId("login")
                .name("name")
                .password("1234")
                .email("email@email.com")
                .contact("010-1213-1213")
                .build();
        Account savedAccount = accountRepository.save(account);
        //when
        Authentication authentication = userDetailService.generateAuthenticationByClientId(savedAccount.getClientId());
        //then
        assertThat(authentication.getName()).isEqualTo(savedAccount.getClientId());
        assertThat(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())).containsExactly(Authority.USER.ROLL);

    }
}