package com.bmarket.securityservice.domain.account.repository;

import com.bmarket.securityservice.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByClientId(String clientId);
    Optional<Account> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String loginId);

    boolean existsByContact(String loginId);

    boolean existsByClientId(String clientId);
}
