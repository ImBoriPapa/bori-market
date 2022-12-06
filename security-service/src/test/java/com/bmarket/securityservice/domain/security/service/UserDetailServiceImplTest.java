package com.bmarket.securityservice.domain.security.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;

import com.bmarket.securityservice.domain.security.service.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
@ActiveProfiles("local")
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
        UserDetails userDetails = userDetailService.loadUserByUsername(String.valueOf(savedAccount.getId()));
        //then
        assertThat(userDetails.getUsername()).isEqualTo(String.valueOf(savedAccount.getId()));
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
        Authentication authentication = userDetailService.generateAuthentication(savedAccount.getId());
        //then
        assertThat(authentication.getName()).isEqualTo(String.valueOf(savedAccount.getId()));
        assertThat(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())).containsExactly(Authority.USER.ROLL);

    }
}