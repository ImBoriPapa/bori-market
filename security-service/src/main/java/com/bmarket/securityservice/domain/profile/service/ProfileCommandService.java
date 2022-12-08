package com.bmarket.securityservice.domain.profile.service;

import com.bmarket.securityservice.domain.account.controller.RequestAccountForm;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.Address;
import com.bmarket.securityservice.domain.address.AddressRange;
import com.bmarket.securityservice.domain.profile.controller.RequestProfileForm;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.internal_api.frm.RequestFrmApi;
import com.bmarket.securityservice.internal_api.frm.ResponseImageForm;
import com.bmarket.securityservice.internal_api.trade.RequestTradeApi;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        log.info("[createProfile 동작]");
        Address address = Address.createAddress()
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .town(form.getTown()).build();

        ResponseImageForm profileImage = requestFrmApi.postProfileImage();
        log.info("[requestFrmApi 호출]");
        log.info("[requestFrmApi 응답 결과 success= {}]",profileImage.getSuccess());
        log.info("[requestFrmApi 응답 결과 imageId= {}]",profileImage.getImageId());
        log.info("[requestFrmApi 응답 결과 imagePath= {}]",profileImage.getImagePath());

        return Profile.createProfile()
                .nickname(form.getNickname())
                .imageId(profileImage.getImageId())
                .profileImage(profileImage.getImagePath())
                .address(address)
                .build();
    }

    /**
     * 계정에 저장된 프로필을 찾아 닉네임 수정
     */
    public Long updateNickname(Long accountId, RequestProfileForm.UpdateNickname form) throws JsonProcessingException {
        Account account = findAccount(accountId);
        account.getProfile().updateNickname(form.getNickname());
        return account.getId();
    }

    /**
     * 1.프로필 이미지 수정
     */
    // TODO: 2022/11/23 조건 추가  2. trade 에 생성된 거래가 없을 경우 또는 에러가 발생할 경우
    public Long updateProfileImage(Long accountId, MultipartFile file) {
        Account account = findAccount(accountId);
        String imageId = account.getProfile().getImageId();

        ResponseImageForm profileImage = requestFrmApi.putProfileImage(imageId, file);


        account.getProfile().updateProfileImage(profileImage.getImageId(),profileImage.getImagePath());

        return accountId;
    }

    /**
     * 계정에 저장된 프로필의 주소 수정
     */
    public Long updateAddress(Long accountId, Address address) {
        findAccount(accountId)
                .getProfile().updateAddress(address);
        return accountId;
    }

    /**
     * 주소 검색 범위 수정
     */
    public Long updateAddressSearchRange(Long accountId, AddressRange range) {
        findAccount(accountId)
                .getProfile().updateAddressRange(range);
        return accountId;
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));
    }


}
