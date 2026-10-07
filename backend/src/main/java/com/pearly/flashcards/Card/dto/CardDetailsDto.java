package com.pearly.flashcards.Card.dto;

import com.pearly.flashcards.Deck.dto.DeckDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CardDetailsDto {
    private Long id;
    private String question;
    private String answer;
    private DeckDto deck;
}
