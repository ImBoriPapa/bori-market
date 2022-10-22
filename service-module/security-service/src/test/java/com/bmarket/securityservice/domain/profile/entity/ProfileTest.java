package com.bmarket.securityservice.domain.profile.entity;

import com.bmarket.securityservice.domain.address.repository.AddressRepository;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
@Slf4j
class ProfileTest {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("프로필 생성")
    void createProfile() throws Exception{

        Profile profile = Profile.createProfile()
                .nickname("ninkname")
                .email("email")
                .contact("dsadsa")
                .profileImage("Fsfa").build();
        Profile save = profileRepository.save(profile);


    }

}