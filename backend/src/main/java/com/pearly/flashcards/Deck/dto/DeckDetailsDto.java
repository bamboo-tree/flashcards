package com.pearly.flashcards.Deck.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pearly.flashcards.Card.dto.CardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  WARN: If you create or update entity use CreateDeckDto instead.
 *        This DTO is not validated and can be dangerous.
 */

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class DeckDetailsDto {
    private Long id;
    private String name;
    private List<CardDto> cards;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
