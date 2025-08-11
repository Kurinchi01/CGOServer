package com.Kuri01.Game.Server.Model.Cards;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PreCalculatedBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT") // Für lange JSON-Strings
    private String boardLayoutJson;

    @Column(columnDefinition = "TEXT")
    private String stockPileJson;

    private boolean solvable;

    private int difficultyScore;

    private int longestCombo;

    @Enumerated(EnumType.STRING)
    private BoardStatus status = BoardStatus.AVAILABLE;
}



