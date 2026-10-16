package com.pearly.flashcards.Card;

import java.time.LocalDateTime;

import com.pearly.flashcards.Deck.Deck;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "card")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false, unique = false)
    @NotBlank(message = "Question must not be blank")
    @Size(min = 3, max = 500, message = "Question must have at least 3 and at most 500 characters.")
    private String question;

    @Column(name = "answer", nullable = false, unique = false)
    @NotBlank(message = "Answer must not be blank")
    @Size(min = 1, max = 500, message = "Answer must have at least 1 and at most 500 characters.")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @NotNull(message = "CreatedAt must not be blank.")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // END OF ENTITY DEFINITION

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasDeck() {
        return (this.deck != null);
    }

    public void addDeck(Deck deck) {
        this.deck = deck;
    }

    public void deleteDeck() {
        this.deck = null;
    }
}
