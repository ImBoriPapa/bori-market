package com.bmarket.securityservice.domain.trade.controller;


import com.bmarket.securityservice.domain.common.ResponseForm;
import com.bmarket.securityservice.domain.trade.controller.RequestForm.RequestTradeForm;
import com.bmarket.securityservice.domain.trade.controller.resultForm.TradeModifyResult;
import com.bmarket.securityservice.domain.trade.service.TradeService;
import com.bmarket.securityservice.exception.custom_exception.security_ex.FormValidationException;

import com.bmarket.securityservice.domain.trade.controller.resultForm.CreateTradeResult;
import com.bmarket.securityservice.internal_api.trade.form.SearchCondition;

import com.bmarket.securityservice.internal_api.trade.form.TradeContentsResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeDeleteResult;
import com.bmarket.securityservice.internal_api.trade.form.TradeListResult;
import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

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
    private final TradeService tradeService;

    // TODO: 2022/11/24 validation
    @PostMapping("/account/{accountId}/trade")
    public ResponseEntity createTrade(
            @Validated
            @PathVariable Long accountId,
            @RequestPart RequestTradeForm.CreateTradeForm form,
            @RequestPart List<MultipartFile> images,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(ResponseStatus.FAIL_VALIDATION);
        }

        CreateTradeResult createTradeResult = tradeService.create(accountId, form, images);

        EntityModel<CreateTradeResult> entityModel = EntityModel.of(createTradeResult);
        WebMvcLinkBuilder builder = WebMvcLinkBuilder.linkTo(TradeController.class);
        entityModel.add(getLinkList(createTradeResult.getTradeId()));

        WebMvcLinkBuilder.linkTo(TradeController.class);
        URI Location = builder.slash("/trade").slash(createTradeResult.getTradeId()).toUri();

        return ResponseEntity
                .created(Location)
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, entityModel));
    }

    private List<Link> getLinkList(Long tradeId) {
        WebMvcLinkBuilder builder = WebMvcLinkBuilder.linkTo(TradeController.class);
        Link link1 = builder.slash("/trade").slash(tradeId).withRel("GET: 판매글 단건 조회");
        Link link2 = builder.slash("/trade").withRel("GET: 판매글 리스트 조회");
        Link link3 = builder.slash("/trade").slash(tradeId).withRel("PATCH: 판매글 수정");
        Link link4 = builder.slash("/trade").slash(tradeId).withRel("DELETE: 판매글 삭제");
        return List.of(link1, link2, link3, link4);
    }

    /**
     * 사용자 계정에 저장된 주소 및 주소 검색 범위 우선으로 판매글 조회
     */
    // TODO: 2022/11/24 다음 판매글의 유무에 따라 링크 변경 구현
    @GetMapping("/account/{accountId}/trade")
    public ResponseEntity getTradeList(
            @PathVariable Long accountId,
            SearchCondition condition) {

        EntityModel<TradeListResult> entityModel = EntityModel
                .of(tradeService.tradeList(accountId, condition));

        return ResponseEntity.ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 판매글 단건 조회
     */
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity getTrade(@PathVariable Long tradeId) {

        EntityModel<TradeContentsResult> entityModel = EntityModel
                .of(tradeService.trade(tradeId));

        return ResponseEntity.ok().body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 판매 글 수정
     */
    @PatchMapping("/trade/{tradeId}")
    public ResponseEntity patchTrade(@Validated
                                     @PathVariable Long tradeId,
                                     @RequestPart RequestTradeForm.ModifyTradeForm form,
                                     @RequestPart List<MultipartFile> images,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Trade 수정에 문제가 발생");
        }

        EntityModel<TradeModifyResult> entityModel = EntityModel.of(tradeService.tradeModify(tradeId, form, images));

        return ResponseEntity.ok().body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, entityModel));
    }

    /**
     * 판매 글 삭제
     */
    @DeleteMapping("/trade/{tradeId}")
    public ResponseEntity deleteTrade(@PathVariable Long tradeId){

        EntityModel<TradeDeleteResult> entityModel = EntityModel.of(tradeService.tradeDelete(tradeId));

        return ResponseEntity.ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, entityModel));
    }
}
