package com.bmarket.securityservice.domain.profile.service;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.entity.Profile;

import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.internal_api.RequestFrmApi;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfileCommandService {
    private final AccountRepository accountRepository;

    private final RequestFrmApi requestFrmApi;

    /**
     * createProfile() 은 Account 를 저장시 같이 저장하기 때문에 저장로직이 없음
     * form 의 정보로 Address 와 Profile 객체를 생성해 전달
     * 프로필 이미지는: getDefaultProfileImage() 최초 가입시 기본 이미지 저장
     */
    public Profile createProfile(RequestAccountForm.CreateForm form) {

        Address address = Address.createAddress()
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .town(form.getTown()).build();

        return Profile.createProfile()
                .nickname(form.getNickname())
                .profileImage(requestFrmApi.getDefaultProfileImage())
                .address(address)
                .build();
    }

    /**
     * 계정에 저장된 프로필을 찾아 닉네임 수정
     */
    public void updateNickname(Long accountId, String newNick) {
        findAccount(accountId)
                .getProfile().updateNickname(newNick);
    }

    /**
     * 프로필 이미지 수정
     * 프로필 이미지 파일의 처리와 저장은 f.r.m service 의 위임 하기 위하여 getProfileImage()을 사용
     */
    public void updateProfileImage(Long accountId, MultipartFile file) {
        Account account = findAccount(accountId);

        String profileImage = requestFrmApi.requestSaveImage(accountId, file);

        account.getProfile().updateProfileImage(profileImage);
    }

    public void deleteProfileImage(Long accountId){
        Account account = findAccount(accountId);
        String result = requestFrmApi.requestDeleteImage(accountId);
        account.getProfile().updateProfileImage(result);
    }

    /**
     * 계정에 저장된 프로필의 주소 수정
     */
    public void updateAddress(Long accountId, Address address) {
        findAccount(accountId)
                .getProfile().updateAddress(address);
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));
    }

    /**
     * 주소 검색 범위 수정
     */
    public void updateAddressSearchRange(Long accountId, AddressRange range) {
        findAccount(accountId)
                .getProfile().updateAddressRange(range);
    }


}
