package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.service.CatalogService;
import com.example.demo.messaging.KafkaListeners;
import com.example.demo.service.LoanService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaListenersTest {

    @Mock
    private LoanService loanService;

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private KafkaListeners kafkaListeners;


    @Test
    @DisplayName("Should listen to a borrowEventPayload")
    void testListenerBorrow() {
        BorrowCreatedEvent borrowCreatedEvent = Instancio.create(BorrowCreatedEvent.class);
        kafkaListeners.listenerBorrow(borrowCreatedEvent);
        verify(loanService).borrowBooks(borrowCreatedEvent);
    }

    @Test
    @DisplayName("Should listen for a return of a listenPayload")
    void testListenerReturn() {
        ReturnCreatedEvent returnCreatedEvent = Instancio.create(ReturnCreatedEvent.class);
        kafkaListeners.listenerReturn(returnCreatedEvent);
        verify(loanService).returnBorrowBooks(returnCreatedEvent);
    }

    @Test
    @DisplayName("Should listen for a new created chapter")
    void testListenerCatalog() {
        ChapterCreatedEvent chapterCreatedEvent = Instancio.create(ChapterCreatedEvent.class);
        kafkaListeners.listenerCatalog(chapterCreatedEvent);
        verify(catalogService).insertNewChapters(chapterCreatedEvent);
    }
}