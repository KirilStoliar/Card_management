package com.stoliar.cardManagement.util;

import org.springframework.stereotype.Component;

@Component
public class CardMasker {
    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 12) {
            return cardNumber;
        }

        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}