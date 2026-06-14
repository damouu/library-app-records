package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterProjection {

    @Id
    @Getter(onMethod = @__(@JsonIgnore))
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_sequence")
    @SequenceGenerator(name = "chapter_sequence", allocationSize = 1, sequenceName = "chapter_sequence")
    private Long id;

    @Column(nullable = false)
    private UUID chapterUuid;

    @Column(nullable = false)
    private UUID seriesUuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String secondTitle;

    @Column(nullable = false)
    private Integer chapterNumber;

    private String coverArtworkUrl;

    private LocalDate publicationDate;

}
