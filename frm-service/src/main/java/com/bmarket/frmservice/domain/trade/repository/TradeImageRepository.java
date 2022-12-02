package com.bmarket.frmservice.domain.trade.repository;

import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TradeImageRepository extends MongoRepository<TradeImage,String> {

}
