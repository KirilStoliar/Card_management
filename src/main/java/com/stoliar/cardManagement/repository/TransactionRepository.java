package com.stoliar.cardManagement.repository;

import com.stoliar.cardManagement.model.Card;
import com.stoliar.cardManagement.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllBySourceCardInOrTargetCardIn(List<Card> sourceCards, List<Card> targetCards, Pageable pageable);
}