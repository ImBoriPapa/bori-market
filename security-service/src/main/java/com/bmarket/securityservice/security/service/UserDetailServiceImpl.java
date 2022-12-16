package com.bmarket.securityservice.security.service;

import com.bmarket.securityservice.account.domain.entity.Authority;
import com.bmarket.securityservice.account.domain.repository.AccountQueryRepository;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
     * memberId 로 Account 조회 후 반환 받은 UserDetailData 로  UserDetails 반환
     *
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        log.info("[loadUserByUsername 동작]");

        return accountQueryRepository.findAccountForLoadUser(memberId).stream()
                .map(data -> generateUser(data.getMemberId(), data.getPassword(), getAuthorityList(data.getAuthority())))
                .findFirst()
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));
    }

    private Collection<? extends GrantedAuthority> getAuthorityList(Authority authority) {
        ArrayList<SimpleGrantedAuthority> objects = new ArrayList<>();
        Arrays.asList(authority.ROLL.split(","))
                .forEach(m -> objects.add(new SimpleGrantedAuthority(m)));
        return objects;
    }

    private User generateUser(String memberId, String password, Collection<? extends GrantedAuthority> authorities) {
        return new User(memberId, password, authorities);
    }

    /**
     * loadUserByUsername() 로 생성한 UserDetails 로 Authentication 반환
     *
     * @param
     * @return
     */
    public Authentication generateAuthentication(String memberId) {
        log.info("[Authentication 생성]");
        UserDetails userDetails = loadUserByUsername(memberId);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities());
    }
}
