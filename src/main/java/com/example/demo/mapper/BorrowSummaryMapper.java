package com.example.demo.mapper;

import com.example.demo.dto.BorrowSummaryDTO;
import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.repository.RecordSummaryProjection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BorrowSummaryMapper {

    private final ObjectMapper objectMapper;

    public BorrowSummaryDTO toDto(RecordSummaryProjection projection) {

        List<ChapterSummaryDTO> chapters = parseChapters(projection.getBorrowDetails());

        return BorrowSummaryDTO.builder().borrowUuid(UUID.fromString(projection.getBorrowUuid())).borrowStartDate(projection.getBorrowStartDate()).borrowEndDate(projection.getBorrowEndDate()).borrowReturnDate(projection.getBorrowReturnDate()).daysLate(projection.getDaysLate()).lateFee(projection.getLateFee()).returnLately(projection.getReturnLately()).chapters(chapters).build();
    }

    private List<ChapterSummaryDTO> parseChapters(String borrowDetails) {

        if (borrowDetails == null || borrowDetails.isBlank()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(borrowDetails, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to parse borrow details", e);
        }
    }
}
