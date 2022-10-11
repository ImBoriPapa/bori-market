package com.bmarket.securityservice.domain.repository;

import com.bmarket.securityservice.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByClientId(String name);
}
