package com.example.demo.unit.service;

import com.example.demo.dto.BorrowSummaryDTO;
import com.example.demo.mapper.BorrowSummaryMapper;
import com.example.demo.repository.RecordSummaryProjection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowSummaryMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BorrowSummaryMapper borrowSummaryMapper;

    @Test
    void shouldReturnEmptyChapterListWhenBorrowDetailsIsNull() {
        RecordSummaryProjection projection = mock(RecordSummaryProjection.class);
        when(projection.getBorrowUuid()).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(projection.getBorrowDetails()).thenReturn(null);
        BorrowSummaryDTO dto = borrowSummaryMapper.toDto(projection);
        assertEquals(0, dto.chapters().size());
    }

    @Test
    void shouldReturnEmptyChapterListWhenBorrowDetailsIsBlank() {
        RecordSummaryProjection projection = mock(RecordSummaryProjection.class);
        when(projection.getBorrowUuid()).thenReturn("123e4567-e89b-12d3-a456-426614174000");
        when(projection.getBorrowDetails()).thenReturn("");
        BorrowSummaryDTO dto = borrowSummaryMapper.toDto(projection);
        assertTrue(dto.chapters().isEmpty());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenJsonCannotBeParsed() throws Exception {
        RecordSummaryProjection projection = mock(RecordSummaryProjection.class);
        when(projection.getBorrowDetails()).thenReturn("invalid-json");
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new JsonProcessingException("boom") {
        });
        assertThrows(IllegalStateException.class, () -> borrowSummaryMapper.toDto(projection));
    }
}