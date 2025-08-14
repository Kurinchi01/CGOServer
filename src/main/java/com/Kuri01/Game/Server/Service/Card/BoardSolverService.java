package com.Kuri01.Game.Server.Service.Card;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.DTO.Card.SolverResult;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.Cards.CardState;
import com.Kuri01.Game.Server.Model.Cards.GameState;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Slf4j
@Service
public class BoardSolverService {

    // Map 1: Key wird von Value blockiert.
    private static final Map<Integer, int[]> BLOCKED_BY_MAP;
    // Map 2: Key blockiert Value.
    private static final Map<Integer, List<Integer>> UNLOCKS_MAP;

    private boolean analysisTimedOut;

    static {
        // Initialisierung der BLOCKED_BY_MAP
        BLOCKED_BY_MAP = new HashMap<>();
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

        // Automatische Erstellung der UNLOCKS_MAP aus der BLOCKED_BY_MAP
        UNLOCKS_MAP = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : BLOCKED_BY_MAP.entrySet()) {
            int blockedCardIndex = entry.getKey();
            for (int blockerIndex : entry.getValue()) {
                UNLOCKS_MAP.computeIfAbsent(blockerIndex, k -> new ArrayList<>()).add(blockedCardIndex);
            }
        }
    }

    private SolverResult bestSolution;

    public SolverResult analyze(GeneratedBoard generatedBoard) {
        this.bestSolution = new SolverResult(false, -1, -1);
        this.analysisTimedOut = false; // Setze den Timeout-Status für diese Analyse zurück

        GameState initialState = createInitialState(generatedBoard);

        // Merke dir die Startzeit in Millisekunden
        long startTime = System.currentTimeMillis();

        // Starte die Rekursion mit der Startzeit
        solveRecursively(initialState, 0, startTime);

        // Wenn die Analyse abgebrochen wurde, stellen Sie sicher, dass das Ergebnis "unlösbar" ist.
        if (this.analysisTimedOut) {
            log.warn("Solver hat das Zeitlimit von 1 Minute für ein Spielfeld überschritten. Markiere als unlösbar.");
            return new SolverResult(false, -1, -1);
        }

        return this.bestSolution;
    }

    private void solveRecursively(GameState currentState, int depth, long startTime) {
        // === SCHRITT 1: PRÜFE DEN TIMEOUT ===
        // 60000 Millisekunden = 30 Sekunden
        if (System.currentTimeMillis() - startTime > 30000) {
            this.analysisTimedOut = true;
            return; // Breche diesen gesamten Lösungszweig sofort ab
        }

        if (bestSolution.isSolvable()) {
            return;
        }

        if (currentState.getCardsClearedFromBoard() == 28) {
            bestSolution = new SolverResult(true, currentState.stockCardsUsed, currentState.longestCombo);
            return; // Beende diesen Lösungsweg.
        }

        List<Integer> playableBoardCardIndices = findPlayableBoardCards(currentState);
        boolean canDrawFromStock = !currentState.getStock().isEmpty();

        if (playableBoardCardIndices.isEmpty() && !canDrawFromStock) {
            return;
        }

        for (int index : playableBoardCardIndices) {
            GameState nextState = new GameState(currentState);
            playBoardCard(nextState, index);
            solveRecursively(nextState, depth + 1, startTime);
        }
        if (canDrawFromStock) {
            GameState nextState = new GameState(currentState);
            drawFromStock(nextState);
            solveRecursively(nextState, depth + 1, startTime);
        }
    }

    public GameState createInitialState(GeneratedBoard generatedBoard) {
        GameState gameState = new GameState();
        if (generatedBoard.stockPile().isEmpty()) {
            throw new IllegalStateException("Nachziehstapel darf nicht leer sein, um den Ablagestapel zu initialisieren.");
        }

        List<Card> stockCopy = new ArrayList<>(generatedBoard.stockPile());
        Card initialWasteCard = stockCopy.remove(stockCopy.size() - 1);
        gameState.getWaste().add(initialWasteCard);
        gameState.setStock(stockCopy);

        for (int i = 0; i < generatedBoard.boardLayout().size(); i++) {
            Card card = generatedBoard.boardLayout().get(i);
            CardState cardState = new CardState(card);
            if (i >= 18) {
                cardState.setFaceUp(true);
                cardState.getCard().setFaceUp(true);
            }

            int[] tmp = BLOCKED_BY_MAP.get(i);
            ArrayList<Integer> tmpList = new ArrayList<>();
            if (tmp != null) {
                tmpList.add(tmp[0]);
                tmpList.add(tmp[1]);
            }
            cardState.blocksCards = tmpList;
            gameState.getBoard().add(cardState);
        }

        return gameState;
    }

    public List<Integer> findPlayableBoardCards(GameState state) {
        List<Integer> result = new ArrayList<>();
        if (state.getWaste().isEmpty()) return result;

        Card compareCard = state.getWaste().get(state.getWaste().size() - 1);
        int compareValue = compareCard.getValue();

        for (int i = 0; i < state.getBoard().size(); i++) {
            CardState cardState = state.getBoard().get(i);

            if (cardState.isFaceUp() && !cardState.isRemoved()) {
                boolean isBlocked = false;
                int[] blockers = BLOCKED_BY_MAP.get(i);
                if (blockers != null) {
                    for (int blockerIndex : blockers) {
                        if (!state.getBoard().get(blockerIndex).isRemoved()) {
                            isBlocked = true;
                            break;
                        }
                    }
                }

                if (!isBlocked) {
                    int cardValue = cardState.getCard().getValue();
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

        // 2. Finde potenziell freigeschaltete Karten mit der UNLOCKS_MAP (Ihre BLOCK_MAP)
        List<Integer> potentiallyUnblockedIndices = UNLOCKS_MAP.get(cardIndex);
        if (potentiallyUnblockedIndices == null) {
            return; // Diese Karte hat nichts blockiert, wir sind fertig.
        }

        // 3. Prüfe für jede potenziell freigeschaltete Karte, ob sie WIRKLICH frei ist
        for (int unblockedIndex : potentiallyUnblockedIndices) {
            CardState cardToCheck = state.getBoard().get(unblockedIndex);

            // Hole die Liste der Blocker für diese Karte aus der statischen BLOCKED_BY_MAP
            int[] blockers = BLOCKED_BY_MAP.get(unblockedIndex);
            boolean isNowFree = true;
            if (blockers != null) {
                for (int blockerIndex : blockers) {
                    // Wenn auch nur EINER ihrer Blocker im aktuellen Spielzustand
                    // noch NICHT entfernt wurde...
                    if (!state.getBoard().get(blockerIndex).isRemoved()) {
                        isNowFree = false; // ...ist sie immer noch nicht frei.
                        break;
                    }
                }
            }

            // Wenn alle Blocker entfernt sind, decke die Karte auf.
            if (isNowFree) {
                cardToCheck.setFaceUp(true);
                cardToCheck.getCard().setFaceUp(true); // Wichtig: Auch das Card-Objekt aktualisieren
            }
        }
    }

    public void drawFromStock(GameState state) {
        state.setStockCardsUsed(state.getStockCardsUsed() + 1);
        state.setCurrentCombo(0);
        Card topCardOnDeck = state.getStock().remove(state.getStock().size() - 1);
        state.getWaste().add(topCardOnDeck);
    }
}