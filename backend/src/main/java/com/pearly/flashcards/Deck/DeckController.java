package com.pearly.flashcards.Deck;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/decks")
@Slf4j
@Validated
public class DeckController {
    private final DeckServiceImp deckServiceImp;

    public DeckController(
        DeckServiceImp deckServiceImp
    ) {
        this.deckServiceImp = deckServiceImp;
    }

    @PostMapping
    public ResponseEntity<DeckDetailsDto> create(@RequestBody @Valid CreateDeckDto createDeckDto) {
        log.info("POST: /api/decks - create.");
        log.info(createDeckDto.toString());
        DeckDetailsDto response = this.deckServiceImp.create(createDeckDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeckDto>> findAll() {
        log.info("GET: /api/decks - find all.");
        List<DeckDto> response = this.deckServiceImp.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckDetailsDto> findById(@PathVariable long id) {
        log.info("GET: /api/decks/id - find by id.");
        DeckDetailsDto response = this.deckServiceImp.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        log.info("DELETE: /api/decks/id - delete by id.");
        this.deckServiceImp.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckDto> update(
        @RequestBody @Valid CreateDeckDto update,
        @PathVariable long id
    ) {
        log.info("PUT: /api/decks/id - update.");
        DeckDto response = this.deckServiceImp.update(update, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{deckId}/{cardId}")
    public ResponseEntity<DeckDetailsDto> addCardById(
        @PathVariable long deckId,
        @PathVariable long cardId
    ) {
        log.info("PUT: /api/decks/deckId/cardId - add card by id.");
        DeckDetailsDto response = this.deckServiceImp.addCardById(deckId, cardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{deckId}/{cardId}")
    public ResponseEntity<DeckDetailsDto> deleteCardById(
        @PathVariable long deckId,
        @PathVariable long cardId
    ) {
        log.info("DELETE: /api/decks/deckId/cardId - delete card by id.");
        DeckDetailsDto response = this.deckServiceImp.deleteCardById(deckId, cardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
