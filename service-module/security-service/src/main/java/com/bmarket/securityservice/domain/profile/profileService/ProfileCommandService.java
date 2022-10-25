package com.bmarket.securityservice.domain.profile.profileService;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.profile.entity.Address;
import com.bmarket.securityservice.domain.profile.entity.AddressSearchRange;
import com.bmarket.securityservice.domain.profile.entity.Profile;

import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;


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
                .email(form.getEmail())
                .contact(form.getContact())
                .profileImage(getDefaultProfileImage())
                .address(address)
                .build();
        return profileRepository.save(profile);
    }

    public void updateNickname(String clientId, String nickname) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateNickname(nickname);
    }

    public void updateEmail(String clientId, String email) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateEmail(email);
    }

    public void updateContact(String clientId, String contact) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().updateContact(contact);
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

    public void updateAddressSearchRange(String clientId, AddressSearchRange range) {
        accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException(""))
                .getProfile().settingAddressSearchRange(range);
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
