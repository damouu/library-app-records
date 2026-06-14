package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.mapper.BorrowRecordMapper;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.BorrowRecord;
import com.example.demo.model.ChapterProjection;
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

    private final BorrowRecordMapper borrowRecordMapper = new BorrowRecordMapper();

    private final ChapterMapper chapterMapper = new ChapterMapper();

    private final KafkaPayloadBuilderService service = new KafkaPayloadBuilderService(chapterMapper, borrowRecordMapper);


    @Test
    @DisplayName("Should build BorrowRecord from BorrowCreatedEvent")
    void testBuildBorrowEntities() {

        BorrowCreatedEvent borrowCreatedEvent = Instancio.create(BorrowCreatedEvent.class);

        borrowCreatedEvent.getData().setBorrow_start_date(LocalDate.now().toString());

        borrowCreatedEvent.getData().setBorrow_end_date(LocalDate.now().plusDays(5).toString());

        BorrowRecord borrowRecord = service.buildBorrowEntities(borrowCreatedEvent);

        assertEquals(borrowCreatedEvent.getData().getBorrow_uuid(), borrowRecord.getBorrowUuid());

        assertEquals(borrowCreatedEvent.getData().getMember_card_uuid(), borrowRecord.getMemberCardUuid());

        assertNotNull(borrowRecord.getBorrowStartDate());
        assertNotNull(borrowRecord.getBorrowEndDate());

        assertEquals(null, borrowRecord.getReturnLately());
        assertEquals(null, borrowRecord.getDaysLate());
    }


    @Test
    @DisplayName("Should build ChapterProjection from ChapterCreatedEvent")
    void testBuildChapterEntities() {

        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);

        event.getData().setPublication_date(LocalDate.now().toString());

        ChapterProjection result = service.buildChapterEntities(event);

        assertNotNull(result);

        assertEquals(event.getData().getChapter_uuid(), result.getChapterUuid());

        assertEquals(event.getData().getTitle(), result.getTitle());

        assertEquals(event.getData().getChapter_number(), result.getChapterNumber());
    }
}