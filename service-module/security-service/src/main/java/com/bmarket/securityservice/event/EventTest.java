package com.bmarket.securityservice.event;

import com.bmarket.securityservice.domain.account.service.AccountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventTest {

    private final AccountEvent accountEvent;

    @GetMapping("/event/{address}")
    public void test(@PathVariable Integer address){

        accountEvent.create(address);

        log.info("테스트 완료");
    }

}
