package com.example.demo.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterSummaryDTO {

    private UUID chapterUuid;

    private UUID seriesUuid;

    private String title;

    private String secondTitle;

    private Integer chapterNumber;

    private String coverArtworkUrl;

    private String publicationDate;
}