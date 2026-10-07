package com.pearly.flashcards.Deck;

import java.util.List;

import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;

public interface DeckService {
    public DeckDetailsDto create(CreateDeckDto createDeckDto);

    public List<DeckDto> findAll();

    public DeckDetailsDto findById(long id);

    public void deleteById(long id);

    public DeckDto update(CreateDeckDto updateDeck, long id);

}
