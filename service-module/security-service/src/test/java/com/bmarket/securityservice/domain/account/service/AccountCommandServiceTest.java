package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.entity.Authority;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.address.AddressRange;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
    @Autowired
    EntityManager em;

    @BeforeEach
    void beforeEach() {
        Account testAdmin = Account.createAccount()
                .loginId("admin")
                .email("admin@admin.com")
                .contact("010-1213-4643")
                .password("admin123")
                .name("관리자1")
                .build();
        Account save1 = accountRepository.save(testAdmin);
        save1.updateAuthority(Authority.ROLL_ADMIN);

        Account testUser = Account.createAccount()
                .loginId("user")
                .email("user@v.com")
                .contact("010-5213-4643")
                .password("user123")
                .name("사용자1")
                .build();
        Account save2 = accountRepository.save(testUser);

        em.flush();
        em.clear();
        System.out.println("================BEFORE EACH================");

    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 성공 테스트")
    void successCreateTest() throws Exception {
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
    @DisplayName("계정 삭제 테스트")
    void updateAccountTest() throws Exception {
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
        accountCommandService.deleteAccount(signupResult.getAccountId(), "bread1234");

        //then
        assertThatThrownBy(() ->
                accountRepository.findById(signupResult.getAccountId())
                        .orElseThrow(() -> new IllegalArgumentException("계정을 찾지 못했습니다.")))
                .isExactlyInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                profileRepository.findById(signupResult.getAccountId())
                        .orElseThrow(() -> new IllegalArgumentException("프로필을 찾지 못했습니다")))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("계정 비밀번호 수정 테스트")
    void updatePasswordTest() throws Exception {
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
        String beforePassword = form.getPassword();
        String afterPassword = "new1234";
        //when
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        Account findAccountBefore = accountRepository.findById(signupResult.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        accountCommandService.updatePassword(signupResult.getAccountId(), beforePassword, afterPassword);
        Account findAccountAfter = accountRepository.findById(signupResult.getAccountId())
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
        Account findUser = accountRepository.findById(signupResult.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        Account findAdmin = accountRepository.findByLoginId("admin")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        Account anotherUser = accountRepository.findByLoginId("user")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));

        accountCommandService.changeAuthority(findAdmin.getId(), findUser.getId(), Authority.ROLL_ADMIN);
        Account afterChange = accountRepository.findById(signupResult.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을수 없습니다."));
        //then
        // 권한이 있는 계정에서 권한 변경 요청
        assertThat(afterChange.getAuthority()).isEqualTo(Authority.ROLL_ADMIN);
        // 권한이 없는 계정에서 권한 변경 요청
        assertThatThrownBy(() ->
                accountCommandService.changeAuthority(anotherUser.getId(), afterChange.getId(), Authority.ROLL_USER)
        ).isExactlyInstanceOf(BasicException.class);
    }
}