package com.example.demo.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.dto.ReturnEventPayload;
import com.example.demo.model.Record;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.repository.RecordSummaryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private final BorrowRepository borrowRepository;

    private final RecordSummaryRepository recordSummaryRepository;

    private final KafkaPayloadBuilderService payloadBuilderService;


    @Transactional
    public void borrowBooks(BorrowEventPayload payload) {
        Record borrows = payloadBuilderService.buildBorrowEntities(payload);
        borrowRepository.save(borrows);
        log.info("saved new borrow");
    }


    @Transactional
    public void returnBorrowBooks(ReturnEventPayload returnEventPayload) {
        UUID borrowUuid = returnEventPayload.getMetadata().getEvent_uuid();
        borrowRepository.findBorrowByBorrowUuid(borrowUuid).ifPresentOrElse(record -> {
            record.updateReturnInfo(returnEventPayload);
            log.info("Updated borrow record for UUID: {}", borrowUuid);
        }, () -> log.error("Borrow record not found for UUID: {}. Cannot process return.", borrowUuid));
    }

}