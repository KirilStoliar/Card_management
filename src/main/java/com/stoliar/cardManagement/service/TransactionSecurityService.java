package com.stoliar.cardManagement.service;

import com.stoliar.cardManagement.model.User;
import com.stoliar.cardManagement.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionSecurityService {
    private final CardRepository cardRepository;

    public boolean isUserCards(Authentication authentication, Long sourceCardId, Long targetCardId) {
        User user = (User) authentication.getPrincipal();
        return cardRepository.existsByIdAndUserId(sourceCardId, user.getId()) && 
               cardRepository.existsByIdAndUserId(targetCardId, user.getId());
    }
}