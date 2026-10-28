package com.pearly.flashcards.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  INFO: If you create or update entity use CreateUserDto.
 */

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateUserDto {
    @NotBlank(message = "UserName must not be blank.")
    @Size(min = 3, max = 50, message = "UserName must have at least 3 and at most 50 characters.")
    private String userName;
}
