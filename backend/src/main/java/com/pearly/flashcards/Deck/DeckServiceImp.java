package com.pearly.flashcards.Deck;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;

@Service
public class DeckServiceImp implements DeckService {

    private final DeckRepository deckRepository;
    private final ModelMapper modelMapper;

    public DeckServiceImp(
            DeckRepository deckRepository,
            ModelMapper modelMapper) {
        this.deckRepository = deckRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new Deck and save it in the JPA repository.
     *
     * @param createDeckDto DTO body of the new Deck.
     * @return Detailed DTO of newly created Deck entity.
     */
    @Override
    public DeckDetailsDto create(CreateDeckDto createDeckDto) {
        if (createDeckDto == null) {
            throw new IllegalArgumentException("Provided createDeckDto is invalid - DTO is null");
        }
        if (createDeckDto.getName().isBlank()) {
            throw new IllegalArgumentException("Provided createDeckDto is invalid - name is blank");
        }

        Deck deck = this.deckRepository.save(this.modelMapper.map(createDeckDto, Deck.class));
        DeckDetailsDto dto = this.modelMapper.map(deck, DeckDetailsDto.class);

        return dto;
    }

    /**
     * Find all Decks in the JPA repository.
     *
     * @return List of Deck DTO.
     */
    @Override
    public List<DeckDto> findAll() {
        List<DeckDto> dto = this.deckRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, DeckDto.class))
                .toList();

        return dto;
    }

    /**
     * Find Deck by id in the JPA repository.
     *
     * @param id Id of the Deck.
     * @return Detailed DTO of found Deck entity.
     */
    @Transactional(readOnly = true)
    @Override
    public DeckDetailsDto findById(long id) {
        Deck deck = this.deckRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));
        DeckDetailsDto dto = this.modelMapper.map(deck, DeckDetailsDto.class);

        return dto;
    }

    /**
     * Delete Deck by id from the JPA repository.
     *
     * @param id Id of the Deck.
     */
    @Override
    public void deleteById(long id) {
        Deck deck = this.deckRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));

        this.deckRepository.delete(deck);
    }

    /**
     * Update Deck body with provided id in the JPA repository.
     *
     * @param updateDeck DTO with updated body. Only present data will be replaced.
     * @param id         Id of the Deck.
     * @return DTO of updated Deck entity.
     */
    @Transactional
    @Override
    public DeckDto update(CreateDeckDto updateDeck, long id) {
        if (updateDeck == null) {
            throw new IllegalArgumentException("Provided updateDeck is invalid - DTO is null");
        }
        Deck deck = this.deckRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));

        if (!updateDeck.getName().isBlank()) {
            deck.setName(updateDeck.getName());
        }

        Deck updatedDeck = this.deckRepository.save(deck);
        DeckDto dto = this.modelMapper.map(updatedDeck, DeckDto.class);

        return dto;
    }

}
