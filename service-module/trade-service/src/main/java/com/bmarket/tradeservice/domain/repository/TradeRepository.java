package com.bmarket.tradeservice.domain.repository;

import com.bmarket.tradeservice.domain.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade,Long> {
}
