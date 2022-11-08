package com.bmarket.tradeservice.domain.trade.repository;

import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.entity.TradeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeImageRepository extends JpaRepository<TradeImage,Long> {
    List<TradeImage> findByTrade(Trade trade);
}
