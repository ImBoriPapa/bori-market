package com.bmarket.securityservice.domain.address;


import com.bmarket.securityservice.internal_api.address.RequestAddressApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final RequestAddressApi requestAddressApi;

    /**
     * 주소 검색 요청 컨트롤러
     */
    @GetMapping("/address")
    public ResponseEntity<List<AddressResult>> searchAddress(@RequestParam(defaultValue = "ready") String town) {
        log.info("검색 키워드 ={}", town);

        return ResponseEntity
                .ok()
                .body(requestAddressApi.searchByTown(town));
    }
}
