package com.bmarket.securityservice.domain.profile.entity;


import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class ProfileTest {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("프로필 생성 테스트")
    void createProfileTest() throws Exception{
        //given
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("서울")
                .district("영등포구")
                .town("여의도동").build();

        Profile profile = Profile.createProfile()
                .nickname("nickname")
                .profileImage("http://localhost:8080/frm/default.jpg")
                .address(address).build();
        //when
        Profile savedProfile = profileRepository.save(profile);
        Profile findProfile = profileRepository.findById(savedProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
        //then
        assertThat(findProfile.getId()).isEqualTo(savedProfile.getId());
        assertThat(findProfile.getNickname()).isEqualTo(findProfile.getNickname());
        assertThat(findProfile.getProfileImage()).isEqualTo(savedProfile.getProfileImage());
        assertThat(findProfile.getAddress()).isExactlyInstanceOf(address.getClass());
        assertThat(findProfile.getAddressRange()).isEqualTo(AddressRange.ONLY);
    }

    @Test
    @DisplayName("프로필 수정 테스트")
    void profileUpdateTest() throws Exception{
        //given
        Address address = Address.createAddress()
                .addressCode(1001)
                .city("서울")
                .district("영등포구")
                .town("여의도동").build();

        Profile profile = Profile.createProfile()
                .nickname("nickname")
                .profileImage("http://localhost:8080/frm/default.jpg")
                .address(address).build();
        Profile savedProfile = profileRepository.save(profile);
        savedProfile.updateNickname("변경된 닉네임");
        savedProfile.updateProfileImage("isChanged");
        Address newAddress = Address.createAddress()
                .addressCode(1005)
                .city("인천시")
                .district("계양구")
                .town("주월동").build();
        savedProfile.updateAddress(newAddress);
        savedProfile.updateAddressRange(AddressRange.FIVE);
        //when
        Profile findProfile = profileRepository.findById(savedProfile.getId())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을수 없습니다."));
        //then
        assertThat(findProfile.getNickname()).isEqualTo("변경된 닉네임");
        assertThat(findProfile.getProfileImage()).isEqualTo("isChanged");
        assertThat(findProfile.getAddress().getCity()).isEqualTo("인천시");
        assertThat(findProfile.getAddressRange()).isEqualTo(AddressRange.FIVE);
    }
}