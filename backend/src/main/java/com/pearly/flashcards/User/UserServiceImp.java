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
     * Creates new User and saves it in JPA repository.
     *
     * @param createUserDto DTO body of the new User.
     * @return UserDetailsDto of newly created User.
     */
    @Override
    public UserDetailsDto create(CreateUserDto createUserDto) {
        if (createUserDto.getUserName().isBlank()) {
            throw new IllegalArgumentException("Provided DTO is invalid, createUserDto.userName is blank.");
        }
        User newUser = this.userRepository.save(this.modelMapper.map(createUserDto, User.class));
        UserDetailsDto dto = this.modelMapper.map(newUser, UserDetailsDto.class);

        return dto;
    }

    /**
     * Find all Users in the JPA repository.
     *
     * @return List of all UserDto.
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
     * Find User by id.
     * This method is transactional.
     *
     * @param id User's id.
     * @return Detailed User DTO.
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetailsDto findById(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no User with id=" + id));
        UserDetailsDto dto = this.modelMapper.map(user, UserDetailsDto.class);

        return dto;
    }

    /**
     * Delete User from JPA repository by id.
     *
     * @param id User's id.
     */
    @Override
    public void deleteById(long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no User with id=" + id));

        if (user != null) {
            this.userRepository.deleteById(id);
        }
    }

    /**
     * Update User data in place.
     * This method is transactional.
     *
     * @param updateUser Updated User data.
     * @param id         User's id to update.
     * @return UserDto with updated data.
     */
    @Transactional
    @Override
    public UserDto update(CreateUserDto updateUser, long id) {
        if (updateUser == null) {
            throw new IllegalArgumentException("No update data provided");
        }
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no User with id=" + id));

        if (updateUser.getUserName() != null && !updateUser.getUserName().isBlank()) {
            user.setUserName(updateUser.getUserName());
        }

        User updatedUser = this.userRepository.save(user);
        UserDto dto = this.modelMapper.map(updatedUser, UserDto.class);

        return dto;
    }
}
