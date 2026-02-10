package com.example.demo.unit.service;

import com.example.demo.repository.RecordSummaryRepository;
import com.example.demo.service.RecordService;
import com.example.demo.view.BorrowSummaryView;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordSummaryRepository recordSummaryRepository;

    @InjectMocks
    private RecordService recordService;

    @Test
    void getHistory_current_unreturned_borrows() {
        UUID memberCardUUID = UUID.randomUUID();
        Map<String, String> allParams = new HashMap<>();
        allParams.put("page", "0");
        allParams.put("size", "10");
        allParams.put("sort", "UNSORTED");
        allParams.put("direction", "asc");
        Sort.Direction direction = Sort.Direction.fromString("asc");
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by(direction, "UNSORTED"));
        List<BorrowSummaryView> borrowSummaryViews = Instancio.ofList(BorrowSummaryView.class).size(1).set(field(BorrowSummaryView::getBorrowStartDate), LocalDate.now().minusWeeks(1)).set(field(BorrowSummaryView::getBorrowEndDate), LocalDate.now().plusWeeks(2)).set(field(BorrowSummaryView::getBorrowReturnDate), null).set(field(BorrowSummaryView::getReturnLately), false).set(field(BorrowSummaryView::getDaysLate), null).set(field(BorrowSummaryView::getLateFee), null).create();
        Page<BorrowSummaryView> mockPage = new PageImpl<>(borrowSummaryViews, expectedPageable, borrowSummaryViews.size());
        when(recordSummaryRepository.getRecordSummaries(memberCardUUID, expectedPageable)).thenReturn(mockPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
        assertEquals(0, response.getBody().get("unreturned_borrow_position"));
        assertNotNull(response.getBody().get("borrows_UUID"));
        Map<String, Object> borrowsMap = (Map<String, Object>) response.getBody().get("borrows_UUID");
        Map<String, Object> innerValue = (Map<String, Object>) borrowsMap.values().stream().findFirst().orElseThrow();
        assertEquals(borrowSummaryViews.get(0).getBorrowUuid().toString(), borrowsMap.keySet().iterator().next());
        assertEquals(innerValue.get("borrow_start_date").toString(), borrowSummaryViews.get(0).getBorrowStartDate().toString());
        assertEquals(innerValue.get("borrow_expected_end_date").toString(), borrowSummaryViews.get(0).getBorrowEndDate().toString());
        assertEquals("null", innerValue.get("borrow_return_date").toString());
        assertFalse((Boolean) innerValue.get("return_lately"));
        Assertions.assertNull(innerValue.get("days_late"));
        Assertions.assertNull(innerValue.get("late_fee"));
        assertNotNull(response.getBody().get("pageable"));

    }


    @Test
    void getHistory_no_borrows_history() {
        UUID memberCardUUID = UUID.randomUUID();
        Map<String, String> allParams = new HashMap<>();
        allParams.put("page", "0");
        allParams.put("size", "10");
        allParams.put("sort", "UNSORTED");
        allParams.put("direction", "asc");
        Sort.Direction direction = Sort.Direction.fromString("asc");
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by(direction, "UNSORTED"));
        List<BorrowSummaryView> emptyList = Collections.emptyList();
        Page<BorrowSummaryView> emptyPage = new PageImpl<>(emptyList);
        when(recordSummaryRepository.getRecordSummaries(any(), any())).thenReturn(emptyPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
        Map<String, Object> borrowsMap = (Map<String, Object>) response.getBody().get("borrows_UUID");
        Assertions.assertTrue(borrowsMap.isEmpty());
    }


    @Test
    void getHistory_returned_borrows_exists() {
        UUID memberCardUUID = UUID.randomUUID();
        Map<String, String> allParams = new HashMap<>();
        allParams.put("page", "0");
        allParams.put("size", "10");
        allParams.put("sort", "UNSORTED");
        allParams.put("direction", "asc");
        Sort.Direction direction = Sort.Direction.fromString("asc");
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by(direction, "UNSORTED"));
        List<BorrowSummaryView> borrowSummaryViews = Instancio.ofList(BorrowSummaryView.class).size(1).set(field(BorrowSummaryView::getBorrowStartDate), LocalDate.now().minusWeeks(1)).set(field(BorrowSummaryView::getBorrowEndDate), LocalDate.now().plusWeeks(2)).set(field(BorrowSummaryView::getBorrowReturnDate), LocalDate.now().plusDays(3)).set(field(BorrowSummaryView::getReturnLately), false).set(field(BorrowSummaryView::getDaysLate), 0).set(field(BorrowSummaryView::getLateFee), BigDecimal.valueOf(0)).create();
        Page<BorrowSummaryView> mockPage = new PageImpl<>(borrowSummaryViews, expectedPageable, borrowSummaryViews.size());
        when(recordSummaryRepository.getRecordSummaries(memberCardUUID, expectedPageable)).thenReturn(mockPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
        assertNotNull(response.getBody().get("borrows_UUID"));
        Map<String, Object> borrowsMap = (Map<String, Object>) response.getBody().get("borrows_UUID");
        Map<String, Object> innerValue = (Map<String, Object>) borrowsMap.values().stream().findFirst().orElseThrow();
        assertEquals(borrowSummaryViews.get(0).getBorrowUuid().toString(), borrowsMap.keySet().iterator().next());
        assertEquals(innerValue.get("borrow_start_date").toString(), borrowSummaryViews.get(0).getBorrowStartDate().toString());
        assertEquals(innerValue.get("borrow_return_date").toString(), borrowSummaryViews.get(0).getBorrowReturnDate().toString());
        assertEquals(innerValue.get("borrow_expected_end_date").toString(), borrowSummaryViews.get(0).getBorrowEndDate().toString());
        assertFalse((Boolean) innerValue.get("return_lately"));
        assertEquals(borrowSummaryViews.get(0).getDaysLate(), innerValue.get("days_late"));
        assertEquals(borrowSummaryViews.get(0).getLateFee(), innerValue.get("late_fee"));
    }


    @Test
    void shouldMergeBookUuidsIntoChaptersList() {
        UUID memberUuid = UUID.randomUUID();
        UUID borrowUuid = UUID.randomUUID();
        String bookUuid = "book-123";

        Map<String, Object> chapter = new HashMap<>();
        chapter.put("chapter_title", "Jujutsu Kaisen");

        Map<String, Object> book = new HashMap<>();
        book.put("book_uuid", bookUuid);

        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("chapters", new ArrayList<>(List.of(chapter)));
        detailsMap.put("books", List.of(book));

        BorrowSummaryView mockView = mock(BorrowSummaryView.class);
        when(mockView.getBorrowUuid()).thenReturn(borrowUuid);
        when(mockView.getBookDetails()).thenReturn(List.of(detailsMap));
        when(mockView.getBorrowStartDate()).thenReturn(java.time.LocalDate.now());

        Page<BorrowSummaryView> page = new PageImpl<>(List.of(mockView));

        when(recordSummaryRepository.getRecordSummaries(any(), any())).thenReturn(page);

        Map<String, Object> params = Map.of("page", "0", "size", "10", "sort", "borrow_start_date", "direction", "DESC");
        ResponseEntity<HashMap<String, Object>> response = recordService.getHistory(memberUuid, params);

        HashMap<String, Object> body = response.getBody();
        Map<String, Object> borrows = (Map<String, Object>) body.get("borrows_UUID");
        Map<String, Object> record = (Map<String, Object>) borrows.get(borrowUuid.toString());
        List<Map<String, Object>> resultChapters = (List<Map<String, Object>>) record.get("chapters");

        assertEquals(bookUuid, resultChapters.get(0).get("book_uuid"), "The book_uuid should be injected into the chapter object");
    }

    @Test
    void shouldHandleMissingBooksOrChaptersGracefully() {
        UUID memberUuid = UUID.randomUUID();
        UUID borrowUuid = UUID.randomUUID();


        Map<String, Object> detailsMap = new HashMap<>();
        Map<String, Object> chapter = new HashMap<>();
        chapter.put("chapter_title", "Jujutsu Kaisen");

        detailsMap.put("chapters", List.of(chapter));
        detailsMap.put("books", null);


        BorrowSummaryView mockView = mock(BorrowSummaryView.class);
        when(mockView.getBorrowUuid()).thenReturn(borrowUuid);
        when(mockView.getBookDetails()).thenReturn(List.of(detailsMap));
        when(mockView.getBorrowStartDate()).thenReturn(java.time.LocalDate.now());
        when(mockView.getBorrowEndDate()).thenReturn(java.time.LocalDate.now());

        Page<BorrowSummaryView> page = new PageImpl<>(List.of(mockView));

        when(recordSummaryRepository.getRecordSummaries(any(), any())).thenReturn(page);

        Map<String, Object> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "10");
        params.put("sort", "borrow_start_date");
        params.put("direction", "DESC");

        ResponseEntity<HashMap<String, Object>> response = recordService.getHistory(memberUuid, params);

        HashMap<String, Object> body = response.getBody();
        assertNotNull(body);

        Map<String, Object> borrows = (Map<String, Object>) body.get("borrows_UUID");
        Map<String, Object> record = (Map<String, Object>) borrows.get(borrowUuid.toString());
        List<Map<String, Object>> resultChapters = (List<Map<String, Object>>) record.get("chapters");

        assertFalse(resultChapters.get(0).containsKey("book_uuid"), "Should not have injected book_uuid because books list was null");
    }
}