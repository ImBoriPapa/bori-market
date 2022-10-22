package com.bmarket.securityservice.domain.address.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.address.entity.Address;
import com.bmarket.securityservice.domain.address.repository.AddressRepository;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import com.bmarket.securityservice.domain.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AddressCommandService {

    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;

    public void addSecondAddress(String clientId,Address address){
        Profile profile = profileRepository.findByClientId(clientId).orElseThrow(() -> new IllegalArgumentException("d"));
        List<Address> addressList = addressRepository.findByProfile(profile);
        if(addressList.contains(address)){
            log.info("이미 저장된 주소");
        }
        addressList.add(1,address);
    }

}
