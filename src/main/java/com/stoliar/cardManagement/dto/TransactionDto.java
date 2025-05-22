package com.stoliar.cardManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String targetCardNumber;
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sourceCardNumber;
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String sourceCardMaskedNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String targetCardMaskedNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime transactionDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;
}