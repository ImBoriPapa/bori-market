package com.bmarket.securityservice.security.repository;

import com.bmarket.securityservice.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

}
