package com.bmarket.frmservice.domain.trade.service;

import com.bmarket.frmservice.domain.trade.entity.TradeImage;
import com.bmarket.frmservice.domain.trade.entity.UploadFile;
import com.bmarket.frmservice.domain.trade.repository.TradeImageRepository;
import com.bmarket.frmservice.utils.FileManager;
import com.bmarket.frmservice.utils.Patterns;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TradeImageService {

    @Value("${resource-path.trade-image-path}")
    private String IMAGE_PATH;
    private final TradeImageRepository tradeImageRepository;
    private final FileManager manager;

    public ResultSave saveImage(Long tradeId, List<MultipartFile> files) {

        List<UploadFile> uploadFiles = manager.saveFile(IMAGE_PATH, files);

        TradeImage tradeImage = TradeImage.createTradeImage()
                .tradeId(tradeId)
                .images(uploadFiles).build();

        TradeImage save = tradeImageRepository.save(tradeImage);
        List<String> list = save.getImages().stream().map(m -> m.getStoredName()).collect(Collectors.toList());
        List<String> strings = list.stream().map(m -> Patterns.SEARCH_TRADE_PATTERN + m).collect(Collectors.toList());
        ResultSave resultSave = new ResultSave(strings);
        return resultSave;
    }
    @Getter
    public static class ResultSave{
        private List<String> imagePath;
        public ResultSave(List<String> imagePath) {
            this.imagePath = imagePath;
        }
    }

    public void deleteImages(Long tradeId){
        TradeImage tradeImage = tradeImageRepository.findByTradeId(tradeId).get();
        List<UploadFile> images = tradeImage.getImages();

        for (UploadFile image : images) {
            manager.deleteFile(IMAGE_PATH,image.getStoredName());
        }
        tradeImageRepository.delete(tradeImage);
    }
}
