package com.pearly.flashcards.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pearly.flashcards.Deck.Deck;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

@Entity(name = "person")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    @NotBlank(message = "UserName must not be blank.")
    @Size(min = 3, max = 50, message = "UserName must have at least 3 and at most 50 characters.")
    private String userName;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private final List<Deck> decks = new ArrayList<>();

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

    public boolean hasDeck(Deck deck) {
        return this.decks.contains(deck);
    }

    public void addDeck(Deck deck) {
        if (deck.hasOwner()) {
            throw new IllegalStateException("Deck already has the owner. Cannot reassign.");
        }
        this.decks.add(deck);
    }

    public void deleteDeck(Deck deck) {
        if (!hasDeck(deck)) {
            throw new IllegalStateException("Provided deck does not belong to this User.");
        }
        this.decks.remove(deck);
    }
}
