package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterCreatedEvent {

    private Metadata metadata;

    private ChapterCreatedEventData data;
    
}
