package com.example.demo.view;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonType.class)
@org.hibernate.annotations.Immutable
@Entity
public class BorrowSummaryView {

    @Id
    @Column(name = "borrow_uuid")
    private UUID borrowUuid;

    @Column(name = "borrow_return_date")
    private LocalDate borrowReturnDate;

    @Column(name = "borrow_end_date")
    private LocalDate borrowEndDate;

    @Column(name = "borrow_start_date")
    private LocalDate borrowStartDate;

    @Column(name = "days_late")
    private Integer daysLate;

    @Column(name = "late_fee")
    private BigDecimal lateFee;

    @Column(name = "return_lately")
    private Boolean returnLately;

    @Type(type = "jsonb")
    @Column(name = "borrow_details", columnDefinition = "jsonb")
    private List<Map<String, Object>> bookDetails;

}