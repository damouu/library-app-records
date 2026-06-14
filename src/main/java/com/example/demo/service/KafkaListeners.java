package com.example.demo.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final LoanService loanService;

    private final CatalogService catalogService;

    @KafkaListener(topics = "library.borrow.v1", groupId = "records-group", containerFactory = "factory")
    public void listenerBorrow(@Payload BorrowCreatedEvent BorrowCreatedEvent) {
        loanService.borrowBooks(BorrowCreatedEvent);
    }

    @KafkaListener(topics = "library.return.v1", groupId = "records-group", containerFactory = "factory")
    public void listenerReturn(@Payload ReturnCreatedEvent returnCreatedEvent) {
        loanService.returnBorrowBooks(returnCreatedEvent);
    }

    @KafkaListener(topics = "library.catalog.v1", groupId = "records-group", containerFactory = "factory")
    public void listenerCatalog(@Payload ChapterCreatedEvent chapterCreatedEvent) {
        catalogService.insertNewChapters(chapterCreatedEvent);
    }
}