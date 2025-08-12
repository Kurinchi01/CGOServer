package com.Kuri01.Game.Server.Service.Card;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.DTO.Card.SolverResult;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.Cards.CardState;
import com.Kuri01.Game.Server.Model.Cards.GameState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Service
public class BoardSolverService {

    private final Map<Integer, int[]> BLOCK_MAP;
    private final Map<Integer, int[]> BLOCKED_BY_MAP;

    public BoardSolverService() {

        ///Key ist blockiert von Value
        BLOCKED_BY_MAP = new HashMap<>();
        // Karten 0-17 sind die, die blockiert werden.
        BLOCKED_BY_MAP.put(0, new int[]{3, 4});
        BLOCKED_BY_MAP.put(1, new int[]{5, 6});
        BLOCKED_BY_MAP.put(2, new int[]{7, 8});

        BLOCKED_BY_MAP.put(3, new int[]{9, 10});
        BLOCKED_BY_MAP.put(4, new int[]{10, 11});
        BLOCKED_BY_MAP.put(5, new int[]{12, 13});
        BLOCKED_BY_MAP.put(6, new int[]{13, 14});
        BLOCKED_BY_MAP.put(7, new int[]{15, 16});
        BLOCKED_BY_MAP.put(8, new int[]{16, 17});

        BLOCKED_BY_MAP.put(9, new int[]{18, 19});
        BLOCKED_BY_MAP.put(10, new int[]{19, 20});
        BLOCKED_BY_MAP.put(11, new int[]{20, 21});
        BLOCKED_BY_MAP.put(12, new int[]{21, 22});
        BLOCKED_BY_MAP.put(13, new int[]{22, 23});
        BLOCKED_BY_MAP.put(14, new int[]{23, 24});
        BLOCKED_BY_MAP.put(15, new int[]{24, 25});
        BLOCKED_BY_MAP.put(16, new int[]{25, 26});
        BLOCKED_BY_MAP.put(17, new int[]{26, 27});


        /// Key blockiert Value
        BLOCK_MAP = new HashMap<>();
        BLOCK_MAP.put(3, new int[]{0});
        BLOCK_MAP.put(4, new int[]{0});
        BLOCK_MAP.put(5, new int[]{1});
        BLOCK_MAP.put(6, new int[]{1});
        BLOCK_MAP.put(7, new int[]{2});
        BLOCK_MAP.put(8, new int[]{2});
        BLOCK_MAP.put(9, new int[]{3});
        BLOCK_MAP.put(10, new int[]{3, 4});
        BLOCK_MAP.put(11, new int[]{4});
        BLOCK_MAP.put(12, new int[]{5});
        BLOCK_MAP.put(13, new int[]{5, 6});
        BLOCK_MAP.put(14, new int[]{6});
        BLOCK_MAP.put(15, new int[]{7});
        BLOCK_MAP.put(16, new int[]{7, 8});
        BLOCK_MAP.put(17, new int[]{8});
        BLOCK_MAP.put(18, new int[]{9});
        BLOCK_MAP.put(19, new int[]{9, 10});
        BLOCK_MAP.put(20, new int[]{10, 11});
        BLOCK_MAP.put(21, new int[]{11, 12});
        BLOCK_MAP.put(22, new int[]{12, 13});
        BLOCK_MAP.put(23, new int[]{13, 14});
        BLOCK_MAP.put(24, new int[]{14, 15});
        BLOCK_MAP.put(25, new int[]{15, 16});
        BLOCK_MAP.put(26, new int[]{16, 17});
        BLOCK_MAP.put(27, new int[]{17});

    }

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
        gameState.getWaste().add(generatedBoard.stockPile().remove(generatedBoard.stockPile().size()-1));

        //setze den Rest der Karten als Nachziehstapel
        gameState.setStock(generatedBoard.stockPile());
        gameState.setBoard(new ArrayList<>());


        // Erstelle zuerst alle 28 CardState-Objekte
        for (int i = 0; i < generatedBoard.boardLayout().size(); i++) {
            Card card = generatedBoard.boardLayout().get(i);
            gameState.getBoard().add(new CardState(card));
        }

        // Gehe jetzt durch die erstellten Objekte und setze die Beziehungen und Zustände
        for (int i = 0; i < generatedBoard.boardLayout().size(); i++) {
            CardState currentState = gameState.getBoard().get(i);

            // 1. Setze die Blockier-Beziehungen aus unserer Map
            if (BLOCKED_BY_MAP.containsKey(i)) {
                for (int blockedIndex : BLOCKED_BY_MAP.get(i)) {
                    currentState.getBlocksCards().add(blockedIndex);
                }
            }

            // 2. Setze die Karten der untersten Reihe aufgedeckt
            if (i >= 18) {
                currentState.setFaceUp(true);
                currentState.getCard().setFaceUp(true);
            }
        }


        return gameState;
    }

    public List<Integer> findPlayableBoardCards(GameState state) {
        List<Integer> result = new ArrayList<>();
        Card compareCard = state.getWaste().get(state.getWaste().size() - 1);
        int compareValue = compareCard.getValue();

        for (int i = 0; i < state.getBoard().size(); i++) {
            CardState cardState = state.getBoard().get(i);
            // 1. Grundvoraussetzung: Karte muss aufgedeckt und noch im Spiel sein.
            if (cardState.isFaceUp() && !cardState.isRemoved()) {

                // 2. NEUE PRÜFUNG: Ist die Karte blockiert?
                boolean isBlocked = false;
                // Hole die Liste der Blocker aus unserer Map
                int[] blockers = BLOCKED_BY_MAP.get(i);
                if (blockers != null) { // Karten der untersten Reihe haben keine Blocker
                    for (int blockerIndex : blockers) {
                        // Wenn auch nur EINER der Blocker noch nicht entfernt wurde...
                        if (!state.getBoard().get(blockerIndex).isRemoved()) {
                            isBlocked = true; // ...ist die Karte blockiert.
                            break;
                        }
                    }
                }

                // Fahre nur fort, wenn die Karte NICHT blockiert ist.
                if (!isBlocked) {
                    int cardValue = cardState.getCard().getValue();

                    // 3. Passt der Wert? (Ihre Logik war hier bereits korrekt)
                    if (Math.abs(cardValue - compareValue) == 1 ||
                            (cardValue == 1 && compareValue == 13) ||
                            (cardValue == 13 && compareValue == 1)) {

                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    public void playBoardCard(GameState state, int cardIndex) {
        CardState playedCard = state.getBoard().get(cardIndex);

        // 1. Zustand der gespielten Karte aktualisieren
        playedCard.setRemoved(true);
        state.getWaste().add(playedCard.getCard());
        state.setCardsClearedFromBoard(state.getCardsClearedFromBoard() + 1);
        state.setCurrentCombo(state.getCurrentCombo() + 1);
        if (state.getCurrentCombo() > state.getLongestCombo()) {
            state.setLongestCombo(state.getCurrentCombo());
        }

        // 2. Finde potenziell freigeschaltete Karten mit der UNLOCKS_MAP
        int[] potentiallyUnblockedIndices = BLOCK_MAP.get(cardIndex);
        if (potentiallyUnblockedIndices == null) {
            return; // Diese Karte hat nichts blockiert
        }

        // 3. Prüfe für jede potenziell freigeschaltete Karte, ob sie WIRKLICH frei ist
        for (int unblockedIndex : potentiallyUnblockedIndices) {
            CardState cardToCheck = state.getBoard().get(unblockedIndex);

            // Hole die Liste der Blocker für diese Karte aus der BLOCKED_BY_MAP
            int[] blockers = BLOCKED_BY_MAP.get(unblockedIndex);
            boolean isNowFree = true;
            if (blockers != null) {
                for (int blockerIndex : blockers) {
                    // Wenn auch nur EINER ihrer Blocker noch NICHT entfernt wurde...
                    if (!state.getBoard().get(blockerIndex).isRemoved()) {
                        isNowFree = false; // ...ist sie immer noch nicht frei.
                        break;
                    }
                }
            }

            // Wenn alle Blocker entfernt sind, decke die Karte auf.
            if (isNowFree) {
                cardToCheck.setFaceUp(true);
                cardToCheck.getCard().setFaceUp(true);
            }
        }

    }

    public void drawFromStock(GameState state) {
        state.setStockCardsUsed(state.stockCardsUsed + 1);
        state.setCurrentCombo(0);
        Card topCardOnDeck = state.getStock().remove(state.getStock().size() - 1);
        state.getWaste().add(topCardOnDeck);

        // TODO: Logik, um eine Karte vom Nachziehstapel zu ziehen.
        // - stockCardsUsed erhöhen
        // - currentCombo auf 0 zurücksetzen
        // - Karte vom 'stock' zum 'waste'-Stapel bewegen
    }

}
