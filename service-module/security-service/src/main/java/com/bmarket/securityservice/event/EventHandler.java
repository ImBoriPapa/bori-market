package com.bmarket.securityservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventHandler {

    @EventListener
    public String send(Event event){
        log.info("이름은 ={}",event.getName());
        return "send" + event.getName();
    }

    @EventListener
    public void coupon(Event event){

        log.info("당첨자는 ={} 입니다.",event.getName());
    }
}
