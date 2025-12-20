package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnEventPayload {

    private ReturnEventData data;

    private Metadata metadata;

}