package com.example.demo.service;

import com.example.demo.dto.BorrowSummaryDTO;
import com.example.demo.mapper.BorrowSummaryMapper;
import com.example.demo.repository.RecordSummaryProjection;
import com.example.demo.repository.RecordSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * The type Record service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordSummaryRepository recordSummaryRepository;

    private final BorrowSummaryMapper borrowSummaryMapper;

    public Page<BorrowSummaryDTO> getHistory(UUID memberCardUUID, Pageable pageable) {
        Page<RecordSummaryProjection> result = recordSummaryRepository.getRecordSummaries(memberCardUUID, pageable);
        return result.map(borrowSummaryMapper::toDto);
    }

}
