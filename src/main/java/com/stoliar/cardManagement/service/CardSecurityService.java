package com.stoliar.cardManagement.service;

import com.stoliar.cardManagement.model.User;
import com.stoliar.cardManagement.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardSecurityService {
    private final CardRepository cardRepository;

    public boolean isCardOwner(Authentication authentication, Long cardId) {
        User user = (User) authentication.getPrincipal();
        return cardRepository.existsByIdAndUserId(cardId, user.getId());
    }
}