package com.pearly.flashcards.User;

import java.util.List;

import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

public interface UserService {
    public UserDetailsDto create(CreateUserDto createUserDto);

    public List<UserDto> findAll();

    public UserDetailsDto findById(long id);

    public void deleteById(long id);

    public UserDto update(CreateUserDto update, long id);

    public UserDetailsDto addDeckById(long userId, long deckId);

    public UserDetailsDto deleteDeckById(long userId, long deckId);
}
