package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnNotificationDataEvent {

    @JsonProperty("borrow_uuid")
    private UUID borrowUuid;

    @JsonProperty("borrow_start_date")
    private String borrowStartDate;

    @JsonProperty("borrow_end_date")
    private String borrowEndDate;

    @JsonProperty("borrow_return_date")
    private String borrowReturnDate;

    @JsonProperty("return_lately")
    private boolean returnLately;

    @JsonProperty("days_late")
    private int daysLate;

    @JsonProperty("late_fee")
    private BigDecimal lateFee;

    private List<ChapterDetails> chapters;
}