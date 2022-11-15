package com.bmarket.securityservice.domain.profile.service;


import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileQueryService {

    private final ProfileRepository profileRepository;


    public void getProfile(String clientId){



    }
}
