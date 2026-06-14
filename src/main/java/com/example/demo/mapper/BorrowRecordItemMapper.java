package com.example.demo.mapper;

import com.example.demo.model.BorrowRecordItem;
import com.example.demo.model.ChapterProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BorrowRecordItemMapper {

    public List<BorrowRecordItem> toBorrowRecordItems(UUID borrowUUID, List<ChapterProjection> chapterProjections) {

        return chapterProjections.stream()
                .map(chapter -> BorrowRecordItem.builder()
                        .borrowUuid(borrowUUID)
                        .chapterUuid(chapter.getChapterUuid())
                        .titleSnapshot(chapter.getTitle())
                        .secondTitleSnapshot(chapter.getSecondTitle())
                        .chapterNumberSnapshot(chapter.getChapterNumber())
                        .build())
                .toList();
    }
}
