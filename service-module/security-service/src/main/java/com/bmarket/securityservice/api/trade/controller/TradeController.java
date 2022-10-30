package com.bmarket.securityservice.api.trade.controller;


import com.bmarket.securityservice.api.common.ResponseForm;
import com.bmarket.securityservice.api.trade.controller.RequestForm.RequestCreateTradeForm;
import com.bmarket.securityservice.api.trade.entity.Category;
import com.bmarket.securityservice.api.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.api.trade.service.RequestTradeApi;
import com.bmarket.securityservice.api.trade.controller.resultForm.RequestGetTradeListResult;
import com.bmarket.securityservice.api.trade.controller.resultForm.ResponseCreateTradeResult;
import com.bmarket.securityservice.api.account.entity.Account;
import com.bmarket.securityservice.api.account.repository.AccountRepository;
import com.bmarket.securityservice.api.security.service.JwtService;
import com.bmarket.securityservice.utils.jwt.JwtUtils;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.bmarket.securityservice.api.security.entity.JwtHeader.AUTHORIZATION_HEADER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TradeController {

    private final AccountRepository accountRepository;
    private final RequestTradeApi requestTradeApi;
    private final JwtUtils jwtUtils;

    private final JwtService jwtService;

    @PostMapping("/trade")
    public ResponseEntity<ResponseForm> createTrade(@RequestPart RequestCreateTradeForm form,
                                                    @RequestPart List<MultipartFile> images,
                                                    HttpServletRequest request) {
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        String clientId = jwtUtils.getUserPk(token).getSubject();
        Account id = accountRepository.findByClientId(clientId).get();

        RequestTradeForm requestTradeForm = RequestTradeForm.builder()
                .accountId(id.getId())
                .nickname(id.getProfile().getNickname())
                .title(form.getTitle())
                .context(form.getContext())
                .price(form.getPrice())
                .category(form.getCategory())
                .isShare(form.getIsShare())
                .isOffer(form.getIsOffer()).build();

        ResponseCreateTradeResult responseCreateTradeResult = requestTradeApi.RequestCreateTrade(requestTradeForm, images);

        return ResponseEntity
                .ok()
                .body(new ResponseForm<>(ResponseStatus.SUCCESS,responseCreateTradeResult));
    }


    @GetMapping("/trade")
    public ResponseEntity updateTrade(HttpServletRequest request){
        String token = jwtUtils.resolveToken(request, AUTHORIZATION_HEADER).get();
        String clientId = jwtUtils.getUserPk(token).getSubject();
        Account id = accountRepository.findByClientId(clientId).get();

        RequestGetTradeListResult[] history = requestTradeApi.RequestGetSaleHistory(id.getId());

        return ResponseEntity
                .ok()
                .body(new ResponseForm<>(ResponseStatus.SUCCESS, history));
    }



}
