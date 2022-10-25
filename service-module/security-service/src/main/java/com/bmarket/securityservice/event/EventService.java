package com.bmarket.securityservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final ApplicationEventPublisher publisher;

    public void hello(String name){
      log.info("회원 추가 !!");
      publisher.publishEvent(new Event(name));
    }
}
