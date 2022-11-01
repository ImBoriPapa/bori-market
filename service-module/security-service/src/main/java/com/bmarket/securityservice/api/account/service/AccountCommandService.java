package com.bmarket.securityservice.api.account.service;

import com.bmarket.securityservice.api.account.controller.requestForm.RequestSignUpForm;
import com.bmarket.securityservice.api.account.controller.resultForm.SignupResult;
import com.bmarket.securityservice.api.profile.service.ProfileCommandService;

import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;

import com.bmarket.securityservice.api.profile.entity.Profile;

import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.PasswordNotCorrectException;
import com.bmarket.securityservice.exception.error_code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AccountRepository accountRepository;
    private final ProfileCommandService profileCommandService;
    private final PasswordEncoder passwordEncoder;

    public SignupResult signUpProcessing(RequestSignUpForm form) {

        Profile profile = profileCommandService.createProfile(form);

        Account account = Account.createAccount()
                .loginId(form.getLoginId())
                .name(form.getName())
                .password(passwordEncoder.encode(form.getPassword()))
                .email(form.getEmail())
                .contact(form.getContact())
                .profile(profile)
                .build();
        Account savedAccount = accountRepository.save(account);

        return new SignupResult(savedAccount.getId(), savedAccount.getCreatedAt());
    }

    // TODO: 2022/10/31 이메일 인증 구현
    public void issueTemporaryPassword(){
    }

    /**
     * 비밀 번호 변경 : clientId 로 계정 조회 -> 비밀번호 검증 후 일치시 새 비밀번호를 인코딩후 저장
     * @param clientId
     * @param password
     * @param newPassword
     */
    public void changePassword(String clientId, String password, String newPassword) {
        Account account = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));

        passwordCheck(password, account.getPassword());

        account.changePassword(passwordEncoder.encode(newPassword));
    }

    // TODO: 2022/10/31 계정 삭제시 계정에 속한 프로필, 거래, 파일 삭제 이벤트 추가
    public void deleteAccount(String clientId,String password){
        Account account = accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new BasicException(ErrorCode.NOT_FOUND_ACCOUNT));

        passwordCheck(password,account.getPassword());

        accountRepository.delete(account);

    }

    private void passwordCheck(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new PasswordNotCorrectException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
    }



}
