package com.zaur.finance_tracker.service;

import com.zaur.finance_tracker.dto.TransactionRequest;
import com.zaur.finance_tracker.dto.TransactionResponse;
import com.zaur.finance_tracker.exception.TransactionNotFoundException;
import com.zaur.finance_tracker.mapper.TransactionMapper;
import com.zaur.finance_tracker.model.Category;
import com.zaur.finance_tracker.model.Currency;
import com.zaur.finance_tracker.model.Transaction;
import com.zaur.finance_tracker.model.TransactionType;
import com.zaur.finance_tracker.repository.TransactionRepository;
import com.zaur.finance_tracker.security.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionMapper transactionMapper;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(testUserId, "testUser", "USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAddTransaction() {

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .type(TransactionType.EXPENSE)
                .amount(new BigDecimal("50.50"))
                .currency(Currency.AZN)
                .category(Category.FOOD)
                .description("")
                .date(LocalDate.parse("2026-11-04"))
                .build();

        Transaction transaction = Transaction.builder()
                        .id(UUID.randomUUID())
                .userId(testUserId)
                .type(TransactionType.EXPENSE)
                .amount(new BigDecimal("50.50"))
                .currency(Currency.AZN)
                .category(Category.FOOD)
                .description("")
                .date(LocalDate.parse("2026-11-04"))
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                        .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .currency(transaction.getCurrency())
                .transactionDate(transaction.getDate())
                .build();

        when(transactionMapper.requestToTransaction(transactionRequest, testUserId)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.transactionToResponse(transaction)).thenReturn(transactionResponse);

        TransactionResponse response = transactionService.addTransaction(transactionRequest);

        assertEquals(transactionResponse, response);

    }

    @Test
    void shouldThrowNotFoundWhenUserIsNotOwner() {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .type(TransactionType.EXPENSE)
                .amount(new BigDecimal("50.50"))
                .currency(Currency.AZN)
                .category(Category.FOOD)
                .description("")
                .date(LocalDate.parse("2026-11-04"))
                .build();

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(transaction.getId()));
    }

    @Test
    void shouldThrowNotFoundWhenUserIsAdmin() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(UUID.randomUUID(), "testAdmin", "ADMIN");
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .userId(testUserId)
                .type(TransactionType.EXPENSE)
                .amount(new BigDecimal("50.50"))
                .currency(Currency.AZN)
                .category(Category.FOOD)
                .description("")
                .date(LocalDate.parse("2026-11-04"))
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .transactionId(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .currency(transaction.getCurrency())
                .transactionDate(transaction.getDate())
                .build();


        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(transactionMapper.transactionToResponse(transaction)).thenReturn(transactionResponse);

        TransactionResponse response = transactionService.getTransaction(transaction.getId());

        assertEquals(transactionResponse, response);

    }
}
