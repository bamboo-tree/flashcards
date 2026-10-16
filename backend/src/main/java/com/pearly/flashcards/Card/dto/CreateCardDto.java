package com.pearly.flashcards.Card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 *  INFO: If you create or update entity use CreateCardDto.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateCardDto {
    @NotBlank(message = "Question must not be blank")
    @Size(min = 3, max = 500, message = "Question must have at least 3 and at most 500 characters.")
    private String question;
    @NotBlank(message = "Answer must not be blank")
    @Size(min = 3, max = 500, message = "Answer must have at least 3 and at most 500 characters.")
    private String answer;
}
