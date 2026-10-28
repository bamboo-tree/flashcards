package com.pearly.flashcards.User;

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

import com.pearly.flashcards.Deck.Deck;
import com.pearly.flashcards.Deck.DeckRepository;
import com.pearly.flashcards.Deck.dto.DeckDto;
import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceImpTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImp userServiceImp;

    private final Validator validator = Validation
        .buildDefaultValidatorFactory()
        .getValidator();

    @Test
    void shouldReturnUserDetailsDtoWhenCreateNewUserAndValidDataProvided() {
        log.info("Should return UserDetailsDto when create new user and valid data provided.");

        Long id = 1L;
        String userName = "UserName";
        List<DeckDto> decks = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        CreateUserDto createUserDto = CreateUserDto.builder()
            .userName(userName)
            .build();
        User mappedUser = User.builder()
            .userName(userName)
            .build();
        User persistedUser = User.builder()
            .id(id)
            .userName(userName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
            .id(id)
            .userName(userName)
            .decks(decks)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(modelMapper.map(createUserDto, User.class)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(persistedUser);
        when(modelMapper.map(persistedUser, UserDetailsDto.class)).thenReturn(userDetailsDto);

        UserDetailsDto result = userServiceImp.create(createUserDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(userName, result.getUserName());
        assertEquals(decks, result.getDecks());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(userRepository).save(mappedUser);
    }
    @Test
    void shouldThrowExceptionWhenCreateNewUserAndNullDataProvided() {
        log.info("Should throw exception when create new user and null data provided.");

        CreateUserDto createUserDto = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.create(createUserDto));
    }
    @Test
    void shouldThrowExceptionWhenCreateNewUserAndInvalidDataProvided() {
        log.info("Should throw exception when create new user and invalid data provided.");

        String invalidUserName = ",";

        CreateUserDto createUserDto = CreateUserDto.builder()
            .userName(invalidUserName)
            .build();

        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(createUserDto);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }

    @Test
    void shouldReturnListOfUsersWhenFindAll(){
        log.info("Should return List<UserDto> when find all users.");

        Long id = 1L;
        String userName = "UserName";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        List<User> users = new ArrayList<>();
        List<UserDto> userDtos = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            users.add(User.builder()
                .id(id)
                .userName(userName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build());
            userDtos.add(UserDto.builder()
                .id(id+1)
                .userName(userName)
                .build());
        }

        when(userRepository.findAll()).thenReturn(users);
        for(int i = 0; i < 5; i++) {
            when(modelMapper.map(users.get(i), UserDto.class)).thenReturn(userDtos.get(i));
        }

        List<UserDto> result = userServiceImp.findAll();

        assertNotNull(result);
        for(int i = 0; i < 5; i++) {
            assertEquals(id+1, result.get(i).getId());
            assertEquals(userName, result.get(i).getUserName());
        }

        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUserDetailsDtoWhenFindByIdAndValidIdProvided() {
        log.info("Should return UserDetailsDto when find by id and valid id provided.");

        Long id = 1L;
        String userName = "UserName";
        List<DeckDto> decks = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        User user = User.builder()
            .id(id)
            .userName(userName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
            .id(id)
            .userName(userName)
            .decks(decks)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDetailsDto.class)).thenReturn(userDetailsDto);

        UserDetailsDto result = userServiceImp.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(userName, result.getUserName());
        assertEquals(decks, result.getDecks());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(userRepository).findById(id);
    }
    @Test
    void shouldThrowExceptionWhenFindByIdAndNullIdProvided() {
        log.info("Should throw exception when find by id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.findById(id));
    }
    @Test
    void shouldThrowExceptionWhenFindByIdAndInvalidIdProvided() {
        log.info("Should throw exception when find by id and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.findById(id1));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.findById(id2));
    }
    @Test
    void shouldThrowExceptionWhenFindByIdAndEntityNotFound() {
        log.info("Should throw exception when find by id and entity not found.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.findById(id));

        verify(userRepository).findById(id);
    }

    @Test
    void shouldDeleteUserWhenDeleteByIdAndValidIdProvided() {
        log.info("Should delete user when delete by id and valid id provided.");

        Long id = 1L;
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userServiceImp.deleteById(id);

        verify(userRepository).deleteById(id);
    }
    @Test
    void shouldThrowExceptionWhenDeleteByIdAndNullIdProvided() {
        log.info("Should throw exception when delete by id and null id provided.");

        Long id = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.deleteById(id));
    }
    @Test
    void shouldThrowExceptionWhenDeleteByIdAndInvalidIdProvided() {
        log.info("Should throw exception when delete by id and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteById(id1));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteById(id2));
    }
    @Test
    void shouldThrowExceptionWhenDeleteByIdAndEntityNotFound() {
        log.info("Should throw exception when delete by id and entity not found.");

        Long id = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.deleteById(id));

        verify(userRepository).findById(id);
    }

    @Test
    void shouldReturnUpdatedUserDetailsDtoWhenUpdateUserAndValidaDataProvided() {
        log.info("Should return updated user when update and valid data provided.");

        Long id = 1L;
        String userName = "UserName";
        String newUserName = "NewUserName";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        User user = User.builder()
            .id(id)
            .userName(userName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        CreateUserDto update = CreateUserDto.builder()
            .userName(userName)
            .build();
        User updatedUser = user;
        updatedUser.setUserName(newUserName);
        UserDto userDto = UserDto.builder()
            .id(id)
            .userName(newUserName)
            .build();


        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserDto.class)).thenReturn(userDto);

        UserDto result = userServiceImp.update(update, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(newUserName, result.getUserName());

        verify(userRepository).findById(id);
        verify(userRepository).save(updatedUser);
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndNullIdProvided() {
        log.info("Should throw exception when update and null id provided.");

        Long id = null;
        CreateUserDto update = new CreateUserDto();

        assertThrows(NullPointerException.class,
            () -> userServiceImp.update(update, id));
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndNullDataProvided() {
        log.info("Should throw exception when update and null data provided.");

        Long id = 1L;
        CreateUserDto update = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.update(update, id));
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndInvalidIdProvided() {
        log.info("Should throw exception when update and invalid id provided.");

        Long id1 = -1L;
        Long id2 = 0L;
        CreateUserDto update = new CreateUserDto();

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.update(update, id1));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.update(update, id2));
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndInvalidDataProvided() {
        log.info("Should throw exception when update and invalid data provided.");

        String userName = ".";
        CreateUserDto update = CreateUserDto.builder()
            .userName(userName)
            .build();

        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(update);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndEntityNotFound() {
        log.info("Should throw exception when update and entity not found.");

        Long id = 1L;
        CreateUserDto update = new CreateUserDto();

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.update(update, id));

        verify(userRepository).findById(id);
    }
    @Test
    void shouldThrowExceptionWhenUpdateUserAndNotAllDataFieldsProvided(){
        log.info("Should throw exception when update and not all fields provided.");

        String userName = null;
        CreateUserDto update = CreateUserDto.builder()
            .userName(userName)
            .build();

        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(update);

        assertFalse(violations.isEmpty());
        violations.forEach(e -> System.out.println(e.getPropertyPath() + " : " + e.getMessage()));
    }

    @Test
    void shouldReturnUpdatedUserDetailsDtoWhenAddDeckByIdAndValidDataProvided(){
        log.info("Should return updated UserDetailsDto when add deck by id and valid data provided.");

        Long userId = 1L;
        String userName = "UserName";
        Long deckId = 1L;
        String name = "Deck name";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;


        Deck deck = Deck.builder()
            .id(deckId)
            .name(name)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        DeckDto deckDto = DeckDto.builder()
            .id(deckId)
            .name(name)
            .build();

        List<DeckDto> deckDtos = new ArrayList<>();
        deckDtos.add(deckDto);

        User user = User.builder()
            .id(userId)
            .userName(userName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
            .id(userId)
            .userName(userName)
            .decks(deckDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDetailsDto.class)).thenReturn(userDetailsDto);

        UserDetailsDto result = userServiceImp.addDeckById(userId, deckId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(userName, result.getUserName());
        assertEquals(deckDtos.get(0), result.getDecks().get(0));
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(userRepository).findById(userId);
        verify(deckRepository).findById(deckId);
        verify(userRepository).save(user);
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndNullDeckIdProvided(){
        log.info("Should throw exception when add deck by id and null deck id provided.");

        Long userId = 1L;
        Long deckId = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.addDeckById(userId, deckId));
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndNullUserIdProvided(){
        log.info("Should throw exception when add deck by id and null user id provided.");

        Long userId = null;
        Long deckId = 1L;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.addDeckById(userId, deckId));
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndInvalidDeckIdProvided(){
        log.info("Should throw exception when add deck by id and invalid deck id provided.");

        Long userId = 1L;
        Long deckId1 = -1L;
        Long deckId2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.addDeckById(userId, deckId1));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.addDeckById(userId, deckId2));
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndInvalidUserIdProvided(){
        log.info("Should throw exception when add deck by id and invalid user id provided.");

        Long userId1 = -1L;
        Long userId2 = 0L;
        Long deckId = 1L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.addDeckById(userId1, deckId));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.addDeckById(userId2, deckId));
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndDeckNotFound(){
        log.info("Should throw exception when add deck by id and deck not found.");

        Long userId = 1L;
        Long deckId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.addDeckById(userId, deckId));

        verify(deckRepository).findById(deckId);
    }
    @Test
    void shouldThrowExceptionWhenAddDeckByIdAndUserNotFound(){
        log.info("Should throw exception when add deck by id and user not found.");

        Long userId = 1L;
        Long deckId = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.addDeckById(userId, deckId));

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldReturnUpdatedUserDetailsDtoWhenDeleteDeckByIdAndValidDataProvided(){
        log.info("Should return updated UserDetailsDto when delete deck by id and valid data provided.");

        Long userId = 1L;
        String userName = "UserName";
        Long deckId = 1L;
        String name = "Deck name";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = null;

        Deck deck = Deck.builder()
            .id(deckId)
            .name(name)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        User user = User.builder()
            .id(userId)
            .userName(userName)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
        user.getDecks().add(deck);

        List<DeckDto> deckDtos = new ArrayList<>();
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
            .id(userId)
            .userName(userName)
            .decks(deckDtos)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDetailsDto.class)).thenReturn(userDetailsDto);

        UserDetailsDto result = userServiceImp.deleteDeckById(userId, deckId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(userName, result.getUserName());
        assertEquals(true, result.getDecks().isEmpty());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());

        verify(userRepository).findById(userId);
        verify(deckRepository).findById(deckId);
        verify(userRepository).save(user);
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndNullDeckIdProvided(){
        log.info("Should throw exception when delete deck by id and null deck id provided.");

        Long userId = 1L;
        Long deckId = null;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId));
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndNullUserIdProvided(){
        log.info("Should throw exception when delete deck by id and null user id provided.");

        Long userId = null;
        Long deckId = 1L;

        assertThrows(NullPointerException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId));
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndInvalidDeckIdProvided(){
        log.info("Should throw exception when delete deck by id and invalid deck id provided.");

        Long userId = 1L;
        Long deckId1 = -1L;
        Long deckId2 = 0L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId1));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId2));
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndInvalidUserIdProvided(){
        log.info("Should throw exception when delete deck by id and invalid user id provided.");

        Long userId1 = -1L;
        Long userId2 = 0L;
        Long deckId = 1L;

        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteDeckById(userId1, deckId));
        assertThrows(IllegalArgumentException.class,
            () -> userServiceImp.deleteDeckById(userId2, deckId));
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndDeckNotFound(){
        log.info("Should throw exception when delete deck by id and deck not found.");
        Long userId = 1L;
        Long deckId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId));

        verify(deckRepository).findById(deckId);
    }
    @Test
    void shouldThrowExceptionWhenDeleteDeckByIdAndUserNotFound(){
        log.info("Should throw exception when delete deck by id and user not found.");

        Long userId = 1L;
        Long deckId = 1L;

        assertThrows(EntityNotFoundException.class,
            () -> userServiceImp.deleteDeckById(userId, deckId));

        verify(userRepository).findById(userId);
    }
}
