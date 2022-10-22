package com.bmarket.securityservice.domain.address.repository;

import com.bmarket.securityservice.domain.address.entity.Address;
import com.bmarket.securityservice.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByProfile(Profile profile);
}
