package com.example.demo.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedItem {

    private UUID book_uuid;

    private UUID chapter_uuid;
    
}
