package com.example.demo.unit.service;

import com.example.demo.controller.RecordController;
import com.example.demo.service.RecordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
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
    @DisplayName("shouldReturnHistory_whenMemberCardMatchesJwt")
    void shouldReturnHistory_whenMemberCardMatchesJwt() {
        UUID memberCardUUID = UUID.randomUUID();
        Map<String, Object> params = Map.of("page", 0);

        ResponseEntity<?> expectedResponse = ResponseEntity.ok("history");

        when(jwt.getClaimAsString("user_memberCardUUID"))
                .thenReturn(memberCardUUID.toString());

        when(recordService.getHistory(memberCardUUID, params))
                .thenReturn((ResponseEntity<HashMap<String, Object>>) expectedResponse);

        ResponseEntity<?> response =
                recordController.getHistory(memberCardUUID, params, jwt);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());

        verify(recordService).getHistory(memberCardUUID, params);
    }

    @Test
    @DisplayName("shouldThrowForbidden_whenMemberCardDoesNotMatchJwt")
    void shouldThrowForbidden_whenMemberCardDoesNotMatchJwt() {
        UUID memberCardUUID = UUID.randomUUID();

        Map<String, Object> params = Map.of("page", 0);

        when(jwt.getClaimAsString("user_memberCardUUID"))
                .thenReturn(UUID.randomUUID().toString());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> recordController.getHistory(memberCardUUID, params, jwt)
        );

        assertEquals(403, exception.getStatus().value());

        verify(recordService, never()).getHistory(any(), any());
    }
}