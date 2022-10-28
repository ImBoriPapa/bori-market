package com.bmarket.tradeservice.domain.trade.repository.query;


import java.util.List;

public interface TradeQueryRepository {

    ResponseResult<List<TradeListDto>> getTradeWithComplexCondition(int size , Long tradeId , SearchCondition searchCondition);
}
