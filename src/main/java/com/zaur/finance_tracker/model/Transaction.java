package com.zaur.finance_tracker.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions")
public class Transaction {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, name = "id")
    @Id
    UUID id;

    @Column(name = "user_id",  nullable = false)
    UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",  nullable = false)
    TransactionType type;

    @Column(name = "amount",  nullable = false)
    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency",  nullable = false)
    Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "category",   nullable = false)
    Category category;

    @Column(name = "description")
    String description;

    @Column(name = "transaction_date",  nullable = false)
    LocalDate date;
}
