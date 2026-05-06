package com.example.demo.unit.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import com.example.demo.model.Record;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.service.KafkaPayloadBuilderService;
import com.example.demo.service.LoanService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private KafkaPayloadBuilderService kafkaPayloadBuilderService;


    @InjectMocks
    private LoanService loanService;

    @Test
    @DisplayName("shouldSaveBorrowRecord_whenBorrowEventReceived")
    void testBorrowBooks() {
        BorrowEventPayload borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        borrowEventPayload.getData().getNotificationData().setBorrowStartDate(LocalDate.now().toString());
        borrowEventPayload.getData().getNotificationData().setBorrowEndDate(LocalDate.now().plusDays(5).toString());
        Record record = Instancio.create(Record.class);
        when(kafkaPayloadBuilderService.buildBorrowEntities(borrowEventPayload)).thenReturn(record);
        loanService.borrowBooks(borrowEventPayload);
        verify(borrowRepository, times(1)).save(record);
        verify(kafkaPayloadBuilderService, times(1)).buildBorrowEntities(borrowEventPayload);
    }

    @Test
    @DisplayName("shouldSaveBorrowRecord_whenBorrowEventReceived")
    void testReturnBorrowBooks() {
        ReturnEventPayload returnEventPayload = createValidReturnEventPayload();
        Record record = Instancio.create(Record.class);
        when(borrowRepository.findBorrowByBorrowUuid(returnEventPayload.getMetadata().getEvent_uuid())).thenReturn(Optional.ofNullable(record));
        loanService.returnBorrowBooks(returnEventPayload);
        verify(borrowRepository, times(1)).findBorrowByBorrowUuid(returnEventPayload.getMetadata().getEvent_uuid());
    }

    private ReturnEventPayload createValidReturnEventPayload() {
        ReturnEventPayload payload = Instancio.create(ReturnEventPayload.class);

        var notification = payload.getData().getNotificationData();

        notification.setBorrowStartDate(LocalDate.now().toString());
        notification.setBorrowEndDate(LocalDate.now().plusDays(5).toString());
        notification.setBorrowReturnDate(LocalDate.now().plusDays(3).toString());
        notification.setDaysLate(0);
        notification.setReturnLately(false);
        notification.setLateFee(BigDecimal.ZERO);

        return payload;
    }

}