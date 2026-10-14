package com.pearly.flashcards.Card;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CardServiceImp implements CardService {
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    public CardServiceImp(
            CardRepository cardRepository,
            ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new Card and save it in the JPA repository.
     *
     * @param createCardDto DTO body of the new Card.
     * @return Detailed DTO of newly created Card entity.
     */
    @Override
    public CardDetailsDto create(CreateCardDto createCardDto) {
        log.info("Create new Card.");

        Objects.requireNonNull(createCardDto, "Invalid argument: createCardDto must not be null");

        Card card = this.cardRepository.save(this.modelMapper.map(createCardDto, Card.class));
        CardDetailsDto dto = this.modelMapper.map(card, CardDetailsDto.class);

        return dto;
    }

    /**
     * Find all Cards in the JPA repository.
     *
     * @return List of Card DTO.
     */
    @Override
    public List<CardDto> findAll() {
        log.info("Find all Cards.");

        List<CardDto> dtos = this.cardRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, CardDto.class))
                .collect(Collectors.toList());

        return dtos;
    }

    /**
     * Find Card by id in the JPA repository.
     *
     * @param id Id of the Card.
     * @return Detailed DTO of found Card entity.
     */
    @Override
    public CardDetailsDto findById(long id) {
        log.info("Find Card id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        Card card = this.cardRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));
        CardDetailsDto dto = this.modelMapper.map(card, CardDetailsDto.class);

        return dto;
    }

    /**
     * Delete Card by id from the JPA repository.
     *
     * @param id Id of the Card.
     */
    @Override
    public void deleteById(long id) {
        log.info("Delete Card id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        Card card = this.cardRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        this.cardRepository.delete(card);
    }

    /**
     * Update Card body with provided id in the JPA repository.
     *
     * @param updateCard DTO with updated body. Only present data will be replaced.
     * @param id         Id of the Card.
     * @return DTO of updated Card entity.
     */
    @Override
    public CardDto update(CreateCardDto update, long id) {
        log.info("Update Card id={}.", id);

        Objects.requireNonNull(update, "Invalid argument: update must not be null.");
        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        Card card = this.cardRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        if (update.getQuestion() != null) {
            card.setQuestion(update.getQuestion());
        }
        if (update.getAnswer() != null) {
            card.setAnswer(update.getAnswer());
        }

        card = this.cardRepository.save(card); // Synchronize data in JPA
        CardDto dto = this.modelMapper.map(card, CardDto.class);

        return dto;
    }
}
