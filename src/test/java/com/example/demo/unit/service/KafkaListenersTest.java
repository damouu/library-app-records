package com.example.demo.unit.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import com.example.demo.service.KafkaListeners;
import com.example.demo.service.LoanService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaListenersTest {

    @Mock
    private LoanService loanService;

    private KafkaListeners kafkaListeners;

    @BeforeEach
    void setup() {
        kafkaListeners = new KafkaListeners(loanService);
    }

    @Test
    @DisplayName("Should listen to a borrowEventPayload")
    void testListenerBorrow() {
        BorrowEventPayload borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        kafkaListeners.listenerBorrow(borrowEventPayload);
        verify(loanService).borrowBooks(borrowEventPayload);
    }

    @Test
    @DisplayName("Should listen for a return of a listenPayload")
    void testListenerReturn() {
        ReturnEventPayload returnEventPayload = Instancio.create(ReturnEventPayload.class);
        kafkaListeners.listenerReturn(returnEventPayload);
        verify(loanService).returnBorrowBooks(returnEventPayload);
    }
}