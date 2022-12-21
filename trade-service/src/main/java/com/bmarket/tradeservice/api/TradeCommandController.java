package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.api.requestForm.RequestForm;
import com.bmarket.tradeservice.api.requestForm.RequestUpdateForm;
import com.bmarket.tradeservice.api.responseForm.ResponseForm;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

        EntityModel<ResponseCreateTrade> entityModel = EntityModel.of(new ResponseCreateTrade(trade),
                linkTo(methodOn(TradeCommandController.class).postTrade(form, images)).withSelfRel(),
                linkTo(methodOn(TradeCommandController.class).patchTrade(trade.getId(), new RequestUpdateForm(), images)).withRel("PATCH : 판매 글 수정"),
                linkTo(methodOn(TradeCommandController.class).patchStatus(trade.getId())).withRel("PATCH : 판매 글 상태 변경"),
                linkTo(methodOn(TradeCommandController.class).deleteTrade(trade.getId())).withRel("DELETE : 판매 글 삭제")
        );


        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseForm.Of(ResponseStatus.SUCCESS, entityModel));
    }

    @Getter
    @NoArgsConstructor
    public static class ResponseCreateTrade {
        private Long tradeId;
        private String memberId;
        private LocalDateTime createdAt;

        public ResponseCreateTrade(Trade trade) {
            this.tradeId = trade.getId();
            this.memberId = trade.getMemberId();
            this.createdAt = trade.getCreatedAt();
        }
    }


    /**
     * 판매글 삭제
     */
    @DeleteMapping("/trade/{tradeId}")
    public ResponseEntity deleteTrade(@PathVariable Long tradeId) {
        tradeCommandService.deleteTrade(tradeId);


        return ResponseEntity
                .ok()
                .body(null);
    }

    /**
     * 판매글 수정
     */
    @PatchMapping("/trade/{tradeId}")
    public ResponseEntity patchTrade(@PathVariable Long tradeId,
                                     @RequestPart RequestUpdateForm form,
                                     @RequestPart List<MultipartFile> images) {
        Trade trade = tradeCommandService.updateTrade(tradeId, form, images);


        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/{tradeId}/status")
    public ResponseEntity patchStatus(@PathVariable Long tradeId) {

        return null;
    }


}
