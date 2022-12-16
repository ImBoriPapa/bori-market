package com.bmarket.securityservice.account.domain.service;

import com.bmarket.securityservice.account.domain.entity.Address;
import com.bmarket.securityservice.account.api.RequestAccountForm;
import com.bmarket.securityservice.account.domain.entity.Account;
import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.AccountRepository;
import com.bmarket.securityservice.exception.custom_exception.BasicException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.exception.custom_exception.security_ex.PasswordNotCorrectException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
// TODO: 2022/11/02 1차 리펙토링 완
/**
 *  계정 서비스 로직중 command 에 해당 하는 로직 Transactional 안에서 변경이 일어나야 한다.
 *  create
 *  delete
 *  update
 */
public class AccountCommandService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account signUpProcessing(RequestAccountForm.CreateForm form) {

        Address address = Address.createAddress()
                .addressCode(form.getAddressCode())
                .city(form.getCity())
                .district(form.getDistrict())
                .town(form.getTown())
                .build();

        Account account = Account.createAccount()
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .name(form.getName())
                .nickName(form.getNickname())
                .contact(form.getContact())
                .address(address)
                .authority(Authority.USER)
                .build();

        return accountRepository.save(account);

    }

    /**
     * 비밀 번호 변경 : clientId 로 계정 조회 -> 비밀번호 검증 후 일치시 새 비밀번호를 인코딩후 저장
     *
     * @param accountId
     * @param password
     * @param newPassword
     */
    public Long updatePassword(Long accountId, String password, String newPassword) {
        Account account = findAccount(accountId);

        passwordCheck(password, account.getPassword());

        account.changePassword(passwordEncoder.encode(newPassword));

        return accountId;
    }

    // TODO: 2022/11/02 SMS 인증 구현 공부하기
    public void updateContact(Long accountId, String contact) {
        findAccount(accountId)
                .updateContact(contact);
    }

    // TODO: 2022/11/02 email 인증 구현 공부하기

    public void updateEmail(Long accountId, String email) {
        log.info("[이메일 변경]");
        findAccount(accountId).updateEmail(email);
    }

    // TODO: 2022/11/02 계정 삭제시 계정에 속한, 거래, 파일 삭제 이벤트 추가

    /**
     * 계정 삭제
     *
     * @param accountId
     * @param password
     */
    public void deleteAccount(Long accountId, String password) {
        Account account = findAccount(accountId);
        passwordCheck(password, account.getPassword());
        accountRepository.delete(account);
    }


    /**
     * 권한 변경
     *
     * @param adminId
     * @param accountId
     * @param authority
     */
    public void changeAuthority(Long adminId, Long accountId, Authority authority) {
        Account admin = findAccount(adminId);

        if (admin.getAuthority() == Authority.USER) {
            throw new BasicException(ResponseStatus.ACCESS_DENIED);
        }
        findAccount(accountId).updateAuthority(authority);
    }

    /**
     * 계정 조회 : 서비스 레이어 안에서만 조회 용도의 단순 계정 조회
     *
     * @param accountId
     * @return
     */
    protected Account findAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));
        return account;
    }

    /**
     * 비밀번호 일치 확인
     *
     * @param password
     * @param encodedPassword
     */
    private void passwordCheck(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new PasswordNotCorrectException(ResponseStatus.NOT_CORRECT_PASSWORD);
        }
    }
}

