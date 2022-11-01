package com.bmarket.securityservice.api.account.repository;

import com.bmarket.securityservice.api.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByClientId(String clientId);

    Optional<Account> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);

}
