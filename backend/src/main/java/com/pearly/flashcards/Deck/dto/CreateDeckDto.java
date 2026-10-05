package com.pearly.flashcards.Deck.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateDeckDto {
    private String name;
    private List<CardDto> cards;
}
