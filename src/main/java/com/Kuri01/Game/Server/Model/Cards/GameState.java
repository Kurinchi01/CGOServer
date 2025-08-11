package com.Kuri01.Game.Server.Model.Cards;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameState {
    public List<CardState> board; // Die 28 Karten auf dem Feld
    public List<Card> stock; // Der Nachziehstapel
    public List<Card> waste; // Der Ablagestapel

    public int cardsClearedFromBoard = 0;
    public int stockCardsUsed = 0;
    public int currentCombo = 0;
    public int longestCombo = 0;

    public GameState(GameState gameState) {
        this.board = gameState.board;
        this.stock = gameState.stock;
        this.waste = gameState.waste;
        this.cardsClearedFromBoard = gameState.cardsClearedFromBoard;
        this.stockCardsUsed = gameState.stockCardsUsed;
        this.currentCombo = gameState.currentCombo;
        this.longestCombo = gameState.longestCombo;
    }
}