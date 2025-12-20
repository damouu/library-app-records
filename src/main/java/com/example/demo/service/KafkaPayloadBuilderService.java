package com.example.demo.service;

import com.example.demo.dto.BorrowEventPayload;
import com.example.demo.model.Record;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class KafkaPayloadBuilderService {

    public Record buildBorrowEntities(BorrowEventPayload payload) {
        var notificationData = payload.getData().getNotificationData();
        var metadata = payload.getMetadata();

        return Record.builder().borrowUuid(notificationData.getBorrowUuid()).memberCardUuid(metadata.getMemberCardUUID()).borrowStartDate(LocalDate.parse(notificationData.getBorrowStartDate())).borrowEndDate(LocalDate.parse(notificationData.getBorrowEndDate())).chapters(notificationData.getChapters()).build();
    }
}