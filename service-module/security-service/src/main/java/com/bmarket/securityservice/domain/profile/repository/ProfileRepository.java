package com.bmarket.securityservice.domain.profile.repository;

import com.bmarket.securityservice.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
