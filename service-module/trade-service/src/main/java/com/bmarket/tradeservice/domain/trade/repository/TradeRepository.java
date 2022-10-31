package com.bmarket.tradeservice.domain.trade.repository;

import com.bmarket.tradeservice.domain.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade,Long> {
    Optional<Trade> findByAccountId(Long accountId);
}
