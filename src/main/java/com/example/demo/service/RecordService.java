package com.example.demo.service;

import com.example.demo.repository.RecordSummaryRepository;
import com.example.demo.utils.PaginationUtil;
import com.example.demo.view.BorrowSummaryView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordSummaryRepository recordSummaryRepository;

    public ResponseEntity<HashMap<String, Object>> getHistory(UUID memberCardUUID, Map allParams) throws ResponseStatusException {
        Pageable pageable2 = PaginationUtil.extractPage(allParams);
        int page = pageable2.getPageNumber();
        int size = pageable2.getPageSize();
        String sortProperty = (String) allParams.get("sort");
        String sortDirection = (String) allParams.get("direction");
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<BorrowSummaryView> borrowSummaries = recordSummaryRepository.getRecordSummaries(memberCardUUID, pageable);
        HashMap<String, Object> response = new LinkedHashMap<>();
        response.put("memberCard_UUID", memberCardUUID.toString());
        HashMap<String, Object> borrow_history = new LinkedHashMap<>();
        if (!borrowSummaries.isEmpty()) {
            if (borrowSummaries.getContent().getFirst().getBorrowReturnDate() == null) {
                response.put("unreturned_borrows", true);
                response.put("unreturned_borrow_position", 0);
            }
            for (BorrowSummaryView borrowSummaryView : borrowSummaries) {
                HashMap<String, Object> dede = new LinkedHashMap<>();
                List<Object> books = new ArrayList<>(borrowSummaryView.getBookDetails().getFirst().values());
                var popo = books.getFirst();
                dede.put("borrow_start_date", String.valueOf(borrowSummaryView.getBorrowStartDate()));
                dede.put("borrow_expected_end_date", String.valueOf(borrowSummaryView.getBorrowEndDate()));
                dede.put("borrow_return_date", String.valueOf(borrowSummaryView.getBorrowReturnDate()));
                dede.put("return_lately", borrowSummaryView.getReturnLately());
                dede.put("days_late", borrowSummaryView.getDaysLate());
                dede.put("late_fee", borrowSummaryView.getLateFee());
                dede.put("chapters", popo);
                borrow_history.put(String.valueOf(borrowSummaryView.getBorrowUuid()), dede);
            }
        }
        response.put("borrows_UUID", borrow_history);
        response.put("pageable", borrowSummaries.getPageable());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
