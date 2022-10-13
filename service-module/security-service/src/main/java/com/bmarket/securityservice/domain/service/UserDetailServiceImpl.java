package com.bmarket.securityservice.domain.service;

import com.bmarket.securityservice.domain.entity.Account;
import com.bmarket.securityservice.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
        log.info("loadUserByUsername 이 호출 되었습니다");
        Account account = accountRepository.findByClientId(clientId).orElseThrow(() -> new IllegalArgumentException("NOT FOUND ACCOUNT"));
        return generateUser(account);
    }

    private  User generateUser(Account account) {
        return new User(account.getClientId(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getAuthority().name())));
    }

    public Authentication generateAuthenticationByClientId(String clientId) {
        log.info("generateAuthenticationByClientId is call");
        UserDetails userDetails = loadUserByUsername(clientId);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities());
    }


}
