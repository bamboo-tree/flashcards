package com.pearly.flashcards.User;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pearly.flashcards.User.dto.CreateUserDto;
import com.pearly.flashcards.User.dto.UserDetailsDto;
import com.pearly.flashcards.User.dto.UserDto;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImp(
            UserRepository userRepository,
            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Create bew User and save it in the JPA repository.
     *
     * @param createUserDto DTO body of the new User.
     * @return Detailed DTO of newly created User entity.
     */
    @Override
    public UserDetailsDto create(CreateUserDto createUserDto) {
        if (createUserDto == null) {
            throw new IllegalArgumentException("Provided createUserDto is invalid - DTO is null");
        }
        if (createUserDto.getUserName().isBlank()) {
            throw new IllegalArgumentException("Provided createUserDto is invalid - userName is blank");
        }

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
        List<UserDto> dtos = this.userRepository
                .findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, UserDto.class))
                .toList();

        return dtos;
    }

    /**
     * Find User by id in the JPA repository.
     *
     * @param id Id of the User.
     * @return Detailed DTO of found User entity.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetailsDto findById(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
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
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
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
    @Transactional
    @Override
    public UserDto update(CreateUserDto updateUser, long id) {
        if (updateUser == null) {
            throw new IllegalArgumentException("Provided updateUser is invalid - DTO is null");
        }
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Provided id is invalid - there is no entity with id=" + id));

        if (!updateUser.getUserName().isBlank()) {
            user.setUserName(updateUser.getUserName());
        }

        User updatedUser = this.userRepository.save(user);
        UserDto dto = this.modelMapper.map(updatedUser, UserDto.class);

        return dto;
    }
}
