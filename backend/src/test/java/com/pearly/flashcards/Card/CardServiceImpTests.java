package com.pearly.flashcards.Card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;
import com.pearly.flashcards.Deck.Deck;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class CardServiceImpTests {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardServiceImp cardServiceImp;

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Test
    void shouldReturnCardDetailsDtoWhenCreateNewCardAndCorrectDataProvided() {
        log.info("Should return CardDetailsDto when create new card and correct data provided.");

        Long id = 1L;
        String question = "Question";
        String answer = "Answer";
        Deck deck = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        // Correct data provided to service method
        CreateCardDto createCardDto = CreateCardDto.builder()
                .question(question)
                .answer(answer)
                .build();
        // Entity mapped from argument
        Card mappedCard = Card.builder()
                .question(question)
                .answer(answer)
                .build();
        // Entity after JPA save with auto added id and createdAt date
        Card persistedCard = Card.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .deck(deck)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        // Expected result
        CardDetailsDto cardDetailsDto = CardDetailsDto.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(modelMapper.map(createCardDto, Card.class)).thenReturn(mappedCard);
        when(cardRepository.save(mappedCard)).thenReturn(persistedCard);
        when(modelMapper.map(persistedCard, CardDetailsDto.class)).thenReturn(cardDetailsDto);
        CardDetailsDto result = cardServiceImp.create(createCardDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(question, result.getQuestion());
        assertEquals(answer, result.getAnswer());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(cardRepository).save(mappedCard);
    }

    @Test
    void shouldThrowExcpetionWhenCreateNewCardAndNullArgumentProvided() {
        log.info("Should throw exception when create new card and null argument provided.");

        CreateCardDto createCardDto = null;

        assertThrows(NullPointerException.class,
                () -> cardServiceImp.create(createCardDto));
    }

    @Test
    void shouldThrowExceptionWhenCreateNewCardAndInvalidDataProvided() {
        log.info("Should throw exception when create new card and invalid data provided.");

        String question = "1"; // Too short
        String answer = ""; // Must not be blank

        // Invalid data provided to service method
        CreateCardDto createCardDto = CreateCardDto.builder()
                .question(question)
                .answer(answer)
                .build();

        Set<ConstraintViolation<CreateCardDto>> violations = validator.validate(createCardDto);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }

    @Test
    void shouldReturnListOfCardDtoWhenFindAll() {
        log.info("Should return List<CardDto> when find all.");

        Long id = 1L;
        String question = "Question";
        String answer = "Answer";
        Deck deck = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        // Cards in JPA
        List<Card> cards = new ArrayList<>();
        // Mapped CardDtos
        List<CardDto> cardDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.add(Card.builder()
                    .id(id + 1)
                    .question(question)
                    .answer(answer)
                    .deck(deck)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build());
            cardDtos.add(CardDto.builder()
                    .id(id + 1)
                    .question(question)
                    .answer(answer)
                    .build());
        }

        when(cardRepository.findAll()).thenReturn(cards);
        for (int i = 0; i < 5; i++) {
            when(modelMapper.map(cards.get(i), CardDto.class)).thenReturn(cardDtos.get(i));
        }

        List<CardDto> result = cardServiceImp.findAll();

        assertNotNull(result);
        for (int i = 0; i < 5; i++) {
            assertEquals(id + 1, result.get(i).getId());
            assertEquals(question, result.get(i).getQuestion());
            assertEquals(answer, result.get(i).getAnswer());
        }

        verify(cardRepository).findAll();
    }

    @Test
    void shouldReturnCardDetailsDtoWhenFindByIdAndValidIdProvided() {
        log.info("Should return CardDetailsDto when find by id and valid id provided.");

        Long id = 1L;
        String question = "Question";
        String answer = "Answer";
        Deck deck = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        // Card in JPA
        Card card = Card.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .deck(deck)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        // Mapped CardDetailsDto
        CardDetailsDto cardDetailsDto = CardDetailsDto.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(modelMapper.map(card, CardDetailsDto.class)).thenReturn(cardDetailsDto);

        CardDetailsDto result = cardServiceImp.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(question, result.getQuestion());
        assertEquals(answer, result.getAnswer());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(cardRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndNullIdProvided() {
        log.info("Should throw exception when find by id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
                () -> cardServiceImp.findById(id));
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndIllegalIdProvided() {
        log.info("Should throw exception when find by id and illegal id provided.");

        Long id1 = -1L; // negative
        Long id2 = 0L; // not positive

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.findById(id1));
        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.findById(id2));
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndEntityNotFound() {
        log.info("Should throw exception when find by id and entity not found.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.findById(id));

        verify(cardRepository).findById(id);
    }

    @Test
    void shouldDeleteCardWhenDeleteCardByIdAndValidIdProvided() {
        log.info("Should delete card when delte card by id and valid id provided.");

        Long id = 1L;

        when(cardRepository.findById(id)).thenReturn(Optional.of(new Card()));

        cardServiceImp.deleteById(id);

        verify(cardRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdAndNullIdProvided() {
        log.info("Should throw exception when delete by id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
                () -> cardServiceImp.deleteById(id));
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdAndIllegalIdProvided() {
        log.info("Should throw exception when delete by id and illegal id provided.");

        Long id1 = -1L; // negative
        Long id2 = 0L; // not positive

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.deleteById(id1));
        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.deleteById(id2));
    }

    @Test
    void shouldThrowExcpetionWhenDeleteByIdAndEntityNotFound() {
        log.info("Should throw exception when delete by id and entity not found.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.deleteById(id));
    }

    @Test
    void shouldReturnUpdatedCardDtoWhenUpdateAndValidDataProvided() {
        log.info("Should return updated CardDto when update and valid data provided.");

        Long id = 1L;
        String question = "Question";
        String answer = "Answer";
        String newQuestion = "New Question";
        String newAnswer = "New Answer";
        Deck deck = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        // Card in JPA - original data
        Card card = Card.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .deck(deck)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        // Mapped CreateCardDto - update data
        CreateCardDto createCardDto = CreateCardDto.builder()
                .question(newQuestion)
                .answer(newAnswer)
                .build();
        // Updated CardDto - expected result after update
        CardDto updated = CardDto.builder()
                .id(id)
                .question(newQuestion)
                .answer(newAnswer)
                .build();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(modelMapper.map(card, CardDto.class)).thenReturn(updated);

        CardDto result = cardServiceImp.update(createCardDto, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(newQuestion, result.getQuestion());
        assertEquals(newAnswer, result.getAnswer());

        verify(cardRepository).findById(id);
        verify(cardRepository).save(card);
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndNullUpdateDataProvided() {
        log.info("Should throw exception when update card and null update data provided.");

        Long id = 1L;
        CreateCardDto update = null;

        assertThrows(NullPointerException.class,
                () -> cardServiceImp.update(update, id));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndNullIdProvided() {
        log.info("Should throw exception when update card and null id provided.");

        Long id = null;
        CreateCardDto update = new CreateCardDto(); // empty body does not matter in this test

        assertThrows(NullPointerException.class,
                () -> cardServiceImp.update(update, id));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndIllegalIdProvided() {
        log.info("Should throw exception when update card and illegal id provided.");

        Long id1 = -1L; // negative
        Long id2 = 0L; // not positive
        CreateCardDto update = new CreateCardDto(); // empty body does not matter in this test

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.update(update, id1));
        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.update(update, id2));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndEntityNotFound() {
        log.info("Should throw exception when update and entity not found.");

        Long id = 1L;
        String newQuestion = "New Question";
        String newAnswer = "New Answer";

        // Mapped CreateCardDto - update data
        CreateCardDto createCardDto = CreateCardDto.builder()
                .question(newQuestion)
                .answer(newAnswer)
                .build();

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.update(createCardDto, id));

        verify(cardRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndNotAllFieldsProvidedInUpdateBody() {
        log.info("Should throw exception when update card and not all fields provided in update body");

        String newQuestion = null; // missing field
        String newAnswer = "New Answer"; // new answer

        // Update data with missing fields
        CreateCardDto createCardDto = CreateCardDto.builder()
                .question(newQuestion)
                .answer(newAnswer)
                .build();

        Set<ConstraintViolation<CreateCardDto>> violations = validator.validate(createCardDto);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }
}
