package com.zaur.finance_tracker.mapper;

import com.zaur.finance_tracker.dto.TransactionRequest;
import com.zaur.finance_tracker.dto.TransactionResponse;
import com.zaur.finance_tracker.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionMapper {

    public Transaction requestToTransaction(TransactionRequest transactionRequest, UUID userId) {

        return Transaction.builder()
                .userId(userId)
                .type(transactionRequest.getType())
                .amount(transactionRequest.getAmount())
                .currency(transactionRequest.getCurrency())
                .category(transactionRequest.getCategory())
                .description(transactionRequest.getDescription())
                .date(transactionRequest.getDate())
                .build();
    }

    public TransactionResponse transactionToResponse(Transaction transaction) {

        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .currency(transaction.getCurrency())
                .transactionDate(transaction.getDate())
                .build();
    }
}
