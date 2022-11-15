package com.bmarket.securityservice.domain.security.repository;

import com.bmarket.securityservice.domain.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

}
