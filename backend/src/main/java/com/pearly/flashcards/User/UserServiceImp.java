package com.pearly.flashcards.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pearly.flashcards.Deck.Deck;
import com.pearly.flashcards.Deck.DeckRepository;
import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final DeckRepository deckRepository;
    private final ModelMapper modelMapper;

    public UserServiceImp(
            UserRepository userRepository,
            DeckRepository deckRepository,
            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.deckRepository = deckRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create new User and save it in the JPA repository.
     *
     * @param createUserDto DTO body of the new User.
     * @return Detailed DTO of newly created User entity.
     */
    @Override
    public UserDetailsDto create(CreateUserDto createUserDto) {
        log.info("Create new User.");

        Objects.requireNonNull(createUserDto, "Invalid argument: createUserDto must not be null.");

        User newUser = this.userRepository.save(this.modelMapper.map(createUserDto, User.class));
        UserDetailsDto dto = this.modelMapper.map(newUser, UserDetailsDto.class);

        return dto;
    }

    /**
     * Find all Users in the JPA repository.
     *
     * @return List of User DTO.
     */
    @Override
    public List<UserDto> findAll() {
        log.info("Find all Users.");

        List<UserDto> dtos = this.userRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, UserDto.class))
                .collect(Collectors.toList());

        return dtos;
    }

    /**
     * Find User by id in the JPA repository.
     *
     * @param id Id of the User.
     * @return Detailed DTO of found User entity.
     */
    @Override
    public UserDetailsDto findById(long id) {
        log.info("Find User id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));
        UserDetailsDto dto = this.modelMapper.map(user, UserDetailsDto.class);

        return dto;
    }

    /**
     * Delete User by id from the JPA repository.
     *
     * @param id Id of the User.
     */
    @Override
    public void deleteById(long id) {
        log.info("Delete User id={}.", id);

        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        this.userRepository.delete(user);
    }

    /**
     * Update User body with provided id in the JPA repository.
     *
     * @param updateUser DTO with updated body. Only present data will be replaced.
     * @param id         Id of the User.
     * @return DTO of updated User entity.
     */
    @Override
    public UserDto update(CreateUserDto update, long id) {
        log.info("Update User id={}.", id);

        Objects.requireNonNull(update, "Invalid argument: update must not be null.");
        Objects.requireNonNull(id, "Invalid argument: id must not be null.");

        User user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided id is invalid - there is no entity with id=" + id));

        if (update.getUserName() != null) {
            user.setUserName(update.getUserName());
        }

        user = this.userRepository.save(user); // Synchronize data in JPA
        UserDto dto = this.modelMapper.map(user, UserDto.class);

        return dto;
    }

    /**
     * Add Deck to User.
     *
     * @param userId Id of the User.
     * @param deckId Id of the Deck.
     * @return Detailed DTO of updated User entity.
     */
    @Override
    public UserDetailsDto addDeckById(long userId, long deckId) {
        log.info("Add Deck id={} to User id={}.", deckId, userId);

        Objects.requireNonNull(userId, "Invalid argument: userId must not be null.");
        Objects.requireNonNull(deckId, "Invalid argument: deckId must not be null.");

        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided userId is invalid - there is no entity with id=" + userId));
        Deck deck = this.deckRepository
                .findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided deckId is invalid - there is no entity with id=" + deckId));

        user.addDeck(deck);
        deck.addOwner(user);

        user = this.userRepository.save(user); // Synchronize data in JPA
        UserDetailsDto dto = this.modelMapper.map(user, UserDetailsDto.class);

        return dto;
    }

    /**
     * Delete Deck from User.
     *
     * @param userId Id of the User.
     * @param deckId Id of the Deck.
     * @return Detailed DTO of updated User entity.
     */
    @Override
    public UserDetailsDto deleteDeckById(long userId, long deckId) {
        log.info("Delete Deck id={} from User id={}.", deckId, userId);

        Objects.requireNonNull(userId, "Invalid argument: userId must not be null.");
        Objects.requireNonNull(deckId, "Invalid argument: deckId must not be null.");

        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided userId is invalid - there is no entity with id=" + userId));
        Deck deck = this.deckRepository
                .findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Provided deckId is invalid - there is no entity with id=" + deckId));

        user.deleteDeck(deck);
        deck.deleteOwner();

        user = this.userRepository.save(user); // Synchronize data in JPA
        UserDetailsDto dto = this.modelMapper.map(user, UserDetailsDto.class);

        return dto;
    }
}
