package com.stoliar.cardManagement.controller;

import com.stoliar.cardManagement.dto.CardDto;
import com.stoliar.cardManagement.exception.EntityNotFoundException;
import com.stoliar.cardManagement.exception.InvalidCardOperationException;
import com.stoliar.cardManagement.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardController {
    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create a new card (Admin only)")
    public ResponseEntity<?> createCard(@Valid @RequestBody CardDto cardDto) {
        try {
            return ResponseEntity.ok(cardService.createCard(cardDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (InvalidCardOperationException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @cardSecurityService.isCardOwner(authentication, #id)")
    @Operation(summary = "Get card by ID (Admin only)")
    public ResponseEntity<CardDto> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all cards (Admin only)")
    public ResponseEntity<Page<CardDto>> getAllCards(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(pageable));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get current user's cards")
    public ResponseEntity<Page<CardDto>> getUserCards(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(cardService.getCurrentUserCards(pageable));
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @cardSecurityService.isCardOwner(authentication, #id)")
    @Operation(summary = "Block a card (Admin only)")
    public ResponseEntity<CardDto> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.blockCard(id));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Activate a card (Admin only)")
    public ResponseEntity<CardDto> activateCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.activateCard(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete a card (Admin only)")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}