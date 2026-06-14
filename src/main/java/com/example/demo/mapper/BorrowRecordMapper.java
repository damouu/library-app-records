package com.example.demo.mapper;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.model.BorrowRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BorrowRecordMapper {

    public BorrowRecord toEventData(BorrowCreatedEvent event) {

        return BorrowRecord.builder()
                .borrowUuid(event.getData().getBorrow_uuid())
                .memberCardUuid(event.getData().getMember_card_uuid())
                .borrowStartDate(LocalDate.parse(event.getData().getBorrow_start_date()))
                .borrowEndDate(LocalDate.parse(event.getData().getBorrow_end_date()))
                .build();
    }
}
