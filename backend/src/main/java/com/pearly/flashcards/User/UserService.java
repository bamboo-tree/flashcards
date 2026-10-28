package com.pearly.flashcards.User;

import java.util.List;

import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

import jakarta.validation.Valid;

public interface UserService {
    public UserDetailsDto create(@Valid CreateUserDto createUserDto);

    public List<UserDto> findAll();

    public UserDetailsDto findById(long id);

    public void deleteById(long id);

    public UserDto update(@Valid CreateUserDto update, long id);

    public UserDetailsDto addDeckById(long userId, long deckId);

    public UserDetailsDto deleteDeckById(long userId, long deckId);
}
