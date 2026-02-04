package com.example.demo.controller;

import com.example.demo.service.RecordService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/api/records", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHistory(@RequestHeader("X-User-UUID") UUID memberCardUUID, @RequestParam Map<String, ?> allParams, @AuthenticationPrincipal Jwt jwt) {

        String jwtMemberCard = jwt.getClaimAsString("user_memberCardUUID");

        if (!jwtMemberCard.equals(memberCardUUID.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "memberCard UUID mismatch");
        }

        return recordService.getHistory(memberCardUUID, allParams);
    }
}
