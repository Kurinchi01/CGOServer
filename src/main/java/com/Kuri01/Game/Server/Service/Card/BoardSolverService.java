package com.Kuri01.Game.Server.Service.Card;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.DTO.Card.SolverResult;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.Cards.CardState;
import com.Kuri01.Game.Server.Model.Cards.GameState;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BoardSolverService {
    private SolverResult bestSolution;


    public SolverResult analyze(GeneratedBoard generatedBoard) {
        // Initialisiere das beste Ergebnis mit "unlösbar".
        this.bestSolution = new SolverResult(false, -1, -1);

        // 1. Erstelle den initialen Spielzustand aus dem generierten Board.
        GameState initialState = createInitialState(generatedBoard);

        // 2. Starte den rekursiven Lösungsprozess.
        solveRecursively(initialState);

        // 3. Gib das beste gefundene Ergebnis zurück.
        return this.bestSolution;
    }


    private void solveRecursively(GameState currentState) {
        // --- PRÜFE, OB DAS SPIEL GEWONNEN IST ---
        if (currentState.cardsClearedFromBoard == 28) {
            // Wir haben eine Lösung gefunden!
            // Prüfen, ob diese Lösung besser ist als die bisher beste.
            if (!bestSolution.isSolvable() || currentState.stockCardsUsed < bestSolution.difficulty()) {
                bestSolution = new SolverResult(true, currentState.stockCardsUsed, currentState.longestCombo);
            }
            return; // Diesen Lösungsweg nicht weiter verfolgen.
        }

        // --- FINDE ALLE MÖGLICHEN ZÜGE ---
        List<Integer> playableBoardCardIndices = findPlayableBoardCards(currentState);
        boolean canDrawFromStock = !currentState.stock.isEmpty();

        // --- PRÜFE, OB DAS SPIEL VERLOREN IST (DEAD END) ---
        if (playableBoardCardIndices.isEmpty() && !canDrawFromStock) {
            return; // Keine Züge mehr möglich, Sackgasse.
        }

        // --- FÜHRE JEDEN MÖGLICHEN ZUG AUS UND GEHE REKURSIV WEITER ---

        // 1. Probiere, jede spielbare Karte vom Feld zu nehmen.
        for (int index : playableBoardCardIndices) {
            GameState nextState = new GameState(currentState); // Erstelle eine Kopie für den neuen Pfad
            playBoardCard(nextState, index);
            solveRecursively(nextState); // Rekursiver Aufruf
        }

        // 2. Probiere, eine Karte vom Nachziehstapel zu ziehen.
        if (canDrawFromStock) {
            GameState nextState = new GameState(currentState); // Erstelle eine Kopie
            drawFromStock(nextState);
            solveRecursively(nextState); // Rekursiver Aufruf
        }
    }

    // --- HILFSMETHODEN ---

    public GameState createInitialState(GeneratedBoard generatedBoard) {
        GameState gameState = new GameState();
        gameState.setWaste(new ArrayList<>());

        //decke eine Karte auf und lege diese auf dem Ablagestapel
        gameState.getWaste().add(generatedBoard.stockPile().remove(0));

        //setze den Rest der Karten als Nachziehstapel
        gameState.setStock(generatedBoard.stockPile());
        gameState.setBoard(new ArrayList<>());


        // Erstelle zuerst alle 28 CardState-Objekte
        for (int i = 0; i < 28; i++) {
            Card card = generatedBoard.boardLayout().get(i);
            gameState.getBoard().add(new CardState(card));
        }

        // Gehe jetzt durch die erstellten Objekte und setze die Beziehungen und Zustände
        for (int i = 0; i < 28; i++) {
            CardState currentState = gameState.getBoard().get(i);

            // 1. Setze die Blockier-Beziehungen aus unserer Map
            if (BLOCK_MAP.containsKey(i)) {
                for (int blockedIndex : BLOCK_MAP.get(i)) {
                    currentState.getBlocksCards().add(blockedIndex);
                }
            }

            // 2. Setze die Karten der untersten Reihe aufgedeckt
            if (i >= 18) {
                currentState.setFaceUp(true);
            }
        }


        return gameState;
    }

    private List<Integer> findPlayableBoardCards(GameState state) {
        // TODO: Logik, um alle spielbaren Karten zu finden.
        // Eine Karte ist spielbar, wenn sie 'faceUp', nicht 'removed' ist und keine
        // anderen Karten mehr auf ihr liegen. Ihr Wert muss zum 'waste'-Stapel passen.
        return new ArrayList<>();
    }

    private void playBoardCard(GameState state, int cardIndex) {
        // TODO: Logik, um eine Karte vom Feld zu spielen.
        // - Karte als 'removed' markieren
        // - cardsClearedFromBoard erhöhen
        // - currentCombo erhöhen
        // - longestCombo aktualisieren
        // - Karte auf den 'waste'-Stapel legen
        // - Andere Karten "entblocken"
    }

    private void drawFromStock(GameState state) {
        // TODO: Logik, um eine Karte vom Nachziehstapel zu ziehen.
        // - stockCardsUsed erhöhen
        // - currentCombo auf 0 zurücksetzen
        // - Karte vom 'stock' zum 'waste'-Stapel bewegen
    }


    private static final Map<Integer, int[]> BLOCK_MAP;

    static {
        // Initialisiere Map einmal, wenn die Klasse geladen wird.
        BLOCK_MAP = new HashMap<>();
        BLOCK_MAP.put(0, new int[]{3, 4});
        BLOCK_MAP.put(1, new int[]{5, 6});
        BLOCK_MAP.put(2, new int[]{7, 8});
        BLOCK_MAP.put(3, new int[]{9, 10});
        BLOCK_MAP.put(4, new int[]{10, 11});
        BLOCK_MAP.put(5, new int[]{12, 13});
        BLOCK_MAP.put(6, new int[]{13, 14});
        BLOCK_MAP.put(7, new int[]{15, 16});
        BLOCK_MAP.put(8, new int[]{16, 17});
        BLOCK_MAP.put(9, new int[]{18, 19});
        BLOCK_MAP.put(10, new int[]{19, 20});
        BLOCK_MAP.put(11, new int[]{20, 21});
        BLOCK_MAP.put(12, new int[]{21, 22});
        BLOCK_MAP.put(13, new int[]{22, 23});
        BLOCK_MAP.put(14, new int[]{23, 24});
        BLOCK_MAP.put(15, new int[]{24, 25});
        BLOCK_MAP.put(16, new int[]{25, 26});
        BLOCK_MAP.put(17, new int[]{26, 27});
    }
}
