package com.bmarket.securityservice.api.profile.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.address.Address;
import com.bmarket.securityservice.api.address.AddressRange;
import com.bmarket.securityservice.api.profile.entity.Profile;

import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfileCommandService {
    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    public Profile createProfile(RequestSignUpForm form) {

        Address address = Address.createAddress()
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .town(form.getTown()).build();

        Profile profile = Profile.createProfile()
                .nickname(form.getNickname())
                .profileImage(getDefaultProfileImage())
                .address(address)
                .build();
        return profile;
    }

    public void updateNickname(String clientId, String nickname) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateNickname(nickname);
    }

    public void updateProfileImage(String clientId, MultipartFile file) {
        Account optional = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        Long id = optional.getProfile().getId();

        String profileImage = getProfileImage(id, file);

        optional.getProfile().updateProfileImage(profileImage);
    }

    public void updateAddress(String clientId, Address address) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateAddress(address);
    }

    public void updateAddressSearchRange(String clientId, AddressRange range) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateAddressRange(range);
    }

    private String getDefaultProfileImage() {
        String profileImage = WebClient.create()
                .get()
                .uri("http://localhost:8095/frm/profile/default")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return profileImage;
    }

    private String getProfileImage(Long profileId, MultipartFile file) {
        Resource resource = file.getResource();
        String profileImage = WebClient.create()
                .post()
                .uri("http://localhost:8095/frm/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData("accountId", profileId)
                        .with("image", resource))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return profileImage;
    }


}
