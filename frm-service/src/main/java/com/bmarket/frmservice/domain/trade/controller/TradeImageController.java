package com.bmarket.frmservice.domain.trade.controller;

import com.bmarket.frmservice.domain.trade.service.TradeImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeImageController {

    private final TradeImageService tradeImageService;

    @PostMapping("/frm/trade")
    public ResponseEntity createTradeImage(@RequestParam(name ="tradeId") Long id,
                                           @RequestPart(name="images",required = false) List<MultipartFile> images){
        TradeImageService.ResultSave resultSave = tradeImageService.saveImage(id, images);

        return ResponseEntity.ok().body(resultSave);
    }


}
