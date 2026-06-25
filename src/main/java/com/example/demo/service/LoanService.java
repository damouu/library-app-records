package com.example.demo.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.BorrowedItem;
import com.example.demo.dto.ReturnCreatedEvent;
import com.example.demo.mapper.BorrowRecordItemMapper;
import com.example.demo.mapper.BorrowRecordMapper;
import com.example.demo.model.BorrowRecord;
import com.example.demo.model.BorrowRecordItem;
import com.example.demo.model.ChapterProjection;
import com.example.demo.repository.BorrowRecordItemRepository;
import com.example.demo.repository.BorrowRecordRepository;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * The type Loan service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final BorrowRepository borrowRepository;

    private final BorrowRecordRepository borrowRecordRepository;

    private final BorrowRecordItemMapper borrowRecordItemMapper;

    private final BorrowRecordItemRepository borrowRecordItemRepository;

    private final ChapterRepository chapterRepository;

    private final BorrowRecordMapper borrowRecordMapper;


    /**
     * Processes a BORROW_CREATED event.
     * The operation is idempotent:
     * if the borrow record already exists, the event is ignored.
     * Creates the borrow aggregate and its associated items
     * using locally projected chapter data.
     *
     * @param payload BORROW_CREATED event payload
     */
    @Transactional
    public void borrowBooks(BorrowCreatedEvent payload) {
        UUID borrowUuid = payload.data().borrow_uuid();
        if (borrowRecordRepository.existsByBorrowUuid(borrowUuid)) {
            log.info("Borrow {} already processed", borrowUuid);
            return;
        }
        BorrowRecord borrow = borrowRecordMapper.toEventData(payload);
        List<UUID> chapterUuids = payload.data().borrowed_items().stream().map(BorrowedItem::chapter_uuid).toList();
        List<ChapterProjection> chapters = chapterRepository.findByChapterUuidIn(chapterUuids);
        if (chapters.size() != chapterUuids.size()) {
            throw new IllegalStateException("Missing chapter projections");
        }
        List<BorrowRecordItem> borrowRecordItems = borrowRecordItemMapper.toBorrowRecordItems(borrowUuid, chapters);
        borrowRecordRepository.save(borrow);
        borrowRecordItemRepository.saveAll(borrowRecordItems);
        log.info("Borrow {} persisted with {} items", borrowUuid, borrowRecordItems.size());
    }



    @Transactional
    public void returnBorrowBooks(ReturnCreatedEvent returnCreatedEvent) {
        UUID borrowUuid = returnCreatedEvent.metadata().event_uuid();
        borrowRepository.findBorrowByBorrowUuid(borrowUuid).ifPresentOrElse(record -> {
            record.updateReturnInfo(returnCreatedEvent);
            log.info("Updated borrow record for UUID: {}", borrowUuid);
        }, () -> log.error("Borrow record not found for UUID: {}. Cannot process return.", borrowUuid));
    }

}