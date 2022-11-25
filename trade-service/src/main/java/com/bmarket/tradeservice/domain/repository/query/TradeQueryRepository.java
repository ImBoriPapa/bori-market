package com.bmarket.tradeservice.domain.repository.query;


import com.bmarket.tradeservice.domain.repository.query.dto.TradeListDto;

import java.util.List;

public interface TradeQueryRepository {

    ResponseResult<List<TradeListDto>> getTradeWithComplexCondition(int size , Long tradeId , SearchCondition searchCondition);
}
