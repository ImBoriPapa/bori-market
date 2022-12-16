package com.bmarket.securityservice.account.domain.repository;

import com.bmarket.securityservice.account.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByContact(String email);

    Optional<Account> findByMemberId(String memberId);
}
