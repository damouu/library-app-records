package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public record BorrowSummaryDTO(
        String borrowUuid,

        LocalDate borrowStartDate,

        LocalDate borrowEndDate,

        LocalDate borrowReturnDate,

        Integer daysLate,

        BigDecimal lateFee,

        Boolean returnLately,

        List<ChapterSummaryDTO> chapters
) {
}