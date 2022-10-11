package com.bmarket.securityservice.domain.repository;

import com.bmarket.securityservice.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByClientId(String clientId);

    Optional<Account> findByLoginId(String loginId);


    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByContact(String contact);
}
