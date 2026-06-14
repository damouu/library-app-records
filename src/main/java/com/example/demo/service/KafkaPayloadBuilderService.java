package com.example.demo.service;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.mapper.BorrowRecordMapper;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.BorrowRecord;
import com.example.demo.model.ChapterProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The type Kafka payload builder service.
 */
@Service
@RequiredArgsConstructor
public class KafkaPayloadBuilderService {

    private final ChapterMapper chapterMapper;

    private final BorrowRecordMapper borrowRecordMapper;

    /**
     * Build borrow entities record.
     *
     * @param payload the payload
     * @return the record
     */
    public BorrowRecord buildBorrowEntities(BorrowCreatedEvent payload) {
        return borrowRecordMapper.toEventData(payload);
    }

    /**
     * Build chapter entities chapter projection.
     *
     * @param payload the payload
     * @return the chapter projection
     */
    public ChapterProjection buildChapterEntities(ChapterCreatedEvent payload) {
        return chapterMapper.toEventData(payload);
    }
}
