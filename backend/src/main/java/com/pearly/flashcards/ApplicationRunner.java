package com.pearly.flashcards;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pearly.flashcards.Deck.DeckServiceImp;
import com.pearly.flashcards.Deck.dto.CreateDeckDto;
import com.pearly.flashcards.Deck.dto.DeckDetailsDto;
import com.pearly.flashcards.Deck.dto.DeckDto;
import com.pearly.flashcards.User.UserServiceImp;
import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

@Component
public class ApplicationRunner implements CommandLineRunner {
    private final UserServiceImp userServiceImp;
    private final DeckServiceImp deckServiceImp;

    public ApplicationRunner(
            UserServiceImp userServiceImp,
            DeckServiceImp deckServiceImp) {
        this.userServiceImp = userServiceImp;
        this.deckServiceImp = deckServiceImp;
    }

    @Override
    public void run(String... args) {

        long deleteId = 1L;
        long updateId = 4L;
        long findId = 2L;

        System.out.println("++++++++++++++++++++ DeckServiceImp ++++++++++++++++++++");

        System.out.println("==================== TEST: findById() ====================");
        List<CreateDeckDto> newDecks = new ArrayList<>();
        newDecks.add(new CreateDeckDto("Apple"));
        newDecks.add(new CreateDeckDto("Banana"));
        newDecks.add(new CreateDeckDto("Cherry"));
        newDecks.add(new CreateDeckDto("Dragon Fruit"));
        newDecks.forEach(e -> System.out.println(e.toString()));

        List<DeckDetailsDto> createdDecks = new ArrayList<>();
        newDecks.forEach(e -> {
            DeckDetailsDto created = this.deckServiceImp.create(e);
            createdDecks.add(created);
        });
        createdDecks.forEach(e -> System.out.println(e.toString()));

        System.out.println("==================== TEST: deleteById() ====================");
        this.deckServiceImp.deleteById(deleteId);
        System.out.println("Deleting Deck with id=" + deleteId);

        System.out.println("==================== TEST: update() ====================");
        CreateDeckDto newDeckData = new CreateDeckDto("Watermelon");
        DeckDto updatedDeck = this.deckServiceImp.update(newDeckData, updateId);
        System.out.println(updatedDeck.toString());

        System.out.println("==================== TEST: findAll() ====================");
        List<DeckDto> findAllDeckResult = this.deckServiceImp.findAll();
        findAllDeckResult.forEach(e -> System.out.println(e.toString()));

        System.out.println("==================== TEST: findById() ====================");
        DeckDetailsDto deckById = this.deckServiceImp.findById(findId);
        System.out.println(deckById.toString());

        System.out.println("++++++++++++++++++++ UserServiceImp ++++++++++++++++++++");

        System.out.println("==================== TEST: create() ====================");
        List<CreateUserDto> newUsers = new ArrayList<>();
        newUsers.add(new CreateUserDto("John_Pork"));
        newUsers.add(new CreateUserDto("Adam_Adams"));
        newUsers.add(new CreateUserDto("Tim_Cheese"));
        newUsers.add(new CreateUserDto("Jonathan_Porks"));
        newUsers.forEach(e -> System.out.println(e.toString()));

        List<UserDetailsDto> createdUsers = new ArrayList<>();
        newUsers.forEach(e -> {
            UserDetailsDto created = this.userServiceImp.create(e);
            createdUsers.add(created);
        });
        createdUsers.forEach(e -> System.out.println(e.toString()));

        System.out.println("==================== TEST: deleteById() ====================");
        this.userServiceImp.deleteById(deleteId);
        System.out.println("Deleting User with id=" + deleteId);

        System.out.println("==================== TEST: update() ====================");
        CreateUserDto newUserData = new CreateUserDto("Bon_Bork");
        UserDto updatedUser = this.userServiceImp.update(newUserData, updateId);
        System.out.println(updatedUser.toString());

        System.out.println("==================== TEST: findAll() ====================");
        List<UserDto> findAllUserResult = this.userServiceImp.findAll();
        findAllUserResult.forEach(e -> System.out.println(e.toString()));

        System.out.println("==================== TEST: findById() ====================");
        UserDetailsDto userById = this.userServiceImp.findById(findId);
        System.out.println(userById.toString());

        System.out.println("==================== TEST: addDeckById() ====================");
        long userId = 2, deckId = 2;
        UserDetailsDto userWithDeck = this.userServiceImp.addDeckById(userId, deckId);
        userWithDeck = this.userServiceImp.addDeckById(userId, deckId + 1);
        System.out.println(userWithDeck.toString());

        System.out.println("==================== TEST: addDeckById() ====================");
        UserDetailsDto userWithRemovedDeck = this.userServiceImp.deleteDeckById(userId, deckId);
        System.out.println(userWithRemovedDeck.toString());

    }
}
