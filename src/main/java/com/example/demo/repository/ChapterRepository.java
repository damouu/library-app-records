package com.example.demo.repository;

import com.example.demo.model.ChapterProjection;
import com.example.demo.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterProjection, Integer>, JpaSpecificationExecutor<BorrowRecord> {

    List<ChapterProjection> findByChapterUuidIn(Collection<UUID> chapterUuids);
}
