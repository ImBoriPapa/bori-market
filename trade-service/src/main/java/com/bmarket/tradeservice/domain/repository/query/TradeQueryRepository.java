package com.bmarket.tradeservice.domain.repository.query;


import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;

import java.util.Optional;

public interface TradeQueryRepository {
    Optional<TradeDetailDto> findTradeDetailById(Long tradeId);
    TradeListDto findTradeListWithCondition(int size , Long tradeId , SearchCondition searchCondition);
}


