package com.Kuri01.Game.Server.Service.Card;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import com.Kuri01.Game.Server.DTO.Card.SolverResult;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.Cards.CardState;
import com.Kuri01.Game.Server.Model.Cards.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardSolverServiceTest {

    private BoardSolverService boardSolverService;
    private GeneratedBoard testBoard;
    private GameState testState;

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

    @Test
    void findPlayableBoardCards_shouldOnlyFindMatchingCardsFromBottomRow_onInitialState() {
        // ARRANGE: Waste-Karte ist eine 5. Auf dem initialen Board sind die aufgedeckten
        // Karten (18-27) die einzigen, die potenziell spielbar sind.
        // Karte 21 hat den Wert 4 (21 % 13 + 1 -> 8 + 1 -> 9? Nein. 21=8. 21%13=8. 8+1=9. okay 21 ist 9, 22 ist 10...
        // Lass uns die Werte explizit setzen für Klarheit.
        testState = boardSolverService.createInitialState(testBoard);
        testState.getBoard().get(18).getCard().setValue(9);
        testState.getBoard().get(21).getCard().setValue(4); // Spielbar (4 auf 5)
        testState.getBoard().get(22).getCard().setValue(6); // Spielbar (6 auf 5)
        testState.getBoard().get(23).getCard().setValue(9); // Nicht spielbar

        testState.getWaste().add(new Card(Card.Suit.heart, 5));

        // ACT
        List<Integer> playableCards = boardSolverService.findPlayableBoardCards(testState);

        // ASSERT
        assertEquals(2, playableCards.size());
        assertTrue(playableCards.contains(21) && playableCards.contains(22));
    }

    @Test
    void findPlayableBoardCards_shouldFindUnblockedCardInUpperRow() {
        testState = boardSolverService.createInitialState(testBoard);
        // ARRANGE: Karte 9 wird von 18 und 19 blockiert. Wir entfernen beide.
        testState.getBoard().get(18).setRemoved(true);
        testState.getBoard().get(19).setRemoved(true);
        // Dadurch sollte Karte 9 jetzt aufgedeckt sein (diese Logik gehört in playBoardCard)
        // Für den Test setzen wir sie manuell.
        testState.getBoard().get(9).setFaceUp(true);

        // Die Waste-Karte ist eine 6. Die Karte bei Index 9 hat den Wert 7.
        testState.getBoard().get(9).getCard().setValue(7);
        testState.getWaste().add(new Card(Card.Suit.heart, 6));

        // ACT
        List<Integer> playableCards = boardSolverService.findPlayableBoardCards(testState);

        // ASSERT
        // Die Methode sollte jetzt die Karte 9 finden, da ihre Blocker weg sind.
        assertTrue(playableCards.contains(9), "Karte 9 sollte spielbar sein, da 18 und 19 entfernt wurden.");
    }

    @Test
    void findPlayableBoardCards_shouldNotFindPartiallyUnblockedCard() {
        testState = boardSolverService.createInitialState(testBoard);
        // ARRANGE: Karte 9 wird von 18 und 19 blockiert. Wir entfernen nur einen davon.
        testState.getBoard().get(18).setRemoved(true);
        // Karte 19 ist noch da!
        testState.getBoard().get(9).setFaceUp(true); // Annahme: sie ist schon aufgedeckt

        testState.getBoard().get(9).getCard().setValue(7);
        testState.getWaste().add(new Card(Card.Suit.heart, 6));

        // ACT
        List<Integer> playableCards = boardSolverService.findPlayableBoardCards(testState);

        // ASSERT
        assertFalse(playableCards.contains(9), "Karte 9 sollte NICHT spielbar sein, da Blocker 19 noch vorhanden ist.");
    }


    @Test
    void playBoardCard_WithFindPlayableBoardCards_OnlyFindMatchingCardsFromBottomRow_onInitialState() {

        testState = boardSolverService.createInitialState(testBoard);

        testState.getBoard().get(18).getCard().setValue(9);
        testState.getBoard().get(21).getCard().setValue(4); // Spielbar (4 auf 5)
        testState.getBoard().get(22).getCard().setValue(6); // Spielbar (6 auf 5)
        testState.getBoard().get(23).getCard().setValue(5); // Noch nicht Spielbar, aber nach dem die 4(index 21) gespielt wurde schon


        testState.getWaste().add(new Card(Card.Suit.heart, 5));

        List<Integer> playableCards = boardSolverService.findPlayableBoardCards(testState);

        assertEquals(2, playableCards.size());

        boardSolverService.playBoardCard(testState, playableCards.get(0));

        assertEquals(4, testState.getWaste().get(testState.getWaste().size() - 1).getValue());

        playableCards = boardSolverService.findPlayableBoardCards(testState);

        assertEquals(1, playableCards.size());
        boardSolverService.playBoardCard(testState, playableCards.get(0));

        assertEquals(5, testState.getWaste().get(testState.getWaste().size() - 1).getValue());

    }

    @Test
    void playBoardCard_FindPlayableBoardCards_shouldFindUnblockedCardInUpperRow() {
        testState = boardSolverService.createInitialState(testBoard);
        // ARRANGE: Karte 9 wird von 18 und 19 blockiert. Wir entfernen beide.
        testState.getBoard().get(18).setRemoved(true);
        testState.getBoard().get(19).setRemoved(true);
        testState.getBoard().get(20).setRemoved(true);
        // Dadurch sollte Karte 9 jetzt aufgedeckt sein (diese Logik gehört in playBoardCard)
        // Für den Test setzen wir sie manuell.
        testState.getBoard().get(9).setFaceUp(true);
        testState.getBoard().get(10).setFaceUp(true);

        // Die Waste-Karte ist eine 6. Die Karte bei Index 9 hat den Wert 7.
        testState.getBoard().get(9).getCard().setValue(7);
        testState.getWaste().add(new Card(Card.Suit.heart, 6));
        testState.getBoard().get(10).getCard().setValue(8);

        // ACT
        List<Integer> playableCards = boardSolverService.findPlayableBoardCards(testState);
        assertEquals(1, playableCards.size());
        boardSolverService.playBoardCard(testState, playableCards.get(0));

        assertEquals(7, testState.getWaste().get(testState.getWaste().size() - 1).getValue());

        assertFalse(testState.getBoard().get(3).isFaceUp);

        playableCards = boardSolverService.findPlayableBoardCards(testState);

        assertEquals(1, playableCards.size());

        boardSolverService.playBoardCard(testState, playableCards.get(0));

        assertTrue(testState.getBoard().get(3).isFaceUp);



    }

    @Test
    void drawFromStock() {
        //ARRANGE
        testState = boardSolverService.createInitialState(testBoard);
        Card tmp = testState.getStock().get(testState.getStock().size()-1);
        testState.setCurrentCombo(5);

        //PRIOR CHECK
        assertNotEquals(testState.getWaste().get(0), tmp);
        assertEquals(0,testState.getStockCardsUsed());
        assertEquals(5,testState.getCurrentCombo());

        //ACT
        boardSolverService.drawFromStock(testState);

        //AFTER CHECK
        assertEquals(testState.getWaste().get(1), tmp);
        assertEquals(1,testState.getStockCardsUsed());
        assertEquals(0,testState.getCurrentCombo());

    }



    @Test
    void analyze_withPerfectlySolvableBoard_shouldSucceed() {
        // ========== ARRANGE (Vorbereiten) ==========
        // Wir erstellen ein "perfektes" Deck, das in einer langen Combo lösbar ist,
        // ohne den Nachziehstapel zu verwenden.

        List<Card> boardLayout = new ArrayList<>();
        // Erstelle eine vorhersagbare Sequenz für die 28 Karten auf dem Feld
        for (int i = 0; i < 28; i++) {
            // z.B. 3, 4, 5, ..., König, Ass, 2, ...
            int value = ((i + 2) % 13) + 1;
            boardLayout.add(new Card(Card.Suit.heart, value));
        }

        // Der Nachziehstapel ist für diese Lösung nicht relevant
        List<Card> stockPile = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            stockPile.add(new Card(Card.Suit.spade, 10));
        }

        // Die erste Karte vom Stapel (die zur Startkarte auf dem Ablagestapel wird)
        // muss zur ersten spielbaren Karte (Index 18, Wert 3) passen.
        stockPile.set(stockPile.size()-1, new Card(Card.Suit.club, 5));

        GeneratedBoard solvableBoard = new GeneratedBoard(boardLayout, stockPile);


        // ========== ACT (Ausführen) ==========
        // Rufe die zu testende analyze-Methode auf.
        SolverResult result = boardSolverService.analyze(solvableBoard);


        // ========== ASSERT (Überprüfen) ==========
        assertTrue(result.isSolvable(), "Dieses perfekt konstruierte Spielfeld MUSS als lösbar erkannt werden.");

        // Da wir den Nachziehstapel nicht verwenden müssen, sollte die "Schwierigkeit" (stockCardsUsed) 0 sein.
        assertEquals(0, result.difficulty(), "Für die optimale Lösung sollten keine Karten vom Stapel benötigt werden.");

        // Da alle 28 Karten in einer Kette abgeräumt werden können, sollte die längste Combo 28 sein.
        assertEquals(28, result.longestCombo(), "Die längste Combo sollte 28 sein.");
    }

    @Test
    void analyze_withUnsolvableBoard_shouldFail() {
        // ========== ARRANGE (Vorbereiten) ==========
        // Unlösbares Feld: Nur Könige (13) auf dem Feld, nur Asse (1) im Stapel.
        // Die Startkarte ist eine 7, daher kann kein Zug gemacht werden.
        List<Card> boardLayout = new ArrayList<>(Collections.nCopies(28, new Card(Card.Suit.heart, 13)));
        List<Card> stockPile = new ArrayList<>(Collections.nCopies(24, new Card(Card.Suit.heart, 1)));
        stockPile.set(0, new Card(Card.Suit.diamond, 7)); // Startkarte ist eine 7

        GeneratedBoard unsolvableBoard = new GeneratedBoard(boardLayout, stockPile);

        // ========== ACT (Ausführen) ==========
        SolverResult result = boardSolverService.analyze(unsolvableBoard);

        // ========== ASSERT (Überprüfen) ==========
        assertFalse(result.isSolvable(), "Dieses Spielfeld sollte als unlösbar erkannt werden.");
    }

}