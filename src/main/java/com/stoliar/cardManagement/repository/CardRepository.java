package com.stoliar.cardManagement.repository;

import com.stoliar.cardManagement.model.Card;
import com.stoliar.cardManagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findAllByUser(User user, Pageable pageable);
    List<Card> findAllByUser(User user);
    Optional<Card> findByCardNumber(String cardNumber);
    boolean existsByCardNumber(String cardNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Card c WHERE c.id = :cardId AND c.user.id = :userId")
    boolean existsByIdAndUserId(@Param("cardId") Long cardId, @Param("userId") Long userId);
}