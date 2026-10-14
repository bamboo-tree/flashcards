package com.pearly.flashcards.Card;

import java.util.List;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;

public interface CardService {
    public CardDetailsDto create(CreateCardDto createCardDto);

    public List<CardDto> findAll();

    public CardDetailsDto findById(long id);

    public void deleteById(long id);

    public CardDto update(CreateCardDto update, long id);
}
