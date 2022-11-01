package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.address.AddressRange;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class AccountCommandServiceTest {
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreateTest() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("tester")
                .name("이테스트")
                .nickname("브레드피트")
                .password("bread1234")
                .email("bread@bread.com")
                .contact("010-2222-1234")
                .addressCode(1100)
                .city("서울")
                .district("종로구")
                .town("암사동")
                .build();
        //when
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        Account findAccount = accountRepository.findById(signupResult.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다"));
        //then
        //계정 생성시 반환값 SignupResult : accountId,createdAt
        assertThat(findAccount.getId()).isEqualTo(signupResult.getAccountId());
        assertThat(findAccount.getCreatedAt()).isEqualTo(signupResult.getCreatedAt());
        assertThat(findAccount.getLoginId()).isEqualTo("tester");
        assertThat(findAccount.getName()).isEqualTo("이테스트");
        //encoding 된 패스워드 확인
        assertThat(passwordEncoder.matches("bread1234", findAccount.getPassword())).isTrue();
        assertThat(findAccount.getEmail()).isEqualTo("bread@bread.com");
        assertThat(findAccount.getContact()).isEqualTo("010-2222-1234");
        //최초 가입시 권한은 ROLL_USER
        assertThat(findAccount.getAuthority()).isEqualTo(Authority.ROLL_USER);
        assertThat(findAccount.getRefreshToken()).isNull();
        assertThat(findAccount.getProfile().getNickname()).isEqualTo("브레드피트");
        //최초 가입시 프로필 이미지는 기본이미지로 저장 : http://localhost:8095/file/default/default-profile.jpg
        assertThat(findAccount.getProfile().getProfileImage()).startsWith("http");
        assertThat(findAccount.getProfile().getAddress().getAddressCode()).isNotNull();
        assertThat(findAccount.getProfile().getAddress().getCity()).isEqualTo("서울");
        assertThat(findAccount.getProfile().getAddress().getDistrict()).isEqualTo("종로구");
        assertThat(findAccount.getProfile().getAddress().getTown()).isEqualTo("암사동");
        assertThat(findAccount.getProfile().getAddressRange()).isEqualTo(AddressRange.JUST);

    }

    @Test
    @DisplayName("계정 수정 테스트")
    void updateAccountTest() throws Exception{
        //given

        //when

        //then

    }

}