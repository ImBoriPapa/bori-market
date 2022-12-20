package com.bmarket.tradeservice.domain.repository;

import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeImageRepository extends JpaRepository<TradeImage,Long> {
    List<TradeImage> findAllByTrade(Trade trade);
}
