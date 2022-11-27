package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.controller.ResponseAccountForm;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

// TODO: 2022/11/19 테스트 코드 보강
@ActiveProfiles("test")
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
    @Autowired
    EntityManager em;

    public MockWebServer mockWebServer;

    @BeforeEach
    void beforeEach() throws IOException {

        accountRepository.save(
                Account.createAdmin("admin1", "admin1", "!@admin1234", "admin1@admin.com", "01011111111", Authority.ADMIN)
        );

        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/profile/default");

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody("http://localhost:8095/frm/profile/default.jpg");

        mockWebServer.enqueue(mockResponse);
    }

    @AfterEach
    void afterEach() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreateTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
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
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
        Account findAccount = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다"));
        //then
        //계정 생성시 반환값 SignupResult : accountId,createdAt
        assertThat(findAccount.getId()).isEqualTo(signupForm.getAccountId());
        assertThat(findAccount.getCreatedAt()).isEqualTo(signupForm.getCreatedAt());
        assertThat(findAccount.getLoginId()).isEqualTo("tester");
        assertThat(findAccount.getName()).isEqualTo("이테스트");
        //encoding 된 패스워드 확인
        assertThat(passwordEncoder.matches("bread1234", findAccount.getPassword())).isTrue();
        assertThat(findAccount.getEmail()).isEqualTo("bread@bread.com");
        assertThat(findAccount.getContact()).isEqualTo("010-2222-1234");
        //최초 가입시 권한은 ROLL_USER
        assertThat(findAccount.getAuthority()).isEqualTo(Authority.USER);
        assertThat(findAccount.getRefreshToken()).isNull();
        assertThat(findAccount.getProfile().getNickname()).isEqualTo("브레드피트");
        assertThat(findAccount.getProfile().getProfileImage()).isEqualTo("http://localhost:8095/frm/profile/default.jpg");

        assertThat(findAccount.getProfile().getAddress().getAddressCode()).isNotNull();
        assertThat(findAccount.getProfile().getAddress().getCity()).isEqualTo("서울");
        assertThat(findAccount.getProfile().getAddress().getDistrict()).isEqualTo("종로구");
        assertThat(findAccount.getProfile().getAddress().getTown()).isEqualTo("암사동");
        assertThat(findAccount.getProfile().getAddressRange()).isEqualTo(AddressRange.ONLY);
    }

    @Test
    @DisplayName("계정 삭제 테스트")
    void deleteAccountTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
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
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
        accountCommandService.deleteAccount(signupForm.getAccountId(), "bread1234");

        //then
        assertThatThrownBy(() ->
                accountRepository.findById(signupForm.getAccountId())
                        .orElseThrow(() -> new IllegalArgumentException("계정을 찾지 못했습니다.")))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                profileRepository.findById(signupForm.getAccountId())
                        .orElseThrow(() -> new IllegalArgumentException("프로필을 찾지 못했습니다")))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("계정 비밀번호 수정 테스트")
    void updatePasswordTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
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
        String beforePassword = form.getPassword();
        String afterPassword = "new1234";
        //when
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);
        Account findAccountBefore = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        accountCommandService.updatePassword(signupForm.getAccountId(), beforePassword, afterPassword);
        Account findAccountAfter = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        //then
        assertThat(findAccountAfter.getPassword()).isNotEqualTo("bread1234");
        assertThat(passwordEncoder.matches(afterPassword, findAccountAfter.getPassword())).isTrue();
        assertThat(findAccountAfter.getUpdatedAt().compareTo(findAccountBefore.getUpdatedAt())).isEqualTo(0);

    }

    @Test
    @DisplayName("계정 권한 수정 테스트")
    void changeAuthorityTest() throws Exception {
        //given
        RequestAccountForm.CreateForm form = RequestAccountForm.CreateForm.builder()
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

        Account account = Account.createAccount()
                .loginId("noAdmin")
                .name("noAdmin")
                .password("noAdmin1234")
                .email("noAdmin@noAdmin.com")
                .contact("01011114444").build();
        Account noAdmin = accountRepository.save(account);

        //when
        ResponseAccountForm.ResponseSignupForm signupForm = accountCommandService.signUpProcessing(form);


        Account findUser = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        Account findAdmin = accountRepository.findByLoginId("admin1")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));

        accountCommandService.changeAuthority(findAdmin.getId(), findUser.getId(), Authority.ADMIN);

        Account afterChange = accountRepository.findById(signupForm.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        //then
        // 권한이 있는 계정에서 권한 변경 요청
        assertThat(afterChange.getAuthority()).isEqualTo(Authority.ADMIN);
        assertThatThrownBy(() ->
                accountCommandService.changeAuthority(noAdmin.getId(), findAdmin.getId(), Authority.SUPER_ADMIN)
        );

    }
}