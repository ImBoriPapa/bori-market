package com.bmarket.tradeservice.domain.controller;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.dto.RequestUpdateForm;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.repository.query.AddressRange;
import com.bmarket.tradeservice.domain.repository.query.ResponseResult;
import com.bmarket.tradeservice.domain.repository.query.SearchCondition;
import com.bmarket.tradeservice.domain.repository.query.TradeQueryRepositoryImpl;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
import com.bmarket.tradeservice.domain.entity.Category;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TradeController {

    private final TradeCommandService tradeCommandService;
    private final TradeQueryRepositoryImpl tradeQueryRepository;

    /**
     * 판매글 생성
     */
    @PostMapping(value = "/trade", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity postTrade(@RequestPart RequestForm form,
                                    @RequestPart(name = "images") List<MultipartFile> images) {

        images.forEach(r -> log.info("image name= {}", r.getOriginalFilename()));

        Trade trade = tradeCommandService.createTrade(form, images);

        ResultForm resultForm = ResultForm.builder()
                .success(true)
                .tradeId(trade.getId())
                .createdAt(trade.getCreatedAt())
                .build();

        return ResponseEntity
                .ok()
                .body(resultForm);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ResultForm {
        private Boolean success;
        private Long tradeId;
        private LocalDateTime createdAt;
    }

    /**
     * 판매글 삭제
     */
    @DeleteMapping("/trade/{tradeId}")
    public ResponseEntity deleteTrade(@PathVariable Long tradeId) {
        tradeCommandService.deleteTrade(tradeId);

        ResultForm resultForm = ResultForm.builder()
                .success(true)
                .tradeId(tradeId)
                .createdAt(null)
                .build();
        return ResponseEntity
                .ok()
                .body(resultForm);
    }

    /**
     * 판매글 수정
     */
    @PutMapping("/trade/{tradeId}")
    public ResponseEntity putTrade(@PathVariable Long tradeId,
                                   @RequestPart RequestUpdateForm form,
                                   @RequestPart List<MultipartFile> images) {
        Trade trade = tradeCommandService.updateTrade(tradeId, form, images);

        ResultForm resultForm = ResultForm.builder()
                .success(true)
                .tradeId(trade.getId())
                .createdAt(trade.getCreatedAt())
                .build();

        return ResponseEntity.ok().body(resultForm);
    }


    /**
     * 내용 조회
     */
    @GetMapping("/internal/trade/{tradeId}")
    public ResponseEntity getTrade(@PathVariable Long tradeId) {
        TradeDetailDto tradeDetail = tradeQueryRepository.getTradeDetail(tradeId);
        return ResponseEntity.ok().body(tradeDetail);
    }

    /**
     * 리스트 조회
     */
    @GetMapping("/internal/trade")
    public ResponseEntity getTradeList(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") Long lastIndex,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Boolean isShare,
            @RequestParam(required = false) Boolean isOffer,
            @RequestParam(required = false) TradeStatus status,
            @RequestParam Integer addressCode,
            @RequestParam AddressRange range
    ) {
        SearchCondition searchCondition = SearchCondition.builder()
                .category(category)
                .isShare(isShare)
                .isOffer(isOffer)
                .status(status)
                .addressCode(addressCode)
                .range(range).build();

        log.info("category={}", searchCondition.getCategory());
        log.info("share={}", searchCondition.getIsShare());
        log.info("offer={}", searchCondition.getIsOffer());
        log.info("status={}", searchCondition.getStatus());
        log.info("address code={}", searchCondition.getAddressCode());
        log.info("address code={}", searchCondition.getRange());

        ResponseResult result = tradeQueryRepository.getTradeWithComplexCondition(size, lastIndex, searchCondition);
        return ResponseEntity.ok().body(result);
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
