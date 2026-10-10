package com.pearly.flashcards.Card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CardServiceImpTests {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardServiceImp cardServiceImp;

    @Test
    void shouldReturnCardWhenCardExists() {
        Long cardId = 1L;
        String question = "Mock Question";
        String answer = "Mock Answer";

        Card card = new Card(cardId, question, answer, null);
        CardDetailsDto cardDetailsDto = new CardDetailsDto(cardId, question, answer);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(modelMapper.map(card, CardDetailsDto.class)).thenReturn(cardDetailsDto);

        CardDetailsDto result = cardServiceImp.findById(cardId);

        assertNotNull(result);
        assertEquals(result.getId(), card.getId());
        assertEquals(result.getAnswer(), card.getAnswer());
        assertEquals(result.getQuestion(), card.getQuestion());

        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenCardDoesNotExist() {
        Long cardId = 1L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.findById(cardId));

        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldReturnAllCards() {
        Long cardId = 1L;
        String question = "Mock Question";
        String answer = "Mock Answer";

        List<Card> cards = new ArrayList<>();
        List<CardDto> cardDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.add(new Card(cardId + i, question + i, answer + i, null));
            cardDtos.add(new CardDto(cardId + i, question + i, answer + i));
        }

        when(cardRepository.findAll()).thenReturn(cards);
        for (int i = 0; i < 5; i++) {
            when(modelMapper.map(cards.get(i), CardDto.class)).thenReturn(cardDtos.get(i));
        }

        List<CardDto> result = cardServiceImp.findAll();

        assertNotNull(result);
        for (int i = 0; i < 5; i++) {
            assertEquals(cardId + i, result.get(i).getId());
            assertEquals(question + i, result.get(i).getQuestion());
            assertEquals(answer + i, result.get(i).getAnswer());
        }

        verify(cardRepository).findAll();
    }

    @Test
    void shouldCreateNewCard() {
        Long cardId = 1L;
        String question = "Mock Question";
        String answer = "Mock Answer";

        Card card = new Card(cardId, question, answer, null);
        CreateCardDto createCardDto = new CreateCardDto(question, answer);
        Card mappedCard = new Card(null, question, answer, null);
        CardDetailsDto cardDetailsDto = new CardDetailsDto(cardId, question, answer);

        when(modelMapper.map(createCardDto, Card.class)).thenReturn(mappedCard);
        when(cardRepository.save(mappedCard)).thenReturn(card);
        when(modelMapper.map(card, CardDetailsDto.class)).thenReturn(cardDetailsDto);

        CardDetailsDto result = cardServiceImp.create(createCardDto);

        assertNotNull(result);
        assertEquals(cardId, result.getId());
        assertEquals(question, result.getQuestion());
        assertEquals(answer, result.getAnswer());

        verify(cardRepository).save(mappedCard);
    }

    @Test
    void shouldThrowExceptionWhenCreateNewCardAndArgumentIsNull() {
        CreateCardDto createCardDto = null;

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.create(createCardDto));
    }

    @Test
    void shouldThrowExceptionWhenCreateNewCardAndSomeFieldIsBlank() {
        String question = "Mock Question";
        String answer = "";

        CreateCardDto createCardDto = new CreateCardDto(question, answer);

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.create(createCardDto));
    }

    @Test
    void shouldDeleteCardByIdWhenEntityExists() {
        Long cardId = 1L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(new Card()));

        cardServiceImp.deleteById(cardId);

        verify(cardRepository).deleteById(cardId);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdAndEntityDoesNotExist() {
        Long cardId = 1L;

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.deleteById(cardId));
    }

    @Test
    void shouldUpdateCardWhenCorrectDataProvided() {
        Long cardId = 1L;
        String question = "Mock Question";
        String answer = "Mock Answer";
        String updatedQuestion = "Updated Question";
        String updatedAnswer = "Updated Answer";

        // Original data
        Card card = new Card(cardId, question, answer, null);
        // Update data
        CreateCardDto createCardDto = new CreateCardDto(updatedQuestion, updatedAnswer);
        CardDto cardDto = new CardDto(cardId, updatedQuestion, updatedAnswer);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(modelMapper.map(card, CardDto.class)).thenReturn(cardDto);

        CardDto result = cardServiceImp.update(createCardDto, cardId);

        assertNotNull(result);
        assertEquals(cardId, result.getId());
        assertEquals(updatedQuestion, result.getQuestion());
        assertEquals(updatedAnswer, result.getAnswer());

        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndNoUpdateDataProvided() {
        Long cardId = 1L;

        assertThrows(IllegalArgumentException.class,
                () -> cardServiceImp.update(null, cardId));
    }

    @Test
    void shouldThrowExceptionWhenUpdateCardAndEntityDoesNotExist() {
        Long cardId = 1L;
        String updatedQuestion = "Updated Question";
        String updatedAnswer = "Updated Answer";

        CreateCardDto createCardDto = new CreateCardDto(updatedQuestion, updatedAnswer);

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> cardServiceImp.update(createCardDto, cardId));

        verify(cardRepository).findById(cardId);
    }

    @Test
    void shouldUpdateCardWhenNotAllFieldsInUpdateDataProvided() {
        Long cardId = 1L;
        String question = "Mock Question";
        String answer = "Mock Answer";
        String updatedQuestion = null;
        String updatedAnswer = "Updated Answer";

        // Original data
        Card card = new Card(cardId, question, answer, null);
        // Update data
        CreateCardDto createCardDto = new CreateCardDto(updatedQuestion, updatedAnswer);
        CardDto cardDto = new CardDto(cardId, updatedQuestion, updatedAnswer);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(modelMapper.map(card, CardDto.class)).thenReturn(cardDto);

        CardDto result = cardServiceImp.update(createCardDto, cardId);

        assertNotNull(result);
        assertEquals(cardId, result.getId());
        assertEquals(updatedQuestion, result.getQuestion());
        assertEquals(updatedAnswer, result.getAnswer());

        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }
}
