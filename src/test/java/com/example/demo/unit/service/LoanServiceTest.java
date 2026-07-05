package com.example.demo.unit.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.BorrowRecordItemMapper;
import com.example.demo.mapper.BorrowRecordMapper;
import com.example.demo.model.BorrowRecord;
import com.example.demo.model.BorrowRecordItem;
import com.example.demo.model.ChapterProjection;
import com.example.demo.repository.BorrowRecordItemRepository;
import com.example.demo.repository.BorrowRecordRepository;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.service.LoanService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private BorrowRecordMapper borrowRecordMapper;

    @Mock
    private BorrowRepository borrowRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void testBorrowBooks() {
        BorrowCreatedEvent event = Instancio.of(BorrowCreatedEvent.class).generate(field(BorrowCreatedEventData::borrowed_items), gen -> gen.collection().size(1)).create();
        UUID chapterUuid = event.data().borrowed_items().getFirst().chapter_uuid();
        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);
        ChapterProjection chapter = Instancio.create(ChapterProjection.class);
        BorrowRecordItem item = Instancio.create(BorrowRecordItem.class);
        when(borrowRecordRepository.existsByBorrowUuid(any())).thenReturn(false);
        when(borrowRecordMapper.toEventData(event)).thenReturn(borrowRecord);
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
        UUID borrowUuid = UUID.randomUUID();
        UUID memberCardUuid = UUID.randomUUID();
        ReturnCreatedEvent returnCreatedEvent = new ReturnCreatedEvent(new Metadata("2026-06-21T10:00:00Z", "library-app-catalogue-v1", "CHAPTER_CREATED", UUID.randomUUID()), new ReturnCreatedEventData(memberCardUuid, borrowUuid, "2026-06-21", "2026-07-21", "2026-06-27", false, 0, BigDecimal.valueOf(0), List.of(new BookToDecrement(UUID.randomUUID(), UUID.randomUUID()))));
        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);
        when(borrowRepository.findBorrowByBorrowUuid(returnCreatedEvent.data().borrow_uuid())).thenReturn(Optional.ofNullable(borrowRecord));
        loanService.returnBorrowBooks(returnCreatedEvent);
        verify(borrowRepository, times(1)).findBorrowByBorrowUuid(returnCreatedEvent.data().borrow_uuid());
    }

    @Test
    @DisplayName("Should skip processing when borrow already exists")
    void testBorrowAlreadyProcessed() {
        UUID borrowUuid = UUID.randomUUID();
        UUID memberCardUuid = UUID.randomUUID();
        BorrowCreatedEvent event = new BorrowCreatedEvent(new Metadata("2026-06-21T10:00:00Z", "library-app-borrow-v1", "BORROW_CREATED", UUID.randomUUID()), new BorrowCreatedEventData(memberCardUuid, borrowUuid, "2026-06-21", "2026-07-21", List.of(new BorrowedItem(UUID.randomUUID(), UUID.randomUUID()))));
        when(borrowRecordRepository.existsByBorrowUuid(borrowUuid)).thenReturn(true);
        loanService.borrowBooks(event);
        verify(borrowRecordRepository).existsByBorrowUuid(borrowUuid);
        verify(borrowRecordRepository, never()).save(any());
        verify(borrowRecordItemRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should throw exception when chapter projections are missing")
    void testMissingChapterProjection() {
        BorrowCreatedEvent event = Instancio.create(BorrowCreatedEvent.class);
        UUID borrowUuid = event.data().borrow_uuid();
        BorrowRecord borrowRecord = Instancio.create(BorrowRecord.class);
        when(borrowRecordRepository.existsByBorrowUuid(borrowUuid)).thenReturn(false);
        when(borrowRecordMapper.toEventData(event)).thenReturn(borrowRecord);
        List<ChapterProjection> projections = List.of(Instancio.create(ChapterProjection.class));
        when(chapterRepository.findByChapterUuidIn(any())).thenReturn(projections);
        assertThrows(IllegalStateException.class, () -> loanService.borrowBooks(event));
        verify(borrowRecordRepository, never()).save(any());
        verify(borrowRecordItemRepository, never()).saveAll(any());
    }

}