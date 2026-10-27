package com.pearly.flashcards.Deck;

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

import com.pearly.flashcards.Card.Card;
import com.pearly.flashcards.Card.CardRepository;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;
import com.pearly.flashcards.User.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class DeckServiceImpTests {
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DeckServiceImp deckServiceImp;

    private final Validator validator = Validation
        .buildDefaultValidatorFactory()
        .getValidator();

    @Test
    void shouldReturnDeckDetailsDtoWhenCreateNewDeckAndValidDataProvided() {
        log.info("Should return DeckDetailsDto when create new Deck and valid data provided.");

        Long id = 1L;
        String name = "Deck name";
        List<CardDto> cardsDtos = null;
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        CreateDeckDto createDeckDto = CreateDeckDto.builder()
            .name(name)
            .build();
        Deck mappedDeck = Deck.builder()
            .name(name)
            .build();
        Deck persistedDeck = Deck.builder()
            .id(id)
            .name(name)
            .owner(owner)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        DeckDetailsDto result = DeckDetailsDto.builder()
            .id(id)
            .name(name)
            .cards(cardsDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(modelMapper.map(createDeckDto, Deck.class)).thenReturn(mappedDeck);
        when(deckRepository.save(mappedDeck)).thenReturn(persistedDeck);
        when(modelMapper.map(persistedDeck, DeckDetailsDto.class)).thenReturn(result);

        DeckDetailsDto res = deckServiceImp.create(createDeckDto);

        assertNotNull(res);
        assertEquals(id, res.getId());
        assertEquals(name, res.getName());
        assertEquals(cardsDtos, res.getCards());
        assertEquals(createdAt, res.getCreatedAt());
        assertEquals(updatedAt, res.getUpdatedAt());

        verify(deckRepository).save(mappedDeck);
    }

    @Test
    void shouldThrowExceptionWhenCreateNewDeckAndNullArgumentProvided() {
        log.info("Should throw exception when create new card and null argument provided.");

        CreateDeckDto createDeckDto = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.create(createDeckDto));
    }

    @Test
    void shouldThrowExceptionWhenCreateNewDeckAndInvalidDataProvided() {
        log.info("Should throw exception when create new deck and invalid data provided.");

        String name = ""; // Must not be blank

        CreateDeckDto createDeckDto = CreateDeckDto.builder()
            .name(name)
            .build();

        Set<ConstraintViolation<CreateDeckDto>> violations = validator.validate(createDeckDto);

        assertFalse(violations.isEmpty());
            violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }

    @Test
    void shouldReturnListOfDeckDtoWhenFindAll() {
        log.info("Should return List<DeckDto> when find all.");

        Long id = 1L;
        String name = "Deck name";
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        List<Deck> decks = new ArrayList<>();
        List<DeckDto> deckDtos = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            decks.add(Deck.builder()
                .id(id+1)
                .name(name)
                .owner(owner)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build());
            deckDtos.add(DeckDto.builder()
                .id(id+1)
                .name(name)
                .build());
        }

        when(deckRepository.findAll()).thenReturn(decks);
        for(int i = 0; i < 5; i++) {
            when(modelMapper.map(decks.get(i), DeckDto.class)).thenReturn(deckDtos.get(i));
        }

        List<DeckDto> result = deckServiceImp.findAll();

        assertNotNull(result);
        for(int i = 0; i < 5; i++) {
            assertEquals(id+1, result.get(i).getId());
            assertEquals(name, result.get(i).getName());
        }

        verify(deckRepository).findAll();
    }

    @Test
    void shouldReturnDeckDetailsDtoWhenFindByIdAndValidIdProvided() {
        log.info("Should return DeckDetailsDto when find by id and valid id provided.");

        Long id = 1L;
        String name = "Deck name";
        List<CardDto> cardDtos = null;
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        Deck deck = Deck.builder()
            .id(id)
            .name(name)
            .owner(owner)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        DeckDetailsDto deckDetailsDto = DeckDetailsDto.builder()
            .id(id)
            .name(name)
            .cards(cardDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(deckRepository.findById(id)).thenReturn(Optional.of(deck));
        when(modelMapper.map(deck, DeckDetailsDto.class)).thenReturn(deckDetailsDto);

        DeckDetailsDto result = deckServiceImp.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(cardDtos, result.getCards());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(deckRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndNullIdProvided() {
        log.info("Should throw exception when find by id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.findById(id));
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndInvalidIdProvided() {
        log.info("Should throw exception when find by id and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.findById(id1));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.findById(id2));
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAndEntityNotFound() {
        log.info("Should throw exception when find by id and entity not found exception.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.findById(id));

        verify(deckRepository).findById(id);
    }

    @Test
    void shouldDeleteDeckWhenDeleteDeckByIdAndValidIdProvided() {
        log.info("Should delete deck when delete deck by id and valid id provided.");

        Long id = 1L;

        when(deckRepository.findById(id)).thenReturn(Optional.of(new Deck()));

        deckServiceImp.deleteById(id);

        verify(deckRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndNullIdProvided() {
        log.info("Should throw excpetion when deleteby id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.deleteById(id));
    }

    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndInvalidIdProvided() {
        log.info("Should throw exception when delete by id and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteById(id1));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteById(id2));
    }

    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndEntityNotFound() {
        log.info("Should throw exception when delete by id and entity not found.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.deleteById(id));
    }

    @Test
    void shouldReturnUpdatedDeckWhenUpdateDeckAndValidDataProvided() {
        log.info("Should return updated DeckDto when update and valid data provided.");

        Long id = 1L;
        String name = "Deck name";
        String updatedName = "Updated name";
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        Deck original = Deck.builder()
            .id(id)
            .name(name)
            .owner(owner)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        CreateDeckDto updatedDeck = CreateDeckDto.builder()
            .name(updatedName)
            .build();

        DeckDto mappedUpdatedDeck = DeckDto.builder()
            .id(id)
            .name(name)
            .build();

        when(deckRepository.findById(id)).thenReturn(Optional.of(original));
        when(deckRepository.save(original)).thenReturn(original);
        when(modelMapper.map(original, DeckDto.class)).thenReturn(mappedUpdatedDeck);

        DeckDto result = deckServiceImp.update(updatedDeck, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(name, result.getName());

        verify(deckRepository).findById(id);
        verify(deckRepository).save(original);
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeckAndNullUpdateDataProvided() {
        log.info("Should throw exception when update deck and null update data provided.");

        Long id = 1L;
        CreateDeckDto update = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.update(update, id));
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeckAndNullIdProvided() {
        log.info("Should throw exception when update deck and null id provided.");

        Long id = null;
        CreateDeckDto update = new CreateDeckDto();

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.update(update, id));
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeckAndInvalidIdProvided() {
        log.info("Should throw exception when update deck and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;
        CreateDeckDto update = new CreateDeckDto();

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.update(update, id1));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.update(update, id2));
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeckAndEntityNotFound() {
        log.info("Should throw exception when update deck and entity not found exception.");

        Long id = 1L;
        CreateDeckDto update = new CreateDeckDto();

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.update(update, id));

        verify(deckRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenUpdateDeckAndNotAllFieldsProvidedInUpdateBody(){
        log.info("Should throw exception when update deck and not all fields provided in update body.");

        Long id = 1L;
        String name = null;

        CreateDeckDto update = CreateDeckDto.builder()
            .name(name)
            .build();

        Set<ConstraintViolation<CreateDeckDto>> violations = validator.validate(update);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }

    @Test
    void shouldReturnUpdatedDeckWhenAddCardByIdAndValidDataProvided() {
        log.info("Should return updated deck when add card by id and valid data provided.");

        Long cardId = 1L;
        String question = "Question";
        String answer = "Answer";
        Long deckId = 1L;
        String name = "Deck name";
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        Card card = Card.builder()
            .id(cardId)
            .question(question)
            .answer(answer)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        CardDto cardDto = CardDto.builder()
            .id(cardId)
            .question(question)
            .answer(answer)
            .build();


        Deck deck = Deck.builder()
            .id(deckId)
            .name(name)
            .owner(owner)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        List<CardDto> cardDtos = new ArrayList<>();
        cardDtos.add(cardDto);
        DeckDetailsDto deckDetailsDto = DeckDetailsDto.builder()
            .id(deckId)
            .name(name)
            .cards(cardDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(deckRepository.save(deck)).thenReturn(deck);
        when(modelMapper.map(deck, DeckDetailsDto.class)).thenReturn(deckDetailsDto);

        DeckDetailsDto result = deckServiceImp.addCardById(deckId, cardId);

        assertNotNull(result);
        assertEquals(deckId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(cardDtos.get(0), result.getCards().get(0));
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(deckRepository).findById(deckId);
        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenAddCardByIdAndNullCardIdProvided() {
        log.info("Should throw exception when add card by id and null card id provided.");

        Long cardId = null;
        Long deckId = 1L;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.addCardById(deckId, cardId));
    }

    @Test
    void shouldThrowExceptionWhenAddCardByIdAndNullDeckIdProvided() {
        log.info("Should throw exception when add card by id and null deck id provided.");

        Long cardId = 1L;
        Long deckId = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.addCardById(deckId, cardId));
    }

    @Test
    void shouldThrowExceptionWhenAddCardByIdAndInvalidCardIdProvided() {
        log.info("Should throw exception when add card by id and invalid card id provided.");

        Long cardId1 = -1L;
        Long cardId2 = 0L;
        Long deckId = 1L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.addCardById(deckId, cardId1));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.addCardById(deckId, cardId2));
    }

    @Test
    void shouldThrowExcecptionWhenAddCardByIdAndInvalidDeckIdProvided() {
        log.info("Should throw exception when add card by id and invalid deck id provided.");

        Long cardId = 1L;
        Long deckId1 = -1L;
        Long deckId2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.addCardById(deckId1, cardId));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.addCardById(deckId2, cardId));
    }

    @Test
    void shouldThrowExceptionWhenAddCardByIdAndCardNotFound() {
        log.info("Should throw exception when add card by id and card not found.");

        Long cardId = 1L;
        Long deckId = 1L;
        Deck deck = new Deck();

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.addCardById(deckId, cardId));

        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenAddCardByIdAndDeckNotFound() {
        log.info("Should throw exception when add card by id and deck not found.");

        Long cardId = 1L;
        Long deckId = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.addCardById(deckId, cardId));

        verify(deckRepository).findById(deckId);
    }

    @Test
    void shouldReturnUpdatedDeckWhenDeleteCardByIdAndValidDataProvided() {
        log.info("Should return updated deck when delete card by id and valid data provided.");

        Long cardId = 1L;
        String question = "Question";
        String answer = "Answer";
        Long deckId = 1L;
        String name = "Deck name";
        User owner = null;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        Card card = Card.builder()
            .id(cardId)
            .question(question)
            .answer(answer)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        Deck deck = Deck.builder()
            .id(deckId)
            .name(name)
            .owner(owner)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        deck.getCards().add(card);

        List<CardDto> cardDtos = new ArrayList<>();
        DeckDetailsDto deckDetailsDto = DeckDetailsDto.builder()
            .id(deckId)
            .name(name)
            .cards(cardDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(deckRepository.save(deck)).thenReturn(deck);
        when(modelMapper.map(deck, DeckDetailsDto.class)).thenReturn(deckDetailsDto);

        DeckDetailsDto result = deckServiceImp.deleteCardById(deckId, cardId);

        assertNotNull(result);
        assertEquals(true, result.getCards().isEmpty());
        assertEquals(deckId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(deckRepository).findById(deckId);
        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenDeleteCardByIdAndNullCardIdProvided() {
        log.info("Should throw exception when delete card by id and null card id provided.");

        Long cardId = null;
        Long deckId = 1L;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCardByIdAndNullDeckIdProvided() {
        log.info("Should throw exception when delete card by id and null deck id provided.");

        Long cardId = 1L;
        Long deckId = null;

        assertThrows(NullPointerException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCardByIdAndInvalidCardIdProvided() {
        log.info("Should throw exception when delete card by id and invalid card id provided.");

        Long cardId1 = -1L;
        Long cardId2 = 0L;
        Long deckId = 1L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId1));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId2));
    }

    @Test
    void shouldThrowExcecptionWhenDeleteCardByIdAndInvalidDeckIdProvided() {
        log.info("Should throw exception when delete card by id and invalid delete id provided.");

        Long cardId = 1L;
        Long deckId1 = 0L;
        Long deckId2 = -1L;

        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteCardById(deckId1, cardId));
        assertThrows(IllegalArgumentException.class,
            () -> deckServiceImp.deleteCardById(deckId2, cardId));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCardByIdAndCardNotFound() {
        log.info("Should throw exception when delete card by id and card not found.");

        Long cardId = 1L;
        Long deckId = 1L;
        Deck deck = new Deck();

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId));

        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenDeleteCardByIdAndDeckNotFound() {
        log.info("Should throw exception when delete card by id and deck not found.");

        Long cardId = 1L;
        Long deckId = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> deckServiceImp.deleteCardById(deckId, cardId));

        verify(deckRepository).findById(deckId);
    }
}
