package com.pearly.flashcards;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pearly.flashcards.User.UserServiceImp;
import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

@Component
public class ApplicationRunner implements CommandLineRunner {
    private final UserServiceImp userServiceImp;

    public ApplicationRunner(
            UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @Override
    public void run(String... args) {
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
        long deleteId = 1L;
        this.userServiceImp.deleteById(deleteId);
        System.out.println("Deleting User with id=" + deleteId);

        System.out.println("==================== TEST: update() ====================");
        CreateUserDto newUserData = new CreateUserDto("Bon_Bork");
        long updateId = 4L;
        UserDto updatedUser = this.userServiceImp.update(newUserData, updateId);
        System.out.println(updatedUser.toString());

        System.out.println("==================== TEST: findAll() ====================");
        List<UserDto> findAllResult = this.userServiceImp.findAll();
        findAllResult.forEach(e -> System.out.println(e.toString()));

        System.out.println("==================== TEST: findById() ====================");
        long findId = 2L;
        UserDetailsDto byId = this.userServiceImp.findById(findId);
        System.out.println(byId.toString());

    }
}
