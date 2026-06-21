package com.example.demo.unit.service;

import com.example.demo.dto.*;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaPayloadBuilderServiceTest {

    private final BorrowRecordMapper borrowRecordMapper = new BorrowRecordMapper();

    private final ChapterMapper chapterMapper = new ChapterMapper();

    private final KafkaPayloadBuilderService service = new KafkaPayloadBuilderService(chapterMapper, borrowRecordMapper);


    @Test
    @DisplayName("Should build BorrowRecord from BorrowCreatedEvent")
    void testBuildBorrowEntities() {
        UUID borrowUuid = UUID.randomUUID();
        UUID memberCardUuid = UUID.randomUUID();
        BorrowCreatedEvent borrowCreatedEvent = new BorrowCreatedEvent(new Metadata("2026-06-21T10:00:00Z", "library-app-borrow-v1", "BORROW_CREATED", UUID.randomUUID()), new BorrowCreatedEventData(memberCardUuid, borrowUuid, "2026-06-21", "2026-07-21", List.of(new BorrowedItem(UUID.randomUUID(), UUID.randomUUID()))));
        BorrowRecord borrowRecord = service.buildBorrowEntities(borrowCreatedEvent);
        assertEquals(borrowUuid, borrowRecord.getBorrowUuid());
        assertEquals(memberCardUuid, borrowRecord.getMemberCardUuid());
        assertNotNull(borrowRecord.getBorrowStartDate());
        assertNotNull(borrowRecord.getBorrowEndDate());
        assertNull(borrowRecord.getReturnLately());
        assertNull(borrowRecord.getDaysLate());
    }


    @Test
    @DisplayName("Should build ChapterProjection from ChapterCreatedEvent")
    void testBuildChapterEntities() {
        UUID borrowUuid = UUID.randomUUID();
        UUID memberCardUuid = UUID.randomUUID();
        ChapterCreatedEvent event = new ChapterCreatedEvent(new Metadata("2026-06-21T10:00:00Z", "library-app-catalogue-v1", "CHAPTER_CREATED", UUID.randomUUID()), new ChapterCreatedEventData(memberCardUuid, borrowUuid, "2026-06-21", "2026-07-21", 2, 2, "dde", "dede", "2026-07-21", 1));
        ChapterProjection result = service.buildChapterEntities(event);
        assertNotNull(result);
        assertEquals(event.data().chapter_uuid(), result.getChapterUuid());
        assertEquals(event.data().title(), result.getTitle());
        assertEquals(event.data().chapter_number(), result.getChapterNumber());
    }
}