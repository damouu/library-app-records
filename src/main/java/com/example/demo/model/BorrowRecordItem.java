package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordItem {

    @Id
    @Getter(onMethod = @__(@JsonIgnore))
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
    @SequenceGenerator(name = "book_sequence", allocationSize = 1, sequenceName = "book_sequence")
    private int id;

    @Column(nullable = false)
    private UUID borrowUuid;

    @Column(nullable = false)
    private UUID chapterUuid;

    @Column(nullable = false)
    private String titleSnapshot;

    @Column(nullable = false)
    private String secondTitleSnapshot;

    @Column(nullable = false)
    private Integer chapterNumberSnapshot;

}
