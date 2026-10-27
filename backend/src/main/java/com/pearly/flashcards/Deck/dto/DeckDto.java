package com.pearly.flashcards.Deck.dto;

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
public class DeckDto {
    private Long id;
    private String name;
}
