package com.bmarket.securityservice.domain.profile.service;


import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.account.service.AccountCommandService;
import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.profile.controller.ProfileResultForm;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProfileQueryServiceTest {

    @Autowired
    ProfileQueryService profileQueryService;
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;

    public final String defaultImage = "http://localhost:8095/frm/default.img";
    @BeforeEach
    void beforeEach()   {

        ArrayList<Account> list = new ArrayList<>();
        for(int i=1;i<=100;i++){
            Address address = Address.createAddress()
                    .addressCode(1001)
                    .city("서울")
                    .district("서울구")
                    .town("서울동").build();
            Profile profile = Profile.createProfile()
                    .nickname("닉네임"+i)
                    .profileImage(defaultImage)
                    .address(address).build();
            Account account = Account.createAccount()
                    .name("이름"+i)
                    .loginId("로그인아이디"+i)
                    .password("password"+i)
                    .email("query@querry.com"+i)
                    .contact("010"+i+"1212")
                    .profile(profile).build();
            list.add(account);
        }
        accountRepository.saveAll(list);
        System.out.println("[Before Each]");
    }

    @AfterEach
    void AfterEach()   {
        System.out.println("[After Each]");
        accountRepository.deleteAll();
    }
    @Transactional(readOnly = true)
    @Test
    @DisplayName("프로필 단건 조회 테스트")
    void getProfileTest() throws Exception {
        //given
        Account account = accountRepository.findByLoginId("로그인아이디1")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        //when
        ProfileResultForm.profileResult profile = profileQueryService.getProfile(account.getId());

        //then
        assertThat(profile.getNickname()).isEqualTo(account.getProfile().getNickname());
        assertThat(profile.getProfileImage()).isEqualTo(defaultImage);
        assertThat(profile.getFullAddress()).isNotEmpty();
    }

}