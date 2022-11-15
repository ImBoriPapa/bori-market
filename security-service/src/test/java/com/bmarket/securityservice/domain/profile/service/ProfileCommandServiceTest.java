package com.bmarket.securityservice.domain.profile.service;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ProfileCommandServiceTest {

    @Autowired
    ProfileCommandService profileCommandService;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;
    public MockWebServer mockWebServer;

    @BeforeEach
    void beforeEach() throws IOException {
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("서울")
                .district("종로구")
                .town("종로동").build();
        Profile profile = Profile.createProfile()
                .nickname("변경전")
                .address(address)
                .build();
        Account account = Account.createAccount()
                .loginId("login")
                .name("name")
                .password("1234")
                .email("email@email.com")
                .contact("010-1213-1213")
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);
        mockWebServer = new MockWebServer();
        mockWebServer.start(8095);
        mockWebServer.url("/frm/profile");


    }

    @AfterEach
    void afterEach() throws IOException {
        accountRepository.deleteAll();
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("프로필 생성 테스트")
    void createProfileTest() throws Exception {
        //given
        RequestAccountForm.CreateForm createForm = RequestAccountForm.CreateForm.builder()
                .name("고길동")
                .loginId("KoKillDong")
                .password("12345678")
                .nickname("swordMaster")
                .email("goKill@goKill.com")
                .contact("010-2323-2424")
                .addressCode(1001)
                .city("서울")
                .district("무슨구")
                .town("무슨동").build();
        //when
        Profile profile = profileCommandService.createProfile(createForm);

        Profile saveProfile = profileRepository.save(profile);

        Profile findProfile = profileRepository.findById(saveProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
        //then
        log.info("result={}", findProfile.getProfileImage());
        assertThat(findProfile.getId()).isEqualTo(saveProfile.getId());
        assertThat(findProfile.getNickname()).isEqualTo(createForm.getNickname());
        assertThat(findProfile.getAddressRange()).isEqualTo(findProfile.getAddressRange());
        assertThat(findProfile.getAddress().getAddressCode()).isEqualTo(createForm.getAddressCode());
        assertThat(findProfile.getAddress().getCity()).isEqualTo(createForm.getCity());
        assertThat(findProfile.getAddress().getDistrict()).isEqualTo(createForm.getDistrict());
        assertThat(findProfile.getAddress().getTown()).isEqualTo(createForm.getTown());

    }

    @Test
    @DisplayName("닉네임 수정 테스트")
    void updateNicknameTest() throws Exception {
        //given
        Account account = accountRepository.findByLoginId("login")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        String beforeNick = account.getProfile().getNickname();
        //when

        String newNick = "변경후";

        profileCommandService.updateNickname(account.getId(), newNick);

        Profile after = profileRepository.findByNickname(newNick)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
        //then
        assertThat(after.getId()).isEqualTo(account.getProfile().getId());
        assertThat(after.getNickname()).isNotEqualTo(beforeNick);
        assertThat(after.getNickname()).isEqualTo(newNick);
    }

    @Test
    @DisplayName("주소 수정 테스트")
    void updateAddressTest() throws Exception {
        //given
        Account account = accountRepository.findByLoginId("login")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));

        Address beforeAddress = account.getProfile().getAddress();

        Address afterAddress = Address.createAddress()
                .addressCode(1003)
                .city("부산")
                .district("부산구")
                .town("부산동").build();
        //when
        profileCommandService.updateAddress(account.getId(), afterAddress);

        Profile findProfile = profileRepository.findById(account.getProfile().getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을수 없습니다."));
        //then
        assertThat(findProfile.getAddress()).isEqualTo(afterAddress);
        assertThat(findProfile.getAddress()).isNotEqualTo(beforeAddress);
    }

    @Test
    @DisplayName("주소 검색 범위 수정 테스트")
    void updateAddressSearchRangeTest() throws Exception {
        //given
        Account account = accountRepository.findByLoginId("login")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
        AddressRange beforeRange = account.getProfile().getAddressRange();
        //when
        AddressRange newRange = AddressRange.TEN;
        profileCommandService.updateAddressSearchRange(account.getId(), newRange);
        Profile findProfile = profileRepository.findById(account.getProfile().getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을수 없습니다."));

        //then
        assertThat(findProfile.getAddressRange()).isNotEqualTo(beforeRange);
        assertThat(findProfile.getAddressRange()).isEqualTo(newRange);
    }

    // TODO: 2022/11/15 테스트 보강
    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateProfileImageTest() throws Exception{
        //given
        Account account = accountRepository.findByLoginId("login")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));

        MockMultipartFile file = new MockMultipartFile("test", "test.jpg", "jpg", "test".getBytes());
        String response = "test";

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(response);
        mockWebServer.enqueue(mockResponse);
        profileCommandService.updateProfileImage(account.getId(),file);

        //when
        Profile findProfile = profileRepository.findById(account.getProfile().getId())
                .orElseThrow(() -> new IllegalArgumentException("ds"));
        log.info("updated image name= {}",findProfile.getProfileImage());
        //then

    }

}