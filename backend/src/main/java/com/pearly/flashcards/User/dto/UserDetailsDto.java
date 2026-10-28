package com.pearly.flashcards.User.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.pearly.flashcards.Deck.dto.DeckDto;

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
public class UserDetailsDto {
    private Long id;
    private String userName;
    private List<DeckDto> decks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
