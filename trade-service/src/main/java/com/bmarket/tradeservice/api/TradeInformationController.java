package com.bmarket.tradeservice.api;

import com.bmarket.tradeservice.domain.entity.Category;
import com.bmarket.tradeservice.domain.entity.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeInformationController {

    @GetMapping("/trade/category")
    public ResponseEntity getCategory() {

        List<String> list = Arrays.stream(Category.values())
                .map(Enum::name).collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new CategoryList(list));
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryList {

        private List<String> names;
        public CategoryList(List<String> names) {
            this.names = names;
        }
    }

    @GetMapping("/trade/status")
    public ResponseEntity getStatus() {

        List<String> list = Arrays.stream(TradeStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());



        return ResponseEntity.ok().body(new TradeStatusList(list));
    }

    @Getter
    @NoArgsConstructor
    public static class TradeStatusList {
        private List<String> names;

        public TradeStatusList(List<String> names) {
            this.names = names;
        }
    }

    @GetMapping("/ex")
    public void ex() throws Exception {
        log.info("Exception");
        throw new Exception();
    }


}
