package com.bmarket.tradeservice.domain.controller;

import com.bmarket.tradeservice.domain.dto.RequestForm;
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

    /**
     * 판매글 생성
     */
    @PostMapping(value = "/internal/trade", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postTrade(@RequestPart RequestForm form,
                                    @RequestPart(name = "images") List<MultipartFile> images) {

        log.info("form accountId = {}", form.getAccountId());
        log.info("form getTitle= {}", form.getTitle());
        log.info("form getContext= {}", form.getContext());
        log.info("form getCategory= {}", form.getCategory());
        log.info("form getIsShare= {}", form.getIsShare());
        log.info("form getIsOffer= {}", form.getIsOffer());
        log.info("form getPrice= {}", form.getPrice());
        log.info("form getAddressCode= {}", form.getAddress().getAddressCode());
        log.info("form getCity= {}", form.getAddress().getCity());
        log.info("form getTown= {}", form.getAddress().getTown());
        log.info("form getDistrict= {}", form.getAddress().getDistrict());

        images.forEach(r -> log.info("image name= {}", r.getOriginalFilename()));

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
