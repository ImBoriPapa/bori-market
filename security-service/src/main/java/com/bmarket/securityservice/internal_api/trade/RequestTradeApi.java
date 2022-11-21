package com.bmarket.securityservice.internal_api.trade;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.internal_api.trade.form.RequestTradeForm;
import com.bmarket.securityservice.internal_api.trade.form.SearchCondition;
import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RequestTradeApi {

     ResponseCreateTradeResult requestCreateTrade(Long accountId, RequestTradeForm form, List<MultipartFile> images);

     List<RequestGetTradeListResult> requestGetSaleHistory(Long accountId);

     TradeListResult requestGetTradeList(SearchCondition condition);

     TradeDetailResult requestGetTrade(Long tradeId);
}

