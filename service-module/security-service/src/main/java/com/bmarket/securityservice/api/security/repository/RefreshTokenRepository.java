package com.bmarket.securityservice.api.security.repository;

import com.bmarket.securityservice.api.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

}
