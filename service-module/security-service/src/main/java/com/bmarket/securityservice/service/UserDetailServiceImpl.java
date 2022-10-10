package com.bmarket.securityservice.service;

import com.bmarket.securityservice.entity.Account;
import com.bmarket.securityservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
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
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
        Account account = accountRepository.findByClientId(clientId).orElseThrow(() -> new IllegalArgumentException("NOT FOUND ACCOUNT"));
        return generateUser(account);
    }

    private  User generateUser(Account account) {
        return new User(account.getClientId(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getAuthority().name())));
    }

    public Authentication generateAuthentication(String memberId) {
        UserDetails userDetails = loadUserByUsername(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities());
    }
}
