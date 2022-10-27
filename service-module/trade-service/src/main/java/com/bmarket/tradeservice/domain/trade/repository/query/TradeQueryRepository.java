package com.bmarket.tradeservice.domain.trade.repository.query;

import com.bmarket.tradeservice.domain.trade.entity.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeQueryRepository {

    Page<TradeListDto> getTradeWithComplexCondition(Pageable pageable, SearchCondition searchCondition);
}
