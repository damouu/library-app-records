package com.example.demo.dto;

import java.util.UUID;


public record ChapterSummaryDTO(
        UUID chapterUuid,

        UUID seriesUuid,

        String title,

        String secondTitle,

        Integer chapterNumber,

        String coverArtworkUrl,

        String publicationDate
) {
}