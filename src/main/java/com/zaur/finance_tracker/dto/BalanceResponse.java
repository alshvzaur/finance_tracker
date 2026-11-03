package com.zaur.finance_tracker.dto;

import com.zaur.finance_tracker.model.Currency;
import com.zaur.finance_tracker.model.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BalanceResponse {

    Currency currency;

    BigDecimal income;

    BigDecimal expense;

    BigDecimal balance;

}
