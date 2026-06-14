package com.example.demo.unit.service;

import com.example.demo.mapper.BorrowRecordItemMapper;
import com.example.demo.model.BorrowRecordItem;
import com.example.demo.model.ChapterProjection;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BorrowRecordItemMapperTest {

    private final BorrowRecordItemMapper mapper = new BorrowRecordItemMapper();

    @Test
    void testToBorrowRecordItems() {

        UUID borrowUuid = UUID.randomUUID();

        ChapterProjection chapter1 = ChapterProjection.builder().chapterUuid(UUID.randomUUID()).title("Naruto").secondTitle("Part 1").chapterNumber(1).build();

        ChapterProjection chapter2 = ChapterProjection.builder().chapterUuid(UUID.randomUUID()).title("Bleach").secondTitle("Soul Society").chapterNumber(2).build();

        List<BorrowRecordItem> result = mapper.toBorrowRecordItems(borrowUuid, List.of(chapter1, chapter2));

        assertEquals(2, result.size());
    }
}