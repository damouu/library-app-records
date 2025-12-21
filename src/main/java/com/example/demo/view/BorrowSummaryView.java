package com.example.demo.view;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
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
@Subselect("SELECT * FROM record")
public class BorrowSummaryView {

    @Id
    private UUID borrowUuid;

    private LocalDate borrowReturnDate;

    private LocalDate borrowEndDate;

    private LocalDate borrowStartDate;

    private Integer daysLate;

    private BigDecimal lateFee;

    private Boolean returnLately;

    @Type(type = "jsonb")
    @Column(name = "borrow_details", columnDefinition = "jsonb")
    private List<Map<String, Object>> bookDetails;

}