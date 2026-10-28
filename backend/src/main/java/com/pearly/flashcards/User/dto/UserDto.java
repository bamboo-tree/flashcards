package com.pearly.flashcards.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  WARN: If you create or update entity use CreateUserDto instead.
 *        This DTO is not validated and can be dangerous.
 */

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private Long id;
    private String userName;
}
