package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import com.bmarket.tradeservice.domain.repository.query.AddressRange;
import com.bmarket.tradeservice.domain.repository.query.SearchCondition;
import com.bmarket.tradeservice.domain.repository.query.TradeListDto;
import com.bmarket.tradeservice.domain.repository.query.TradeQueryRepositoryImpl;
import com.bmarket.tradeservice.domain.repository.query.dto.TradeDetailDto;
import com.bmarket.tradeservice.status.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeQueryController {

    private final TradeQueryRepositoryImpl tradeQueryRepository;

    /**
     * 상세 내용 조회
     */
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity getTrade(@PathVariable Long tradeId) {
        log.info("[getTrade]");
        TradeDetailDto tradeDetail = tradeQueryRepository.findTradeDetailById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException(ResponseStatus.NOTFOUND_TRADE.getMessage()));

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
            @RequestParam(name = "lastIdx", defaultValue = "0") Long lastIndex,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "offer", required = false) Boolean isOffer,
            @RequestParam(name = "status", required = false) TradeStatus status,
            @RequestParam(name = "code") Integer addressCode,
            @RequestParam(name = "range", defaultValue = "ONLY") AddressRange range) {
        log.info("[getTrade]");

        SearchCondition searchCondition = SearchCondition.builder()
                .category(category)
                .isOffer(isOffer)
                .status(status)
                .addressCode(addressCode)
                .range(range).build();

        TradeListDto result = tradeQueryRepository.findTradeListWithCondition(size, lastIndex, searchCondition);

        return ResponseEntity.ok().body(result);
    }
}
