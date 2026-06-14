package com.example.demo.unit.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.BorrowCreatedEventData;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.mapper.BorrowRecordItemMapper;
import com.example.demo.model.BorrowRecord;
import com.example.demo.model.BorrowRecordItem;
import com.example.demo.model.ChapterProjection;
import com.example.demo.repository.BorrowRecordItemRepository;
import com.example.demo.repository.BorrowRecordRepository;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.repository.ChapterRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BorrowRecordItemRepository borrowRecordItemRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private BorrowRecordItemMapper borrowRecordItemMapper;

    @Mock
    private KafkaPayloadBuilderService kafkaPayloadBuilderService;

    @Mock
    private BorrowRepository borrowRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void testBorrowBooks() {
        BorrowCreatedEvent event = Instancio.of(BorrowCreatedEvent.class).generate(field(BorrowCreatedEventData::getBorrowed_items), gen -> gen.collection().size(1)).create();
        UUID chapterUuid = event.getData().getBorrowed_items().getFirst().getChapter_uuid();
        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);
        ChapterProjection chapter = Instancio.create(ChapterProjection.class);
        BorrowRecordItem item = Instancio.create(BorrowRecordItem.class);
        when(borrowRecordRepository.existsByBorrowUuid(any())).thenReturn(false);
        when(kafkaPayloadBuilderService.buildBorrowEntities(event)).thenReturn(borrowRecord);
        when(chapterRepository.findByChapterUuidIn(any())).thenReturn(List.of(chapter));
        when(borrowRecordItemMapper.toBorrowRecordItems(any(), any())).thenReturn(List.of(item));
        loanService.borrowBooks(event);
        verify(borrowRecordItemMapper, times(1)).toBorrowRecordItems(any(), any());
        verify(borrowRecordRepository).save(borrowRecord);
        verify(borrowRecordItemRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("shouldSaveBorrowRecord_whenBorrowEventReceived")
    void testReturnBorrowBooks() {
        ReturnCreatedEvent returnCreatedEvent = createValidReturnEventPayload();
        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);
        when(borrowRepository.findBorrowByBorrowUuid(returnCreatedEvent.getMetadata().getEvent_uuid())).thenReturn(Optional.ofNullable(borrowRecord));
        loanService.returnBorrowBooks(returnCreatedEvent);
        verify(borrowRepository, times(1)).findBorrowByBorrowUuid(returnCreatedEvent.getMetadata().getEvent_uuid());
    }

    private ReturnCreatedEvent createValidReturnEventPayload() {
        ReturnCreatedEvent payload = Instancio.create(ReturnCreatedEvent.class);

        var notification = payload.getData();

        notification.setBorrow_start_date(LocalDate.now().toString());
        notification.setBorrow_end_date(LocalDate.now().plusDays(5).toString());
        notification.setBorrow_return_date(LocalDate.now().plusDays(3).toString());
        notification.setDays_late(0);
        notification.setReturn_lately(false);
        notification.setLate_fee(BigDecimal.ZERO);

        return payload;
    }

    @Test
    @DisplayName("Should skip processing when borrow already exists")
    void testBorrowAlreadyProcessed() {

        BorrowCreatedEvent event = Instancio.create(BorrowCreatedEvent.class);

        UUID borrowUuid = event.getData().getBorrow_uuid();

        when(borrowRecordRepository.existsByBorrowUuid(borrowUuid)).thenReturn(true);

        loanService.borrowBooks(event);

        verify(borrowRecordRepository).existsByBorrowUuid(borrowUuid);

        verifyNoInteractions(kafkaPayloadBuilderService, chapterRepository, borrowRecordItemRepository);
    }

    @Test
    @DisplayName("Should throw exception when chapter projections are missing")
    void testMissingChapterProjection() {

        BorrowCreatedEvent event = Instancio.create(BorrowCreatedEvent.class);

        UUID borrowUuid = event.getData().getBorrow_uuid();

        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);

        when(borrowRecordRepository.existsByBorrowUuid(borrowUuid)).thenReturn(false);

        when(kafkaPayloadBuilderService.buildBorrowEntities(event)).thenReturn(borrowRecord);

        List<ChapterProjection> projections = List.of(Instancio.create(ChapterProjection.class));

        when(chapterRepository.findByChapterUuidIn(any())).thenReturn(projections);

        assertThrows(IllegalStateException.class, () -> loanService.borrowBooks(event));

        verify(borrowRecordRepository, never()).save(any());

        verify(borrowRecordItemRepository, never()).saveAll(any());
    }

}