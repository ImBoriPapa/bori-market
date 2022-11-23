package com.bmarket.securityservice.domain.trade.controller;


import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestCreateTradeForm;
import com.bmarket.securityservice.internal_api.trade.RequestTradeApi;
import com.bmarket.securityservice.internal_api.trade.form.RequestTradeForm;

import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.internal_api.trade.form.SearchCondition;

import com.bmarket.securityservice.internal_api.trade.form.TradeDetailResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.bmarket.securityservice.utils.jwt.SecurityHeader.AUTHORIZATION_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TradeController {
    // TODO: 2022/10/31 exception handling
    /**
     * 1. 거래 생성
     * 2. 거래 단건조회
     * 3. 거래 리스트 조회(조건)
     * 4. 거래 내역 조회
     * 5. 거래 상태 변경
     */
    private final AccountRepository accountRepository;
    private final RequestTradeApi requestTradeApi;
    private final JwtUtils jwtUtils;

    @PostMapping("/trade")
    public ResponseEntity<ResponseForm.Of> createTrade(@RequestPart RequestCreateTradeForm form,
                                                    @RequestPart List<MultipartFile> images,
                                                    HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        Long accountId = jwtUtils.getUserId(token);
        Account id = accountRepository.findById(accountId).get();

        RequestTradeForm requestTradeForm = RequestTradeForm.builder()
                .nickname(id.getProfile().getNickname())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer()).build();

        ResponseCreateTradeResult responseCreateTradeResult = requestTradeApi
                .requestCreateTrade(requestTradeForm, images);

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, responseCreateTradeResult));
    }

    @GetMapping("/trade")
    public ResponseEntity getAllTrade(SearchCondition condition,
                                      HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        Long accountId = jwtUtils.getUserId(token);
        Account id = accountRepository.findById(accountId).get();

        condition.setAddressCode(id.getProfile().getAddress().getAddressCode());
        condition.setRange(id.getProfile().getAddressRange());

        TradeListResult result = requestTradeApi.requestGetTradeList(condition);
        return ResponseEntity.ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, result));
    }

    @GetMapping("/trade/{tradeId}")
    public ResponseEntity getOneTrade(@PathVariable Long tradeId) {
        TradeDetailResult result = requestTradeApi.requestGetTrade(tradeId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/trade/sale-list")
    public ResponseEntity getSaleList(HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        Long accountId = jwtUtils.getUserId(token);
        Account id = accountRepository.findById(accountId).get();

        List<RequestGetTradeListResult> requestGetTradeListResults = requestTradeApi.requestTradeListByAccountId(id.getId());

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, requestGetTradeListResults));
    }
}
