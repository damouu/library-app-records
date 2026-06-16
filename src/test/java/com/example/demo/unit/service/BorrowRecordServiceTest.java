package com.example.demo.unit.service;

import com.example.demo.dto.BorrowSummaryDTO;
import com.example.demo.mapper.BorrowSummaryMapper;
import com.example.demo.repository.RecordSummaryProjection;
import com.example.demo.repository.RecordSummaryRepository;
import com.example.demo.service.RecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceTest {

    @Mock
    private RecordSummaryRepository recordSummaryRepository;

    @Mock
    private BorrowSummaryMapper borrowSummaryMapper;

    @InjectMocks
    private RecordService recordService;

    @Test
    void shouldReturnMappedBorrowSummaries() {
        UUID memberCardUUID = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "borrowStartDate"));
        RecordSummaryProjection projection = mock(RecordSummaryProjection.class);
        BorrowSummaryDTO dto = BorrowSummaryDTO.builder().borrowUuid(UUID.randomUUID()).build();
        Page<RecordSummaryProjection> repositoryPage = new PageImpl<>(List.of(projection));
        when(recordSummaryRepository.getRecordSummaries(memberCardUUID, pageable)).thenReturn(repositoryPage);
        when(borrowSummaryMapper.toDto(projection)).thenReturn(dto);
        Page<BorrowSummaryDTO> result = recordService.getHistory(memberCardUUID, pageable);
        verify(recordSummaryRepository).getRecordSummaries(memberCardUUID, pageable);
        verify(borrowSummaryMapper).toDto(projection);
        assertEquals(1, result.getContent().size());
        assertEquals(dto.getBorrowUuid(), result.getContent().getFirst().getBorrowUuid());
    }
}