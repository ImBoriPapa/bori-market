package com.bmarket.securityservice.domain.trade.controller;


import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestCreateTradeForm;
import com.bmarket.securityservice.domain.trade.entity.Category;
import com.bmarket.securityservice.domain.trade.service.form.RequestTradeForm;

import com.bmarket.securityservice.domain.trade.service.RequestTradeApi;
import com.bmarket.securityservice.domain.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.domain.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.domain.account.entity.Account;
import com.bmarket.securityservice.domain.account.repository.AccountRepository;
import com.bmarket.securityservice.domain.trade.service.form.SearchCondition;

import com.bmarket.securityservice.domain.trade.service.form.TradeDetailResult;
import com.bmarket.securityservice.domain.trade.service.form.TradeListResult;
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
    private final AccountRepository accountRepository;
    private final RequestTradeApi requestTradeApi;
    private final JwtUtils jwtUtils;

    @GetMapping("/trade/category")
    public ResponseEntity getCategoryList(){

        return ResponseEntity
                .ok()
                .body(Category.values());
    }

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
                .requestCreateTrade(accountId,requestTradeForm, images);

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

        TradeListResult result = requestTradeApi.RequestGetTradeList(condition).getBody();
        return ResponseEntity.ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, result));
    }

    @GetMapping("/trade/{tradeId}")
    public ResponseEntity getOneTrade(@PathVariable Long tradeId) {
        ResponseEntity<TradeDetailResult> result = requestTradeApi.RequestGetTrade(tradeId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/trade/sale-list")
    public ResponseEntity getSaleList(HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        Long accountId = jwtUtils.getUserId(token);
        Account id = accountRepository.findById(accountId).get();

//        RequestGetTradeListResult[] history = requestTradeApi.RequestGetSaleHistory(id.getId());
        List<RequestGetTradeListResult> requestGetTradeListResults = requestTradeApi.requestGetSaleHistory(id.getId());

        return ResponseEntity
                .ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, requestGetTradeListResults));
    }


}
