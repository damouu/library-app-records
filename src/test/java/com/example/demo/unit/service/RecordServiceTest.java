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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        Mockito.when(recordSummaryRepository.getRecordSummaries(memberCardUUID, expectedPageable)).thenReturn(mockPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
        Assertions.assertEquals(0, response.getBody().get("unreturned_borrow_position"));
        Assertions.assertNotNull(response.getBody().get("borrows_UUID"));
        Map<String, Object> borrowsMap = (Map<String, Object>) response.getBody().get("borrows_UUID");
        Map<String, Object> innerValue = (Map<String, Object>) borrowsMap.values().stream().findFirst().orElseThrow();
        Assertions.assertEquals(borrowSummaryViews.get(0).getBorrowUuid().toString(), borrowsMap.keySet().iterator().next());
        Assertions.assertEquals(innerValue.get("borrow_start_date").toString(), borrowSummaryViews.get(0).getBorrowStartDate().toString());
        Assertions.assertEquals(innerValue.get("borrow_expected_end_date").toString(), borrowSummaryViews.get(0).getBorrowEndDate().toString());
        Assertions.assertEquals("null", innerValue.get("borrow_return_date").toString());
        Assertions.assertFalse((Boolean) innerValue.get("return_lately"));
        Assertions.assertNull(innerValue.get("days_late"));
        Assertions.assertNull(innerValue.get("late_fee"));
        Assertions.assertNotNull(response.getBody().get("pageable"));

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
        Mockito.when(recordSummaryRepository.getRecordSummaries(any(), any())).thenReturn(emptyPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
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
        Mockito.when(recordSummaryRepository.getRecordSummaries(memberCardUUID, expectedPageable)).thenReturn(mockPage);
        var response = recordService.getHistory(memberCardUUID, allParams);
        verify(recordSummaryRepository, times(1)).getRecordSummaries(memberCardUUID, expectedPageable);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(memberCardUUID.toString(), response.getBody().get("memberCard_UUID").toString());
        Assertions.assertNotNull(response.getBody().get("borrows_UUID"));
        Map<String, Object> borrowsMap = (Map<String, Object>) response.getBody().get("borrows_UUID");
        Map<String, Object> innerValue = (Map<String, Object>) borrowsMap.values().stream().findFirst().orElseThrow();
        Assertions.assertEquals(borrowSummaryViews.get(0).getBorrowUuid().toString(), borrowsMap.keySet().iterator().next());
        Assertions.assertEquals(innerValue.get("borrow_start_date").toString(), borrowSummaryViews.get(0).getBorrowStartDate().toString());
        Assertions.assertEquals(innerValue.get("borrow_return_date").toString(), borrowSummaryViews.get(0).getBorrowReturnDate().toString());
        Assertions.assertEquals(innerValue.get("borrow_expected_end_date").toString(), borrowSummaryViews.get(0).getBorrowEndDate().toString());
        Assertions.assertFalse((Boolean) innerValue.get("return_lately"));
        Assertions.assertEquals(borrowSummaryViews.get(0).getDaysLate(), innerValue.get("days_late"));
        Assertions.assertEquals(borrowSummaryViews.get(0).getLateFee(), innerValue.get("late_fee"));
    }
}