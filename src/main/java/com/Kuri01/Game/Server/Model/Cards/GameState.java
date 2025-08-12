package com.Kuri01.Game.Server.Model.Cards;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameState {
    public List<CardState> board = new ArrayList<>(); // Die 28 Karten auf dem Feld
    public List<Card> stock = new ArrayList<>(); // Der Nachziehstapel
    public List<Card> waste = new ArrayList<>(); // Der Ablagestapel

    public int cardsClearedFromBoard = 0;
    public int stockCardsUsed = 0;
    public int currentCombo = 0;
    public int longestCombo = 0;



    public GameState(GameState other) {
        // 1. Erstelle ECHTE Kopien der Listen.
        // Der Inhalt der Listen (die Card-Objekte) sind unveränderlich,
        // daher reicht eine flache Kopie der Listen selbst.
        this.stock = new ArrayList<>(other.stock);
        this.waste = new ArrayList<>(other.waste);

        // 2. Erstelle eine tiefe Kopie der Board-Liste, da sich CardState-Objekte ändern.
        this.board = new ArrayList<>(other.board.size());
        for (CardState cardState : other.board) {
            this.board.add(new CardState(cardState)); // Benutze den neuen Kopier-Konstruktor
        }

        // 3. Primitive Typen werden automatisch kopiert, das war bereits korrekt.
        this.cardsClearedFromBoard = other.cardsClearedFromBoard;
        this.stockCardsUsed = other.stockCardsUsed;
        this.currentCombo = other.currentCombo;
        this.longestCombo = other.longestCombo;
    }
}