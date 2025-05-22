package com.stoliar.cardManagement.controller;

import com.stoliar.cardManagement.dto.TransactionDto;
import com.stoliar.cardManagement.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get current user's transactions")
    public ResponseEntity<Page<TransactionDto>> getUserTransactions(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(transactionService.getCurrentUserTransactions(pageable));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Transfer between user's cards")
    public ResponseEntity<TransactionDto> transferBetweenCards(
            @Valid @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.transferBetweenCards(transactionDto));
    }
}