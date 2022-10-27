package com.bmarket.tradeservice.domain.trade.repository;

import com.bmarket.tradeservice.domain.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade,Long> {
}
