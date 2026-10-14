package com.pearly.flashcards.Deck;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pearly.flashcards.Card.Card;
import com.pearly.flashcards.User.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "deck")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Name must not be blank.")
    @Size(min = 3, max = 100, message = "Name must have at lest 3 and at most 100 characters.")
    private String name;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "person_id")
    private User owner;

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

    public boolean hasOwner() {
        return (this.owner != null);
    }

    public void addOwner(User owner) {
        this.owner = owner;
    }

    public void deleteOwner() {
        this.owner = null;
    }

    public boolean hasCard(Card card) {
        return this.cards.contains(card);
    }

    public void addCard(Card card) {
        if (card.hasDeck()) {
            throw new IllegalStateException("This Card is already present in different Deck. Can not reassign.");
        }
        this.cards.add(card);
    }

    public void deleteCard(Card card) {
        if (!hasCard(card)) {
            throw new IllegalStateException("Provided Card does not belong to this Deck.");
        }
        this.cards.remove(card);
    }
}
