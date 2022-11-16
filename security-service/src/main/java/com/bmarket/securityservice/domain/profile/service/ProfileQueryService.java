package com.bmarket.securityservice.domain.profile.service;


import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.profile.controller.ProfileResultForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProfileQueryService {

    private final AccountRepository accountRepository;

    // TODO: 2022/11/16 성능 최적화
    public ProfileResultForm.profileResult getProfile(Long accountId) {
        log.info("[getProfile]");
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));


        return ProfileResultForm.profileResult.builder()
                .accountId(account.getId())
                .nickname(account.getProfile().getNickname())
                .profileImage(account.getProfile().getProfileImage())
                .addressRange(account.getProfile().getAddressRange().name())
                .addressRangeEx(account.getProfile().getAddressRange().expression)
                .fullAddress(account.getProfile().getFullAddress()).build();
    }

    // TODO: 2022/11/16 프로필 리스트 조회기능 구현
    public void getProfileList(Long accountId){

    }
}
