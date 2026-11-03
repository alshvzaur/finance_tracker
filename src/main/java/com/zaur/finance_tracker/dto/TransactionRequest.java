package com.zaur.finance_tracker.dto;

import com.zaur.finance_tracker.model.Category;
import com.zaur.finance_tracker.model.Currency;
import com.zaur.finance_tracker.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequest {

    @NotNull(message = "Type is required")
    TransactionType type;

    @Positive
    @NotNull(message = "Enter amount")
    BigDecimal amount;

    @NotNull(message = "Currency is required")
    Currency currency;

    @NotNull(message = "Select category")
    Category category;

    String description;

    @NotNull(message = "Enter date")
    LocalDate date;
}
