package com.bmarket.securityservice.domain.account.service;

import com.bmarket.securityservice.api.controller.external_spec.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.dto.SignupResult;
import com.bmarket.securityservice.domain.address.entity.Address;
import com.bmarket.securityservice.domain.address.repository.AddressRepository;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.entity.AddressStatus;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
/**
 *  계정 서비스 로직중 command 에 해당 하는 로직 Transactional 안에서 변경이 일어나야 한다.
 *  create
 *  delete
 *  update
 */
public class AccountCommandService {


    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResult signUpProcessing(RequestSignUpForm form) {

        String profileImage = getProfileImage();
        Profile profile = Profile.createProfile()
                .nickname(form.getNickname())
                .email(form.getEmail())
                .contact(form.getContact())
                .profileImage(profileImage)
                .build();
        Profile savedProfile = profileRepository.save(profile);

        Address address = Address.createAddress()
                .addressStatus(AddressStatus.FIRST)
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .profile(savedProfile)
                .town(form.getTown()).build();
        Address saveAddress = addressRepository.save(address);

        Account account = Account.createAccount()
                .loginId(form.getLoginId())
                .name(form.getName())
                .password(passwordEncoder.encode(form.getPassword()))
                .profile(savedProfile)
                .build();
        Account savedAccount = accountRepository.save(account);


        return new SignupResult(savedAccount.getClientId(), savedAccount.getCreatedAt());
    }

    public String getProfileImage(){
        String profileImage = WebClient.create()
                .get()
                .uri("http://localhost:8095/frm/profile/default")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return profileImage;
    }
}
