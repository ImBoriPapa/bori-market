package com.bmarket.securityservice.domain.security.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.entity.Authority;
import com.bmarket.securityservice.domain.account.repository.AccountQueryRepository;


import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AccountQueryRepository 의 findAccountForLoadUser() 로 UserDetailData 를 받아 UserDetails 생성
 * findAccountForLoadUser() = Transaction(readOnly=true)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountQueryRepository accountQueryRepository;

    /**
     * clientId 로 Account 조회 후 반환 받은 UserDetailData 로  UserDetails 반환
     *
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        log.info("[loadUserByUsername 동작]");
        return accountQueryRepository.findAccountForLoadUser(Long.valueOf(accountId))
                .stream()
                .map(data -> generateUser(String.valueOf(data.getAccountId()), data.getPassword(), data.getAuthority()))
                .findFirst()
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));
    }

    private User generateUser(String accountId, String password, Authority authorities) {
        return new User(accountId, password, Account.getAuthorityList(authorities));
    }

    /**
     * loadUserByUsername() 로 생성한 UserDetails 로 Authentication 반환
     * @param
     * @return
     */
    public Authentication generateAuthentication(Long accountId) {
        log.info("[Authentication 생성]");
        UserDetails userDetails = loadUserByUsername(String.valueOf(accountId));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities());
    }
}
