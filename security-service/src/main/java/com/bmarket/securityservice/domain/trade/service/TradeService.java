package com.bmarket.securityservice.domain.trade.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseTradeResult;
import com.bmarket.securityservice.exception.custom_exception.security_ex.NotFoundAccountException;
import com.bmarket.securityservice.internal_api.trade.RequestTradeApi;
import com.bmarket.securityservice.internal_api.trade.form.*;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final RequestTradeApi requestTradeApi;
    private final AccountRepository accountRepository;

    /**
     * accountId
     * nickname
     * title
     * context
     * price
     * address
     * category
     * isShare
     * isOffer
     */
    @Transactional(readOnly = true)
    public ResponseTradeResult create(Long accountId, RequestTradeForm.CreateTradeForm form, List<MultipartFile> images) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));

        CreateTradeServiceForm createTradeServiceForm = CreateTradeServiceForm.builder()
                .accountId(account.getId())
                .address(account.getProfile().getAddress())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer()).build();

        return requestTradeApi.requestCreateTrade(createTradeServiceForm, images);
    }

    @Transactional(readOnly = true)
    public TradeListDto tradeList(Long accountId, SearchCondition condition) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));

        condition.setAddressCode(account.getProfile().getAddress().getAddressCode());
        condition.setRange(account.getProfile().getAddressRange());

        return requestTradeApi.requestGetTradeList(condition);
    }

    public TradeDetailResult trade(Long tradeId) {

        return requestTradeApi.requestGetTrade(tradeId);
    }


    public ResponseTradeResult tradeModify(Long tradeId, RequestTradeForm.ModifyTradeForm form, List<MultipartFile> images){
        return requestTradeApi.requestPutTrade(tradeId, form, images);
    }

    public ResponseTradeResult tradeDelete(Long tradeId){
        return requestTradeApi.requestDeleteTrade(tradeId);
    }
}
