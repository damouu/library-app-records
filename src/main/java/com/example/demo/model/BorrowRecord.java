package com.example.demo.model;

import com.example.demo.dto.ReturnCreatedEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    @Id
    @Getter(onMethod = @__(@JsonIgnore))
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
    @SequenceGenerator(name = "book_sequence", allocationSize = 1, sequenceName = "book_sequence")
    private int id;

    @Column(nullable = false, updatable = false)
    private UUID borrowUuid;

    @Column(nullable = false, updatable = false)
    private UUID memberCardUuid;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    private LocalDate borrowStartDate;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    private LocalDate borrowEndDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    private LocalDate borrowReturnDate;

    private Boolean returnLately;

    private Integer daysLate;

    private BigDecimal lateFee;

    public void updateReturnInfo(ReturnCreatedEvent data) {
        this.borrowReturnDate = LocalDate.parse(data.getData().getBorrow_return_date());
        this.daysLate = data.getData().getDays_late();
        this.returnLately = data.getData().isReturn_lately();
        this.lateFee = data.getData().getLate_fee();
    }

}
