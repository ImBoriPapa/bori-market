package com.bmarket.frmservice.repository;


import com.bmarket.frmservice.domain.ProfileImage;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProfileImageRepository extends MongoRepository<ProfileImage,String> {
}
