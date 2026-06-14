package com.example.demo.unit.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.model.ChapterProjection;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.service.CatalogService;
import com.example.demo.service.KafkaPayloadBuilderService;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private KafkaPayloadBuilderService payloadBuilderService;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    @DisplayName("Should save chapter projection when chapter created event is received")
    void insertNewChapters_success() {

        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);

        ChapterProjection projection = Instancio.create(ChapterProjection.class);

        when(payloadBuilderService.buildChapterEntities(event)).thenReturn(projection);

        catalogService.insertNewChapters(event);

        verify(payloadBuilderService, times(1)).buildChapterEntities(event);

        verify(chapterRepository, times(1)).save(projection);
    }
}