package com.example.demo.model;

import com.example.demo.dto.ChapterDetails;
import com.example.demo.dto.ReturnEventPayload;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @Getter(onMethod = @__(@JsonIgnore))
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
    @SequenceGenerator(name = "book_sequence", allocationSize = 1, sequenceName = "book_sequence")
    private int id;

    @Column(nullable = false, name = "member_card_uuid")
    private UUID memberCardUuid;

    @Column(name = "return_lately")
    private Boolean returnLately;

    @Column(name = "days_late")
    private Integer daysLate;

    @Column(name = "late_fee")
    private BigDecimal lateFee;

    @Column(nullable = false, name = "borrow_uuid")
    private UUID borrowUuid;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<ChapterDetails> chapters;

    @Column(name = "borrow_start_date", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private LocalDate borrowStartDate;

    @Column(name = "borrow_end_date", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private LocalDate borrowEndDate;

    @Column(name = "borrow_return_date", columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private LocalDate borrowReturnDate;

    public void updateReturnInfo(ReturnEventPayload data) {
        this.borrowReturnDate = LocalDate.parse(data.getData().getNotificationData().getBorrowReturnDate());
        this.daysLate = data.getData().getNotificationData().getDaysLate();
        this.returnLately = data.getData().getNotificationData().isReturnLately();
        this.lateFee = data.getData().getNotificationData().getLateFee();
    }

}
