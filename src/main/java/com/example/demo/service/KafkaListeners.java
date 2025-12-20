package com.example.demo.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private final LoanService loanService;

    @Autowired
    public KafkaListeners(LoanService loanService) {
        this.loanService = loanService;
    }

    @KafkaListener(topics = "library.borrow.v1", groupId = "records-group", containerFactory = "factory")
    void listenerBorrow(@Payload BorrowEventPayload BorrowEventPayload) {
        loanService.borrowBooks(BorrowEventPayload);
    }

    @KafkaListener(topics = "library.return.v1", groupId = "records-group", containerFactory = "factory")
    void listenerReturn(@Payload ReturnEventPayload returnEventPayload) {
        loanService.returnBorrowBooks(returnEventPayload);
    }
}