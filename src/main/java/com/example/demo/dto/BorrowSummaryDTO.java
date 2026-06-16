package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowSummaryDTO {

    private UUID borrowUuid;

    private LocalDate borrowStartDate;

    private LocalDate borrowEndDate;

    private LocalDate borrowReturnDate;

    private Integer daysLate;

    private BigDecimal lateFee;

    private Boolean returnLately;

    private List<ChapterSummaryDTO> chapters;
}