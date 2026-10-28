package com.pearly.flashcards.Deck;

import java.util.List;

import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;

import jakarta.validation.Valid;

public interface DeckService {
    public DeckDetailsDto create(@Valid CreateDeckDto createDeckDto);

    public List<DeckDto> findAll();

    public DeckDetailsDto findById(long id);

    public void deleteById(long id);

    public DeckDto update(@Valid CreateDeckDto update, long id);

}
