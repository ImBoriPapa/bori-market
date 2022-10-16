package com.bmarket.securityservice.domain.jwt.repository;

import com.bmarket.securityservice.domain.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByClientId(String name);
}
