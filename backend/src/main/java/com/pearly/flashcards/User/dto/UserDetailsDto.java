package com.pearly.flashcards.User.dto;

import java.util.List;

import com.pearly.flashcards.Deck.dto.DeckDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailsDto {
    private Long id;
    private String userName;
    private List<DeckDto> decks;
}
