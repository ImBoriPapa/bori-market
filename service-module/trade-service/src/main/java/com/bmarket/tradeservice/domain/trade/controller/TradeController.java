package com.bmarket.tradeservice.domain.trade.controller;

import com.bmarket.tradeservice.domain.dto.RequestForm;
import com.bmarket.tradeservice.domain.trade.entity.Category;
import com.bmarket.tradeservice.domain.trade.entity.Trade;
import com.bmarket.tradeservice.domain.trade.entity.TradeStatus;
import com.bmarket.tradeservice.domain.trade.repository.query.AddressSearchCondition;
import com.bmarket.tradeservice.domain.trade.repository.query.SearchCondition;
import com.bmarket.tradeservice.domain.trade.repository.query.TradeListDto;
import com.bmarket.tradeservice.domain.trade.repository.query.TradeQueryRepository;
import com.bmarket.tradeservice.domain.trade.service.TradeCommandService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final TradeQueryRepository tradeQueryRepository;

    @PostMapping(value = "/internal/trade", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResultForm> createTrade(@RequestPart RequestForm form,
                                                  @RequestPart(name = "images") List<MultipartFile> images) {

        Trade trade = tradeCommandService.createTrade(form, images);
        return ResponseEntity.ok(new ResultForm(trade.getId(), trade.getCreatedAt()));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResultForm {
        private Long tradeId;
        private LocalDateTime createdAt;
    }

    @GetMapping("/internal/trade")
    public void getTrade(@RequestParam int page,
                         @RequestParam int size) {


    }

    @GetMapping("/test")
    public ResponseEntity test(){

        PageRequest request = PageRequest.of(0, 100, Sort.Direction.DESC, "id");
        SearchCondition condition = SearchCondition.builder()
                .category(null)
                .isShare(null)
                .isOffer(null)
                .status(null)
                .addressCode(1001)
                .addressSearchCondition(AddressSearchCondition.JUST).build();
        Page<TradeListDto> listDtos = tradeQueryRepository.getTradeWithComplexCondition(request, condition);
        return ResponseEntity.ok().body(listDtos);
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
