package com.pearly.flashcards.Card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  WARN: If you create or update entity use CreateCardDto instead.
 *        This DTO is not validated and can be dangerous.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CardDto {
    private Long id;
    private String question;
    private String answer;
}
