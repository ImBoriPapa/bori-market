package com.bmarket.securityservice.domain.trade.service;

import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.CreateTradeResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.TradeModifyResult;
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
    public CreateTradeResult create(Long accountId, RequestTradeForm.CreateTradeForm form, List<MultipartFile> images) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));

        RequestTradeServiceForm requestTradeServiceForm = RequestTradeServiceForm.builder()
                .accountId(account.getId())
                .address(account.getProfile().getAddress())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer()).build();

        return requestTradeApi.requestCreateTrade(requestTradeServiceForm, images);
    }

    @Transactional(readOnly = true)
    public TradeListResult tradeList(Long accountId, SearchCondition condition) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundAccountException(ResponseStatus.NOT_FOUND_ACCOUNT));

        condition.setAddressCode(account.getProfile().getAddress().getAddressCode());
        condition.setRange(account.getProfile().getAddressRange());

        return requestTradeApi.requestGetTradeList(condition);
    }

    public TradeContentsResult trade(Long tradeId) {

        return requestTradeApi.requestGetTrade(tradeId);
    }


    public TradeModifyResult tradeModify(Long tradeId, RequestTradeForm.ModifyTradeForm form, List<MultipartFile> images){

        return requestTradeApi.requestPatchTrade(tradeId, form, images);
    }

    public TradeDeleteResult tradeDelete(Long tradeId){
        return requestTradeApi.requestDeleteTrade(tradeId);
    }
}
