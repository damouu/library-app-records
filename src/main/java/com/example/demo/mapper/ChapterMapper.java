package com.example.demo.mapper;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.model.ChapterProjection;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ChapterMapper {

    public ChapterProjection toEventData(ChapterCreatedEvent chapter) {

        return ChapterProjection.builder()
                .chapterUuid(chapter.data().chapter_uuid())
                .seriesUuid(chapter.data().series_uuid())
                .title(chapter.data().title())
                .secondTitle(chapter.data().second_title())
                .chapterNumber(chapter.data().chapter_number())
                .coverArtworkUrl(chapter.data().cover_artwork_url())
                .publicationDate(LocalDate.parse(chapter.data().publication_date()))
                .build();
    }

}
