package com.example.demo.controller;

import com.example.demo.service.RecordService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@Data
@Validated
@CrossOrigin
@RestController
@RequestMapping("api/records")
public class RecordController {

    private final RecordService recordService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/membercard/{memberCardUUID}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHistory(@PathVariable UUID memberCardUUID, @RequestParam Map<String, ?> allParams, @AuthenticationPrincipal Jwt jwt) {

        String jwtMemberCard = jwt.getClaimAsString("user_memberCardUUID");

        if (!jwtMemberCard.equals(memberCardUUID.toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "memberCard UUID mismatch");
        }

        return recordService.getHistory(memberCardUUID, allParams);
    }
}
