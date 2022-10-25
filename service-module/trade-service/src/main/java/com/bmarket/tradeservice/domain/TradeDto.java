package com.bmarket.tradeservice.domain;

import com.bmarket.tradeservice.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TradeDto {

    private Long accountId;
    private String nickname;
    private String profileImage;
    private String title;
    private String context;
    private Integer price;
    private Integer addressCode;
    private String townName;
    private Category category;
    private Boolean isShare;
    private Boolean isOffer;
    private List<MultipartFile> images;

}
