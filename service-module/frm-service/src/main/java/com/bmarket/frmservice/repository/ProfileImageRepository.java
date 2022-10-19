package com.bmarket.frmservice.repository;


import com.bmarket.frmservice.domain.ProfileImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface ProfileImageRepository extends MongoRepository<ProfileImage,String> {
    Optional<ProfileImage> findByAccountId(Long accountId);
}
