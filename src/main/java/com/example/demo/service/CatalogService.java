package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.model.ChapterProjection;
import com.example.demo.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final ChapterRepository chapterRepository;

    private final KafkaPayloadBuilderService payloadBuilderService;

    @Transactional
    public void insertNewChapters(ChapterCreatedEvent payload) {
        ChapterProjection chapterProjection = payloadBuilderService.buildChapterEntities(payload);
        chapterRepository.save(chapterProjection);
        log.info("saved new chapter");
    }
}
