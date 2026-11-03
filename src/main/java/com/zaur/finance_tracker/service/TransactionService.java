package com.zaur.finance_tracker.service;

import com.zaur.finance_tracker.dto.BalanceResponse;
import com.zaur.finance_tracker.dto.TransactionRequest;
import com.zaur.finance_tracker.dto.TransactionResponse;
import com.zaur.finance_tracker.exception.TransactionNotFoundException;
import com.zaur.finance_tracker.mapper.TransactionMapper;
import com.zaur.finance_tracker.model.Currency;
import com.zaur.finance_tracker.model.Transaction;
import com.zaur.finance_tracker.model.TransactionType;
import com.zaur.finance_tracker.repository.TransactionRepository;
import com.zaur.finance_tracker.security.AuthenticatedUser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionService {

    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;

    private AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         return (AuthenticatedUser) authentication.getPrincipal();
    }

    private Transaction getAccessibleTransaction(UUID transactionId) {
        AuthenticatedUser authenticatedUser = getAuthenticatedUser();
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(()-> new TransactionNotFoundException("Transaction not found: " + transactionId));

        if (authenticatedUser.getId().equals(transaction.getUserId()) || authenticatedUser.getRole().equals("ADMIN")) {
            return transaction;
        } else  {
            throw new TransactionNotFoundException("Transaction not found: " + transactionId);
        }
    }

    public TransactionResponse addTransaction(TransactionRequest  transactionRequest ) {
        Transaction transaction = transactionMapper.requestToTransaction(transactionRequest, getAuthenticatedUser().getId());
        return transactionMapper.transactionToResponse(transactionRepository.save(transaction));
    }

    public void deleteTransactionById(UUID id) {
        Transaction transaction = getAccessibleTransaction(id);
        transactionRepository.delete(transaction);
    }

    public TransactionResponse getTransaction(UUID transactionId) {
        Transaction transaction = getAccessibleTransaction(transactionId);
        return transactionMapper.transactionToResponse(transaction);
    }

    public List<TransactionResponse> getMyTransactions() {
        List<Transaction> transactionList = transactionRepository.findByUserId(getAuthenticatedUser().getId());
        List<TransactionResponse> transactionResponses = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            transactionResponses.add(transactionMapper.transactionToResponse(transaction));
        }

        return transactionResponses;
    }

    private BigDecimal sumByType(List<Object[]> rows, TransactionType type) {
        return rows.stream()
                .filter(row -> row[1] == type)
                .map(row -> (BigDecimal) row[2])
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<BalanceResponse> getSummary() {
        UUID userId = getAuthenticatedUser().getId();

        List<Object[]> rows = transactionRepository.getTransactionSummaryByUserId(userId);

        Map<Currency, List<Object[]>> byCurrency = rows.stream()
                .collect(Collectors.groupingBy(row -> (Currency) row[0]));

        return byCurrency.entrySet().stream()
                .map(entry -> {
                    BigDecimal income = sumByType(entry.getValue(), TransactionType.INCOME);
                    BigDecimal expense = sumByType(entry.getValue(), TransactionType.EXPENSE);

                    return BalanceResponse.builder()
                            .currency(entry.getKey())
                            .income(income)
                            .expense(expense)
                            .balance(income.subtract(expense))
                            .build();
                })
                .toList();
    }
}
