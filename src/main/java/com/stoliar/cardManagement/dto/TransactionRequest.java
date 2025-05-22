package com.stoliar.cardManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotNull
    private Long userId;
    
    @NotNull
    private Long sourceCardId;
    
    @NotNull
    private Long targetCardId;
    
    @NotNull
    @Min(value = 1, message = "Amount must be greater than 0")
    private BigDecimal amount;
}