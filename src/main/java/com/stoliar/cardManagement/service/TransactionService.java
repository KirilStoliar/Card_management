package com.stoliar.cardManagement.service;

import com.stoliar.cardManagement.dto.TransactionDto;
import com.stoliar.cardManagement.exception.EntityNotFoundException;
import com.stoliar.cardManagement.exception.InsufficientFundsException;
import com.stoliar.cardManagement.exception.InvalidCardOperationException;
import com.stoliar.cardManagement.model.Card;
import com.stoliar.cardManagement.model.Transaction;
import com.stoliar.cardManagement.model.User;
import com.stoliar.cardManagement.repository.CardRepository;
import com.stoliar.cardManagement.repository.TransactionRepository;
import com.stoliar.cardManagement.util.CardEncryptor;
import com.stoliar.cardManagement.util.CardMasker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final CardMasker cardMasker;
    private final CardEncryptor cardEncryptor;

    @Transactional(readOnly = true)
    public Page<TransactionDto> getCurrentUserTransactions(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        List<Card> userCards = cardRepository.findAllByUser(currentUser);
        return transactionRepository
                .findAllBySourceCardInOrTargetCardIn(userCards, userCards, pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public TransactionDto transferBetweenCards(TransactionDto transactionDto) {
        Card sourceCard = findCardByNumber(transactionDto.getSourceCardNumber());
        Card targetCard = findCardByNumber(transactionDto.getTargetCardNumber());

        validateTransfer(sourceCard, targetCard, transactionDto.getAmount());

        sourceCard.setBalance(sourceCard.getBalance().subtract(transactionDto.getAmount()));
        targetCard.setBalance(targetCard.getBalance().add(transactionDto.getAmount()));

        Transaction transaction = Transaction.builder()
                .sourceCard(sourceCard)
                .targetCard(targetCard)
                .amount(transactionDto.getAmount())
                .transactionDate(LocalDateTime.now())
                .description(transactionDto.getDescription())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDto(savedTransaction);
    }

    private Card findCardByNumber(String cardNumber) {
        String encryptedNumber = cardEncryptor.encrypt(cardNumber);
        return cardRepository.findByCardNumber(encryptedNumber)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }

    private void validateTransfer(Card sourceCard, Card targetCard, BigDecimal amount) {
        if (sourceCard.getStatus() != Card.CardStatus.ACTIVE ||
                targetCard.getStatus() != Card.CardStatus.ACTIVE) {
            throw new InvalidCardOperationException("Both cards must be active");
        }

        if (sourceCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on source card");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCardOperationException("Amount must be positive");
        }
    }

    private TransactionDto mapToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .sourceCardNumber(cardEncryptor.decrypt(transaction.getSourceCard().getCardNumber()))
                .targetCardNumber(cardEncryptor.decrypt(transaction.getTargetCard().getCardNumber()))
                .sourceCardMaskedNumber(cardMasker.maskCardNumber(
                        cardEncryptor.decrypt(transaction.getSourceCard().getCardNumber())))
                .targetCardMaskedNumber(cardMasker.maskCardNumber(
                        cardEncryptor.decrypt(transaction.getTargetCard().getCardNumber())))
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .userId(transaction.getSourceCard().getUser().getId())
                .build();
    }
}