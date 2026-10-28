package com.pearly.flashcards.User;

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

import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
public class UserController {
    private final UserServiceImp userServiceImp;

    public UserController(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }
    @PostMapping
    public ResponseEntity<UserDetailsDto> create(@RequestBody @Valid CreateUserDto createUserDto) {
        log.info("POST: /api/users - create.");
        UserDetailsDto response = this.userServiceImp.create(createUserDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("GET: /api/users - find all.");
        List<UserDto> response = this.userServiceImp.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDto> findById(@PathVariable long id) {
        log.info("GET: /api/users/id - find by id.");
        UserDetailsDto response = this.userServiceImp.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        log.info("DELETE: /api/users/id - delete by id.");
        this.userServiceImp.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
        @RequestBody @Valid CreateUserDto update,
        @PathVariable long id
    ) {
        log.info("PUT: /api/users/id - update.");
        UserDto response = this.userServiceImp.update(update, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{userId}/{deckId}")
    public ResponseEntity<UserDetailsDto> addCardById(
        @PathVariable long userId,
        @PathVariable long deckId
    ) {
        log.info("PUT: /api/users/userId/cardId - add card by id.");
        UserDetailsDto response = this.userServiceImp.addDeckById(userId, deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userId}/{deckId}")
    public ResponseEntity<UserDetailsDto> deleteCardById(
        @PathVariable long userId,
        @PathVariable long deckId
    ) {
        log.info("DELETE: /api/users/userId/cardId - delete card by id.");
        UserDetailsDto response = this.userServiceImp.deleteDeckById(userId, deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
