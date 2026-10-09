package com.pearly.flashcards.Card;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;

@Service
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
        if (createCardDto == null) {
            throw new IllegalArgumentException("Provided createCardDto is invalid - DTO is null");
        }
        if (createCardDto.getQuestion().isBlank()) {
            throw new IllegalArgumentException("Provided createCardDto is invalid - question is blank");
        }
        if (createCardDto.getAnswer().isBlank()) {
            throw new IllegalArgumentException("Provided createCardDto is invalid - answer is blank");
        }

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
        List<CardDto> dtos = this.cardRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, CardDto.class))
                .toList();

        return dtos;
    }

    /**
     * Find Card by id in the JPA repository.
     *
     * @param id Id of the Card.
     * @return Detailed DTO of found Card entity.
     */
    @Transactional(readOnly = true)
    @Override
    public CardDetailsDto findById(long id) {
        Card card = this.cardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));
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
        Card card = this.cardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));

        this.cardRepository.delete(card);
    }

    /**
     * Update Card body with provided id in the JPA repository.
     *
     * @param updateCard DTO with updated body. Only present data will be replaced.
     * @param id         Id of the Card.
     * @return DTO of updated Card entity.
     */
    @Transactional
    @Override
    public CardDto update(CreateCardDto updateCard, long id) {
        if (updateCard == null) {
            throw new IllegalArgumentException("Provided updateCard is invalid - DTO is null");
        }
        Card card = this.cardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Provided id is invalid - there is no entity with id=" + id));

        if (!updateCard.getQuestion().isBlank()) {
            card.setQuestion(updateCard.getQuestion());
        }
        if (!updateCard.getAnswer().isBlank()) {
            card.setAnswer(updateCard.getAnswer());
        }

        Card updatedCard = this.cardRepository.save(card);
        CardDto dto = this.modelMapper.map(updatedCard, CardDto.class);

        return dto;
    }
}
