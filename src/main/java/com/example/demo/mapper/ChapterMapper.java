package com.example.demo.mapper;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.model.ChapterProjection;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ChapterMapper {

    public ChapterProjection toEventData(ChapterCreatedEvent chapter) {

        return ChapterProjection.builder()
                .chapterUuid(chapter.getData().getChapter_uuid())
                .seriesUuid(chapter.getData().getSeries_uuid())
                .title(chapter.getData().getTitle())
                .secondTitle(chapter.getData().getSecond_title())
                .chapterNumber(chapter.getData().getChapter_number())
                .coverArtworkUrl(chapter.getData().getCover_artwork_url())
                .publicationDate(LocalDate.parse(chapter.getData().getPublication_date()))
                .build();
    }

}
