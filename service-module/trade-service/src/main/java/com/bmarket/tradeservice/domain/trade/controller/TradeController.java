package com.bmarket.tradeservice.domain.trade.controller;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.entity.TradeStatus;
import com.bmarket.tradeservice.domain.trade.repository.query.*;
import com.bmarket.tradeservice.domain.trade.repository.query.dto.TradeListDto;
import com.bmarket.tradeservice.domain.trade.service.TradeCommandService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TradeController {

    private final TradeCommandService tradeCommandService;
    private final TradeQueryRepositoryImpl tradeQueryRepository;

    @PostMapping(value = "/internal/trade", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResultForm> createTrade(@RequestPart RequestForm form,
                                                  @RequestPart(name = "images") List<MultipartFile> images) {

        Trade trade = tradeCommandService.createTrade(form, images);
        return ResponseEntity.ok().body(new ResultForm(trade.getId(), trade.getCreatedAt()));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResultForm {
        private Long tradeId;
        private LocalDateTime createdAt;
    }

    @GetMapping("/internal/trade/{accountId}/sale-history")
    public ResponseEntity getMyTradeList(@PathVariable Long accountId){
        List<TradeListDto> list = tradeQueryRepository.getTradeListByAccountId(accountId);
        for (TradeListDto tradeListDto : list) {

            log.info("tradeId={}",tradeListDto.getTradeId());
        }
        return ResponseEntity.ok().body(list);
    }


    @GetMapping("/test2")
    public void getTrade(SearchCondition condition) {

        log.info("range={}",condition.getRange());
        log.info("range={}",condition.getRange());
        log.info("range={}",condition.getRange());
        log.info("range={}",condition.getRange());
        log.info("range={}",condition.getRange());
        log.info("range={}",condition.getRange());
    }

    @GetMapping("/test")
    public ResponseEntity test(@RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "0") Long tradeId,
                               @RequestParam(required = false) Category category,
                               @RequestParam(required = false) Boolean isShare,
                               @RequestParam(required = false) Boolean isOffer,
                               @RequestParam(required = false) TradeStatus status,
                               @RequestParam(required = false) Integer addressCode,
                               @RequestParam(required = false) AddressRange range
    ) {
        SearchCondition searchCondition = SearchCondition.builder()
                .category(category)
                .isShare(isShare)
                .isOffer(isOffer)
                .status(status)
                .addressCode(addressCode)
                .range(range).build();

        log.info("category={}",searchCondition.getCategory());
        log.info("share={}",searchCondition.getIsShare());
        log.info("offer={}",searchCondition.getIsOffer());
        log.info("status={}",searchCondition.getStatus());
        log.info("address code={}",searchCondition.getAddressCode());

//        ResponseResult result = tradeQueryRepository.getTradeWithComplexCondition(size, tradeId, searchCondition);
        return ResponseEntity.ok().body("ok");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class requestCondition {
        private int page;
        private int size;
        private int addressCode;
        private Category category;
        private Boolean isShare;
        private Boolean isOffer;
        private TradeStatus status;
    }
}
