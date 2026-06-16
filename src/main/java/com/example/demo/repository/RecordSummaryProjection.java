package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RecordSummaryProjection {

    String getBorrowUuid();

    LocalDate getBorrowStartDate();

    LocalDate getBorrowEndDate();

    LocalDate getBorrowReturnDate();

    Integer getDaysLate();

    BigDecimal getLateFee();

    Boolean getReturnLately();

    String getBorrowDetails();
}
