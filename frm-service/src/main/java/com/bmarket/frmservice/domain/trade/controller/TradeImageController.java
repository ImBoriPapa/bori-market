package com.bmarket.frmservice.domain.trade.controller;

import com.bmarket.frmservice.domain.trade.dto.ResponseTradeImage;
import com.bmarket.frmservice.domain.trade.service.TradeImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeImageController {

    private final TradeImageService tradeImageService;

    /**
     * tradeImage 생성
     * 이미지 파일은 최대 10개 까지 저장
     */
    @PostMapping("/frm/trade/{tradeId}/trade-image")
    public ResponseEntity postTradeImage(@PathVariable(name = "tradeId") Long id,
                                         @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        validateImages(images);

        log.info("tradeId={}", id);
        images.forEach(i -> log.info("file name ={}", i.getOriginalFilename()));




        ResponseTradeImage responseTradeImage = tradeImageService.createTradeImage(id, images);

        return ResponseEntity.ok().body(responseTradeImage);
    }

    /**
     * 판매 이미지 삭제 요청
     */
    @DeleteMapping("/frm/trade/{tradeId}/trade-image")
    public ResponseEntity deleteTradeImage(@PathVariable(name = "tradeId") Long id) {

        ResponseTradeImage responseTradeImage = tradeImageService.deleteImages(id);

        return ResponseEntity
                .ok()
                .body(responseTradeImage);
    }

    /**
     * tradeImage 수정
     */
    @PutMapping("/frm/trade/{tradeId}/trade-image")
    public ResponseEntity putTradeImage(@PathVariable(name = "tradeId") Long id,
                                        @RequestPart List<MultipartFile> images){

        validateImages(images);

        ResponseTradeImage responseTradeImage = tradeImageService.updateTradeImage(id, images);

        return ResponseEntity
                .ok()
                .body(responseTradeImage);
    }

    //이미지가 없거나 10개 이상일 경우 exception
    private void validateImages(List<MultipartFile> images) {
        if (images == null) {
            throw new IllegalArgumentException("저장할 이미지파일이 존재하지 않습니다.");
        } else if (images.size() > 10) {
            throw new IllegalArgumentException("이미지 파일은 최대 10개 까지 저장가능합니다.");
        }
    }


}
