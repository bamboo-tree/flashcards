package com.pearly.flashcards.Card;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pearly.flashcards.Card.dto.CardDetailsDto;
import com.pearly.flashcards.Card.dto.CardDto;
import com.pearly.flashcards.Card.dto.CreateCardDto;


@RestController
@RequestMapping("/api/cards")
@Transactional
@Slf4j
@Validated
public class CardController {

    private final CardServiceImp cardServiceImp;

    public CardController(
        CardServiceImp cardServiceImp
    ) {
        this.cardServiceImp = cardServiceImp;
    }

    @PostMapping
    public ResponseEntity<CardDetailsDto> create(@RequestBody @Valid CreateCardDto createCardDto) {
        log.info("POST: /api/cards - create.");
        CardDetailsDto response = this.cardServiceImp.create(createCardDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> findAll() {
        log.info("GET: /api/cards - find all.");
        List<CardDto> response = this.cardServiceImp.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDetailsDto> findById(@PathVariable long id) {
        log.info("GET: /api/cards/id - find by id.");
        CardDetailsDto response = this.cardServiceImp.findById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        log.info("DELETE: /api/cards/id - delete by id.");
        this.cardServiceImp.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> update(@RequestBody @Valid CreateCardDto update, @PathVariable long id) {
        log.info("PUT: /api/cards/id - delete by id.");
        CardDto response = this.cardServiceImp.update(update, id);
        return ResponseEntity.ok(response);
    }
}
