package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.dto.RequestForm;
import com.bmarket.tradeservice.dto.RequestUpdateForm;
import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.Trade;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.exception.ExceptionMessage;
import com.bmarket.tradeservice.domain.repository.query.AddressRange;
import com.bmarket.tradeservice.domain.repository.query.SearchCondition;
import com.bmarket.tradeservice.domain.repository.query.TradeListDto;
import com.bmarket.tradeservice.domain.repository.query.TradeQueryRepositoryImpl;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;
import com.bmarket.tradeservice.domain.service.TradeCommandService;
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
@RequestMapping("/internal")
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
     * 상세 내용 조회
     */
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity getTrade(@PathVariable Long tradeId) {

        TradeDetailDto tradeDetail = tradeQueryRepository.findTradeDetailById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOTFOUND_TRADE.getMessage()));

        return ResponseEntity.ok().body(tradeDetail);
    }

    /**
     * 리스트 조회
     * 필수 파라미터
     * size  : 요청 페이지 크기
     * l-idx : 페이징을 위한 마지막 페이지 인덱스
     * code  : 주소코드 - 계정 정보의 주소코드를 기준으로 데이터 제공하기 위하여 필수로 받아야함
     * range : 주소 검색 범위
     */
    @GetMapping("/trade")
    public ResponseEntity getTradeList(
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "l-idx", defaultValue = "0") Long lastIndex,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "share", required = false) Boolean isShare,
            @RequestParam(name = "offer", required = false) Boolean isOffer,
            @RequestParam(name = "status", required = false) TradeStatus status,
            @RequestParam(name = "code") Integer addressCode,
            @RequestParam(name = "range", defaultValue = "ONLY") AddressRange range) {
        SearchCondition searchCondition = SearchCondition.builder()
                .category(category)
                .isShare(isShare)
                .isOffer(isOffer)
                .status(status)
                .addressCode(addressCode)
                .range(range).build();


        TradeListDto result = tradeQueryRepository.findTradeListWithCondition(size, lastIndex, searchCondition);

        return ResponseEntity.ok().body(result);
    }
}
