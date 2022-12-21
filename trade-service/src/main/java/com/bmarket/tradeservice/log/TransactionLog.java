package com.bmarket.tradeservice.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Component
@Slf4j
public class TransactionLog {

    public void getInfo() {
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
        log.info("[currentTransactionName= {}]", currentTransactionName);
        log.info("[isActualTransactionActive= {}]", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("[getCurrentTransactionIsolationLevel= {}]", TransactionSynchronizationManager.getCurrentTransactionIsolationLevel());
        TransactionSynchronizationManager.getResourceMap().forEach((k,v)->log.info("[getResourceMap key ={},value ={}]",k,v));
        synchronizations.forEach(data -> log.info("[synchronizations= {}]", data));


    }
}
