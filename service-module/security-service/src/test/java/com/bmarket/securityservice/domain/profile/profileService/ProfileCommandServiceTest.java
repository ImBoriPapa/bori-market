package com.bmarket.securityservice.domain.profile.profileService;


import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.account.service.AccountCommandService;
import com.bmarket.securityservice.api.profile.service.ProfileCommandService;
import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.profile.entity.Profile;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class ProfileCommandServiceTest {
    @Autowired
    AccountCommandService accountCommandService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileCommandService profileCommandService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EntityManager em;



    @BeforeEach
    void beforeEach() {
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("before")
                .password("password1234")
                .email("before@before.com")
                .nickname("beforeName")
                .contact("010-0000-0000")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        //when

    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        profileRepository.deleteAll();
    }


    @Test
    @DisplayName("프로필 생성 성공 테스트")
    void successCreateProfile() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        Profile newProfile = profileCommandService.createProfile(form);
        //when
        Profile findProfile = profileRepository.findById(newProfile.getId()).get();
        //then
        assertThat(findProfile.getId()).isEqualTo(newProfile.getId());
        assertThat(findProfile.getNickname()).isEqualTo(newProfile.getNickname());
        assertThat(findProfile.getEmail()).isEqualTo(newProfile.getEmail());
        assertThat(findProfile.getContact()).isEqualTo(newProfile.getContact());
        assertThat(findProfile.getProfileImage()).isEqualTo(newProfile.getProfileImage());
        assertThat(findProfile.getAddress().getAddressCode()).isEqualTo(newProfile.getAddress().getAddressCode());
        assertThat(findProfile.getAddress().getCity()).isEqualTo(newProfile.getAddress().getCity());
        assertThat(findProfile.getAddress().getDistrict()).isEqualTo(newProfile.getAddress().getDistrict());
        assertThat(findProfile.getAddress().getTown()).isEqualTo(newProfile.getAddress().getTown());
        assertThat(findProfile.getAddressRange()).isEqualTo(newProfile.getAddressRange());
    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNickname() throws Exception {
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        String clientId = signupResult.getClientId();
        //when
        profileCommandService.updateNickname(clientId, "newNick");
        //then
        Boolean after = profileRepository.existsByNickname("newNick");

        assertThat(after).isTrue();

    }

    @Test
    @DisplayName("이메일 변경 테스트")
    void updateEmail() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        String clientId = signupResult.getClientId();
        //when
        profileCommandService.updateEmail(clientId,"new@new.com");
        //then
        Boolean after = profileRepository.existsByEmail("new@new.com");

        assertThat(after).isTrue();
    }

    @Test
    @DisplayName("연락처 변경 테스트")
    void updateContact() throws Exception{
        //given
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        String clientId = signupResult.getClientId();
        //when
        profileCommandService.updateContact(clientId,"010-1111-1111");
        //then
        Boolean after = profileRepository.existsByContact("010-1111-1111");
        assertThat(after).isTrue();

    }

    @Test
    @DisplayName("프로필 이미지 변경 테스트")
    void updateProfileImage() throws Exception{
        //given
        MockMultipartFile file = new MockMultipartFile("test", "test.jpg", "jpg", "fafafa".getBytes());
        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        String clientId = signupResult.getClientId();

        //when
        profileCommandService.updateProfileImage(clientId,file);
        //then


    }

    @Test
    @DisplayName("주소 변경 테스트")
    void updateAddress() throws Exception{
        //given
        Address address = Address.createAddress()
                .addressCode(1111)
                .city("뉴욕")
                .district("멘허튼")
                .town("다운타운").build();

        RequestSignUpForm form = RequestSignUpForm.builder()
                .loginId("loginId")
                .password("password1234")
                .email("email@email.com")
                .nickname("nickname")
                .contact("010-2133-1313")
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("동네1").build();
        SignupResult signupResult = accountCommandService.signUpProcessing(form);
        String clientId = signupResult.getClientId();

        //when
        profileCommandService.updateAddress(clientId,address);

        //then

    }


}