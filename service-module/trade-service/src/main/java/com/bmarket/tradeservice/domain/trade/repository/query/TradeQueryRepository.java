package com.bmarket.tradeservice.domain.trade.repository.query;


import com.bmarket.tradeservice.domain.trade.repository.query.dto.TradeListDto;

import java.util.List;

public interface TradeQueryRepository {

    ResponseResult<List<TradeListDto>> getTradeWithComplexCondition(int size , Long tradeId , SearchCondition searchCondition);
}
