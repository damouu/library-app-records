package com.example.demo.unit.service;

import com.example.demo.controller.RecordController;
import com.example.demo.dto.BorrowSummaryDTO;
import com.example.demo.service.RecordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowRecordControllerTest {

    @Mock
    private RecordService recordService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private RecordController recordController;

    @Test
    @DisplayName("shouldReturnHistory")
    void shouldReturnHistory() {
        UUID memberCardUUID = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("borrowStartDate"));
        Page<BorrowSummaryDTO> page = new PageImpl<>(List.of());
        when(recordService.getHistory(memberCardUUID, pageable)).thenReturn(page);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("user_memberCardUUID")).thenReturn(memberCardUUID.toString());
        ResponseEntity<Page<BorrowSummaryDTO>> response = (ResponseEntity<Page<BorrowSummaryDTO>>) recordController.getHistory(memberCardUUID, pageable, jwt);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(page, response.getBody());
        verify(recordService).getHistory(memberCardUUID, pageable);
    }

    @Test
    @DisplayName("shouldThrowForbiddenWhenJwtMemberCardDoesNotMatchHeader")
    void shouldThrowForbiddenWhenJwtMemberCardDoesNotMatchHeader() {
        UUID memberCardUUID = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        when(jwt.getClaimAsString("user_memberCardUUID")).thenReturn(UUID.randomUUID().toString());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> recordController.getHistory(memberCardUUID, pageable, jwt));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("memberCard UUID mismatch", exception.getReason());
        verify(recordService, never()).getHistory(any(), any());
    }
}
