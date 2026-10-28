package com.pearly.flashcards.Deck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  INFO: If you create or update entity use CreateDeckDto.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateDeckDto {
    @NotBlank(message = "Name must not be blank.")
    @Size(min = 3, max = 100, message = "Name must have at lest 3 and at most 100 characters.")
    private String name;
}
