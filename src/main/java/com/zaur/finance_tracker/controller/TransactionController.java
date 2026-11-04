package com.zaur.finance_tracker.controller;

import com.zaur.finance_tracker.dto.BalanceResponse;
import com.zaur.finance_tracker.dto.TransactionRequest;
import com.zaur.finance_tracker.dto.TransactionResponse;
import com.zaur.finance_tracker.exception.TransactionNotFoundException;
import com.zaur.finance_tracker.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/transaction")
public class TransactionController {

    TransactionService transactionService;

    @PostMapping("/add")
    public TransactionResponse  addTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        return transactionService.addTransaction(transactionRequest);
    }

    @DeleteMapping("/{id}")
    public void  deleteTransaction(@PathVariable UUID id) throws TransactionNotFoundException {
        transactionService.deleteTransactionById(id);
    }

    @GetMapping("/{id}")
    public TransactionResponse findTransactionById(@PathVariable UUID id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping("/my")
    public List<TransactionResponse> findTransactionByUser() {
        return transactionService.getMyTransactions();
    }

    @GetMapping("/summary")
    public List<BalanceResponse> getSummary() {
        return transactionService.getSummary();
    }
}
