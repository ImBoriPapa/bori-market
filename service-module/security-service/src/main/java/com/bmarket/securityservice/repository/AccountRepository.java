package com.bmarket.securityservice.repository;

import com.bmarket.securityservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByClientId(String clientId);


    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByContact(String contact);
}
