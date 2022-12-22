package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.api.requestForm.RequestForm;
import com.bmarket.tradeservice.api.requestForm.RequestUpdateForm;
import com.bmarket.tradeservice.api.responseForm.ResponseForm;
import com.bmarket.tradeservice.api.responseForm.ResponseResult;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeCommandController {
    private final TradeCommandService tradeCommandService;

    /**
     * 판매글 생성
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity postTrade(@RequestPart RequestForm form,
                                    @RequestPart(name = "images") List<MultipartFile> images) {
        log.info("[postTrade]");
        Trade trade = tradeCommandService.createTrade(form, images);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI location = linkTo(TradeCommandController.class).slash(trade.getId()).toUri();

        return ResponseEntity
                .created(location)
                .headers(headers)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, getLinks(trade)));
    }

    private EntityModel<ResponseResult> getLinks(Trade trade) {
        return EntityModel.of(new ResponseResult(trade),
                linkTo(TradeCommandController.class).slash(trade.getId()).withSelfRel(),
                linkTo(TradeCommandController.class).slash(trade.getId()).withRel("PATCH : 판매 글 수정"),
                linkTo(methodOn(TradeCommandController.class).patchStatus(trade.getId(), TradeStatus.SOLD_OUT)).withRel("PATCH : 판매 글 상태 변경"),
                linkTo(TradeCommandController.class).slash(trade.getId()).withRel("DELETE : 판매 글 삭제"),
                linkTo(TradeQueryController.class).slash(trade.getId()).withRel("GET : 판매 글 상세"),
                linkTo(TradeQueryController.class).slash(trade.getId()).withRel("GET : 판매 글 리스트"));
    }


    /**
     * 판매글 삭제
     */
    @DeleteMapping("/{tradeId}")
    public ResponseEntity deleteTrade(@PathVariable Long tradeId) {
        log.info("[deleteTrade]");
        tradeCommandService.deleteTrade(tradeId);

        return ResponseEntity
                .ok()
                .body(null);
    }

    /**
     * 판매글 수정
     */
    @PatchMapping(value = "/{tradeId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity patchTrade(@PathVariable Long tradeId,
                                     @RequestPart RequestUpdateForm form,
                                     @RequestPart(required = false) List<MultipartFile> images) {
        log.info("[patchTrade]");
        Trade trade = tradeCommandService.updateTrade(tradeId, form, images);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, getLinks(trade)));
    }

    @PatchMapping("/{tradeId}/status")
    public ResponseEntity patchStatus(@PathVariable Long tradeId,
                                      @RequestParam(name = "set") TradeStatus set) {
        log.info("[patchStatus tradeId= {}, status= {}]", tradeId, set);

        Trade trade = tradeCommandService.updateStatus(tradeId, set);

        return ResponseEntity.ok()
                .body(new ResponseForm.Of<>(ResponseStatus.SUCCESS, getLinks(trade)));
    }


}
