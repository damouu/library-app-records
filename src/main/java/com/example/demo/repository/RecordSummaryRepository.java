package com.example.demo.repository;

import com.example.demo.model.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RecordSummaryRepository extends Repository<BorrowRecord, Integer> {

    @Query(value = """
            SELECT
                CAST(br.borrow_uuid AS varchar) as borrowUuid,
                br.borrow_start_date as borrowStartDate,
                br.borrow_end_date as borrowEndDate,
                br.borrow_return_date as borrowReturnDate,
                br.days_late as daysLate,
                br.late_fee as lateFee,
                br.return_lately as returnLately,
                    CAST(
                        json_agg(
                        json_build_object(
                        'chapterUuid', bri.chapter_uuid,
                        'seriesUuid', cp.series_uuid,
                        'title', bri.title_snapshot,
                        'secondTitle', bri.second_title_snapshot,
                        'chapterNumber', bri.chapter_number_snapshot,
                        'secondTitle', bri.chapter_number_snapshot,
                        'coverArtworkUrl', cp.cover_artwork_url,
                        'publicationDate', cp.publication_date
                                )
                            ) AS text
                        ) as borrowDetails
            FROM borrow_record br
            JOIN borrow_record_item bri
                ON br.borrow_uuid = bri.borrow_uuid
            JOIN public.chapter_projection cp on bri.chapter_uuid = cp.chapter_uuid
            WHERE br.member_card_uuid = :memberCardUuid
            GROUP BY
                br.borrow_uuid,
                br.borrow_start_date,
                br.borrow_end_date,
                br.borrow_return_date,
                br.days_late,
                br.late_fee,
                br.return_lately
            """, countQuery = """
            SELECT COUNT(*)
            FROM borrow_record br
            WHERE br.member_card_uuid = :memberCardUuid
            """, nativeQuery = true)
    Page<RecordSummaryProjection> getRecordSummaries(@Param("memberCardUuid") UUID memberCardUuid, Pageable pageable);
}