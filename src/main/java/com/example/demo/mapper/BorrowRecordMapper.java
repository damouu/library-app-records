package com.example.demo.mapper;

import com.example.demo.dto.BorrowCreatedEvent;
import com.example.demo.model.BorrowRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BorrowRecordMapper {

    public BorrowRecord toEventData(BorrowCreatedEvent event) {

        return BorrowRecord.builder()
                .borrowUuid(event.data().borrow_uuid())
                .memberCardUuid(event.data().member_card_uuid())
                .borrowStartDate(LocalDate.parse(event.data().borrow_start_date()))
                .borrowEndDate(LocalDate.parse(event.data().borrow_end_date()))
                .build();
    }
}
