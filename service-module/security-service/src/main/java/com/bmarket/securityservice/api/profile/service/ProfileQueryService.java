package com.bmarket.securityservice.api.profile.service;

import com.bmarket.securityservice.api.profile.repository.ProfileQueryRepository;
import com.bmarket.securityservice.api.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileQueryService {

    private final ProfileRepository profileRepository;
    private final ProfileQueryRepository queryRepository;

    public void getProfile(String clientId){



    }
}
