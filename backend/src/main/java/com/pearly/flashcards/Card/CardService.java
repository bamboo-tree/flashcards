package com.pearly.flashcards.Card;

import java.util.List;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;

import jakarta.validation.Valid;

public interface CardService {
    public CardDetailsDto create(@Valid CreateCardDto createCardDto);

    public List<CardDto> findAll();

    public CardDetailsDto findById(long id);

    public void deleteById(long id);

    public CardDto update(@Valid CreateCardDto update, long id);
}
