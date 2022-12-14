package com.bmarket.frmservice.domain.profile.repository;


import com.bmarket.frmservice.domain.profile.entity.ProfileImage;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProfileImageRepository extends MongoRepository<ProfileImage,String> {

}
