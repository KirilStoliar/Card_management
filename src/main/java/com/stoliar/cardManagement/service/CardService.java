package com.stoliar.cardManagement.service;

import com.stoliar.cardManagement.dto.CardDto;
import com.stoliar.cardManagement.exception.EntityNotFoundException;
import com.stoliar.cardManagement.exception.InvalidCardOperationException;
import com.stoliar.cardManagement.model.Card;
import com.stoliar.cardManagement.model.User;
import com.stoliar.cardManagement.repository.CardRepository;
import com.stoliar.cardManagement.repository.UserRepository;
import com.stoliar.cardManagement.util.CardEncryptor;
import com.stoliar.cardManagement.util.CardMasker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CardEncryptor cardEncryptor;
    private final CardMasker cardMasker;

    @Transactional
    public CardDto createCard(CardDto cardDto) {
        if (cardDto.getUserId() == null || cardDto.getUserId() <= 0) {
            throw new IllegalArgumentException("User ID must be provided");
        }

        User user = userRepository.findById(cardDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + cardDto.getUserId()));

        if (cardDto.getCardNumber() == null || cardDto.getCardNumber().isEmpty()) {
            throw new IllegalArgumentException("Card number must be provided");
        }

        if (cardDto.getCardNumber() == null || !cardDto.getCardNumber().matches("\\d{16}")) {
            throw new InvalidCardOperationException("Card number must be 16 digits");
        }

        String encryptedCardNumber = cardEncryptor.encrypt(cardDto.getCardNumber());

        if (cardRepository.existsByCardNumber(encryptedCardNumber)) {
            throw new InvalidCardOperationException("Card with this number already exists");
        }

        Card card = Card.builder()
                .cardNumber(encryptedCardNumber)
                .cardHolderName(cardDto.getCardHolderName())
                .expirationDate(cardDto.getExpirationDate())
                .status(cardDto.getStatus() != null ? cardDto.getStatus() : Card.CardStatus.ACTIVE)
                .balance(cardDto.getBalance() != null ? cardDto.getBalance() : BigDecimal.ZERO)
                .user(user)
                .build();

        Card savedCard = cardRepository.save(card);
        return mapToDto(savedCard);
    }

    @Transactional(readOnly = true)
    public CardDto getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: " + id));
        return mapToDto(card);
    }

    @Transactional(readOnly = true)
    public Page<CardDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Page<CardDto> getCurrentUserCards(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return cardRepository.findAllByUser(currentUser, pageable).map(this::mapToDto);
    }

    @Transactional
    public CardDto blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (card.getStatus() == Card.CardStatus.BLOCKED) {
            throw new InvalidCardOperationException("Card is already blocked");
        }

        card.setStatus(Card.CardStatus.BLOCKED);
        Card updatedCard = cardRepository.save(card);
        return mapToDto(updatedCard);
    }

    @Transactional
    public CardDto activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        if (card.getStatus() == Card.CardStatus.ACTIVE) {
            throw new InvalidCardOperationException("Card is already active");
        }

        card.setStatus(Card.CardStatus.ACTIVE);
        Card updatedCard = cardRepository.save(card);
        return mapToDto(updatedCard);
    }

    @Transactional
    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("Card not found with id: " + cardId);
        }
        cardRepository.deleteById(cardId);
    }

    private CardDto mapToDto(Card card) {
        String decryptedNumber = cardEncryptor.decrypt(card.getCardNumber());

        return CardDto.builder()
                .id(card.getId())
                .cardNumber(cardEncryptor.decrypt(card.getCardNumber())) // Полный номер только для админа
                .maskedCardNumber(cardMasker.maskCardNumber(decryptedNumber))
                .cardHolderName(card.getCardHolderName())
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .balance(card.getBalance())
                .userId(card.getUser().getId())
                .userEmail(card.getUser().getEmail())
                .build();
    }
}