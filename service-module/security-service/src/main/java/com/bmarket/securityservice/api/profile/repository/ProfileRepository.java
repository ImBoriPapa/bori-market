package com.bmarket.securityservice.api.profile.repository;

import com.bmarket.securityservice.api.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Profile findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Boolean existsByEmail(String email);
    Boolean existsByContact(String contact);
}
