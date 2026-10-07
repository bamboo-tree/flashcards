package com.pearly.flashcards.Deck.dto;

import java.util.List;

import com.pearly.flashcards.Card.dto.CardDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class CreateDeckDto {
    private String name;
    private List<CardDto> cards;
}
