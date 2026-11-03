package com.zaur.finance_tracker.dto;

import com.zaur.finance_tracker.model.Category;
import com.zaur.finance_tracker.model.Currency;
import com.zaur.finance_tracker.model.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TransactionResponse {

    UUID transactionId;
    BigDecimal amount;
    String description;
    TransactionType  type;
    Category category;
    Currency currency;
    LocalDate transactionDate;

}
