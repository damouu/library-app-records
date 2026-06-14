package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowCreatedEvent {

    private Metadata metadata;

    private BorrowCreatedEventData data;

}