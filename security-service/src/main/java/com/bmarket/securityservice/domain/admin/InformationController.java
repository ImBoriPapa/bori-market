package com.bmarket.securityservice.domain.admin;


import com.bmarket.securityservice.utils.status.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InformationController {
    /**
     * ResponseStatus 정보
     */
    @GetMapping("/info/response-status")
    public List<ResponseStatusValue> responseStatus() {

        return Arrays.stream(ResponseStatus.values())
                .map(ResponseStatusValue::new)
                .collect(Collectors.toList());

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseStatusValue {
        private String status;
        private Integer code;
        private String message;

        public ResponseStatusValue(ResponseStatus status) {
            this.status = status.name();
            this.code = status.getCode();
            this.message = status.getMessage();
        }
    }

}
