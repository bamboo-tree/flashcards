package com.pearly.flashcards.Deck;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.pearly.flashcards.Card.Card;
import com.pearly.flashcards.Card.CardRepository;
import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Validated
@Slf4j
public class DeckServiceImp implements DeckService {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    public DeckServiceImp(
            DeckRepository deckRepository,
            CardRepository cardRepository,
            ModelMapper modelMapper) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new Deck and save it in the JPA repository.
     *
     * @param createDeckDto DTO body of the new Deck.
     * @return Detailed DTO of newly created Deck entity.
     */
    @Override
    public DeckDetailsDto create(@Valid CreateDeckDto createDeckDto) {
        log.info("Create new Deck.");

        Objects.requireNonNull(createDeckDto, "Invalid argument: createDeckDto must not be null.");

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
        log.info("Find all Decks.");

        List<DeckDto> dto = this.deckRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, DeckDto.class))
                .collect(Collectors.toList());

        return dto;
    }

    /**
     * Find Deck by id in the JPA repository.
     *
     * @param id Id of the Deck.
     * @return Detailed DTO of found Deck entity.
     */
    @Override
    public DeckDetailsDto findById(long id) {
        log.info("Find Deck id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");
        if(id <= 0 || id > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: id must be positive value.");
        }

        Deck deck = this.deckRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));
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
        log.info("Delete Deck id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");
        if(id <= 0 || id > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: id must be positive value.");
        }

        this.deckRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        this.deckRepository.deleteById(id);
    }

    /**
     * Update Deck body with provided id in the JPA repository.
     *
     * @param updateDeck DTO with updated body. Only present data will be replaced.
     * @param id         Id of the Deck.
     * @return DTO of updated Deck entity.
     */
    @Override
    public DeckDto update(@Valid CreateDeckDto update, long id) {
        log.info("Update Deck id={}.", id);

        Objects.requireNonNull(update, "Invalid argument: update must not be null.");
        Objects.requireNonNull(id, "Invalid argument: id must not be null.");
        if(id <= 0 || id > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: id must be positive value.");
        }

        Deck deck = this.deckRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        deck.setName(update.getName());

        deck = this.deckRepository.save(deck); // Synchronize data in JPA
        DeckDto dto = this.modelMapper.map(deck, DeckDto.class);

        return dto;
    }

    /**
     * Add Card to Deck.
     *
     * @param deckId Id of the Deck.
     * @param cardId Id of the Card.
     * @return Detailed DTO of updated Deck entity.
     */
    public DeckDetailsDto addCardById(long deckId, long cardId) {
        log.info("Add Card id={} to Deck id={}.", cardId, deckId);

        Objects.requireNonNull(deckId, "Invalid argument: deckId must not be null.");
        if(deckId <= 0 || deckId > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: deckId must be positive value.");
        }
        Objects.requireNonNull(cardId, "Invalid argument: cardId must not be null.");
        if(cardId <= 0 || cardId > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: cardId must be positive value.");
        }

        Deck deck = this.deckRepository
                .findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + deckId));
        Card card = this.cardRepository
                .findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + cardId));

        deck.addCard(card);
        card.addDeck(deck);

        deck = this.deckRepository.save(deck); // Synchronize data in JPA
        DeckDetailsDto dto = this.modelMapper.map(deck, DeckDetailsDto.class);

        return dto;
    }

    /**
     * Delete Card from Deck.
     *
     * @param deckId Id of the Deck.
     * @param cardId Id of the Card.
     * @return Detailed DTO of updated Deck entity.
     */
    public DeckDetailsDto deleteCardById(long deckId, long cardId) {
        log.info("Delete Card id={} from Deck id={}.", cardId, deckId);

        Objects.requireNonNull(deckId, "Invalid argument: deckId must not be null.");
        if(deckId <= 0 || deckId > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: deckId must be positive value.");
        }
        Objects.requireNonNull(cardId, "Invalid argument: cardId must not be null.");
        if(cardId <= 0 || cardId > Long.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid argument: cardId must be positive value.");
        }

        Deck deck = this.deckRepository
                .findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + deckId));
        Card card = this.cardRepository
                .findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + cardId));

        deck.deleteCard(card);
        card.deleteDeck();

        deck = this.deckRepository.save(deck); // Synchronize data in JPA
        DeckDetailsDto dto = this.modelMapper.map(deck, DeckDetailsDto.class);

        return dto;
    }
}
