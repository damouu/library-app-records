package com.example.demo.unit.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.model.Record;
import com.example.demo.service.KafkaPayloadBuilderService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class KafkaPayloadBuilderServiceTest {

    private final KafkaPayloadBuilderService service = new KafkaPayloadBuilderService();


    @Test
    @DisplayName("Should listen to a borrowEventPayload")
    void testBuildBorrowEntities() {
        BorrowEventPayload borrowEventPayload = Instancio.create(BorrowEventPayload.class);
        borrowEventPayload.getData().getNotificationData().setBorrowStartDate(LocalDate.now().toString());
        borrowEventPayload.getData().getNotificationData().setBorrowEndDate(LocalDate.now().plusDays(5).toString());
        Record record = service.buildBorrowEntities(borrowEventPayload);
        assertEquals(borrowEventPayload.getData().getNotificationData().getBorrowUuid(), record.getBorrowUuid());
        assertEquals(borrowEventPayload.getMetadata().getMemberCardUUID(), record.getMemberCardUuid());
        assertNotNull(record.getBooks());
        assertNotNull(record.getChapters());
    }
}