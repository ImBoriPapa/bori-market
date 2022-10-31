package com.bmarket.securityservice.api.account.entity;

import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.profile.entity.Profile;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AccountTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;


    @AfterEach
    void init() {

    }

    @Test
    @DisplayName("Account 생성 테스트")
    void createAccount() throws Exception {
        //given
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("city")
                .district("district")
                .town("town")
                .build();
        Profile profile = Profile.createProfile()
                .nickname("nickname")
                .profileImage("프로필이미지.jpg")
                .address(address).build();

        Account account = Account.createAccount()
                .loginId("login")
                .name("name")
                .password("1234")
                .email("email@email.com")
                .contact("010-1213-1213")
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);

        //when

        //then

    }

}