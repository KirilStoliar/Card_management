package com.stoliar.cardManagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stoliar.cardManagement.model.Card;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    @NotNull(message = "Card number cannot be null")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String cardNumber;

    @NotBlank(message = "Card holder name cannot be blank")
    private String cardHolderName;

    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    private Card.CardStatus status;

    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be positive")
    private Long userId; // Должен быть реальный ID существующего пользователя

    // Только для отображения
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String maskedCardNumber;
    @JsonIgnore
    private String userEmail;


}