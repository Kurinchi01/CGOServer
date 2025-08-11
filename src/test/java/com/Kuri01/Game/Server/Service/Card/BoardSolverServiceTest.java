package com.Kuri01.Game.Server.Service.Card;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.Cards.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardSolverServiceTest {

    private BoardSolverService boardSolverService;
    private GeneratedBoard testBoard;

    @BeforeEach
    void setUp() {
        // Erstelle eine neue Instanz des Service für jeden Test
        boardSolverService = new BoardSolverService();

        // Erstelle ein vorhersagbares, ungemischtes Deck, damit wir die
        // Karten an den Indizes genau kennen.
        List<Card> fullDeck = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            // Wir erstellen einfache Karten, nur um die Liste zu füllen.
            // Die Suit/Value-Kombination ist für diesen Struktur-Test irrelevant.
            fullDeck.add(new Card(Card.Suit.heart, i + 1));
        }

        List<Card> boardLayout = new ArrayList<>(fullDeck.subList(0, 28));
        List<Card> stockPile = new ArrayList<>(fullDeck.subList(28, 52));
        testBoard = new GeneratedBoard(boardLayout, stockPile);
    }

    @Test
    void createInitialState_shouldBuildCorrectBoardStructure() {
        // ========== ARRANGE (Vorbereiten) ==========
        // Die Testdaten werden in der setUp()-Methode vorbereitet.

        // ========== ACT (Ausführen) ==========
        // Rufe die zu testende Methode auf.
        GameState resultState = boardSolverService.createInitialState(testBoard);

        // ========== ASSERT (Überprüfen) ==========

        // 1. Grundlegende Überprüfungen
        assertNotNull(resultState);
        assertEquals(28, resultState.getBoard().size(), "Das Spielfeld sollte 28 Karten haben.");
        assertEquals(23, resultState.getStock().size(), "Der Nachziehstapel sollte 24 Karten haben.");
        assertEquals(1, resultState.getWaste().size(), "Der Ablagestapel sollte 1 Karte haben.");

        // 2. Überprüfe den Aufdeck-Status
        assertFalse(resultState.getBoard().get(0).isFaceUp(), "Karte 0 (Peak) sollte verdeckt sein.");
        assertFalse(resultState.getBoard().get(17).isFaceUp(), "Karte 17 sollte verdeckt sein.");
        assertTrue(resultState.getBoard().get(18).isFaceUp(), "Karte 18 (unterste Reihe) sollte aufgedeckt sein.");
        assertTrue(resultState.getBoard().get(27).isFaceUp(), "Karte 27 (unterste Reihe) sollte aufgedeckt sein.");

        // 3. Stichprobenartige Überprüfung der Blockier-Beziehungen
        // Peak-Karte
        List<Integer> blocksOfCard0 = resultState.getBoard().get(0).getBlocksCards();
        assertTrue(blocksOfCard0.contains(3) && blocksOfCard0.contains(4), "Karte 0 sollte die Karten 3 und 4 blockieren.");

        // Karte in der Mitte
        List<Integer> blocksOfCard4 = resultState.getBoard().get(4).getBlocksCards();
        assertTrue(blocksOfCard4.contains(10) && blocksOfCard4.contains(11), "Karte 4 sollte die Karten 10 und 11 blockieren.");

        // Letzte blockierende Karte
        List<Integer> blocksOfCard17 = resultState.getBoard().get(17).getBlocksCards();
        assertTrue(blocksOfCard17.contains(26) && blocksOfCard17.contains(27), "Karte 17 sollte die Karten 26 und 27 blockieren.");

        // Karte der untersten Reihe (darf nichts blockieren)
        List<Integer> blocksOfCard18 = resultState.getBoard().get(18).getBlocksCards();
        assertTrue(blocksOfCard18.isEmpty(), "Karte 18 sollte keine anderen Karten blockieren.");
    }
}