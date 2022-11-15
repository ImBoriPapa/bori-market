package com.bmarket.securityservice.domain.profile.repository;

import com.bmarket.securityservice.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByNickname(String nickname);
}
