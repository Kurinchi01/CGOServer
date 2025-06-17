package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.*;

import static org.mockito.Mockito.when;

import com.Kuri01.Game.Server.Model.RPG.Chapter;
import com.Kuri01.Game.Server.Model.RPG.ChapterRepository;
import com.Kuri01.Game.Server.Model.RPG.Monster;
import com.Kuri01.Game.Server.Model.RPG.Rarity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceTest4 {

    // Wir injizieren die echte Instanz unseres GameService, die wir testen wollen.
    @Autowired
    private GameService gameService;

    // Wir ersetzen das echte ChapterRepository im Spring-Kontext durch einen Mock.
    // Das gibt uns die Kontrolle darüber, was die "Datenbank" zurückgibt.
    @MockitoBean
    private ChapterRepository chapterRepository;

    @Test
    void createNewRound_whenChapterExists_shouldReturnValidRoundData() {
        // ========== 1. Arrange (Vorbereiten) ==========

        // Erstelle Test-Daten: Ein Monster und ein Kapitel
        Monster testMonster = new Monster("Test Goblin", 50f, 10f, Rarity.common);
        Chapter mockChapter = new Chapter();
        mockChapter.setId(1L);
        mockChapter.setName("Testwald");
        mockChapter.setMonsterCount(1); // Wir erwarten ein Monster
        mockChapter.setMonsters(Set.of(testMonster));

        // Programmiere das Verhalten unseres Mocks:
        // "Wenn die Methode findById mit der ID 1L aufgerufen wird,
        // dann gib unseren vorbereiteten mockChapter zurück."
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(mockChapter));


        // ========== 2. Act (Ausführen) ==========

        // Rufe die Methode auf, die wir testen wollen.
        RoundStartData result = gameService.createNewRound(1L);


        // ========== 3. Assert (Überprüfen) ==========

        // Überprüfe, ob das Ergebnis unseren Erwartungen entspricht.
        assertNotNull(result, "Das Ergebnis sollte nicht null sein.");
        assertNotNull(result.getRoundId(), "Eine Runden-ID sollte generiert worden sein.");

        // Wurde die korrekte Anzahl an Monstern ausgewählt?
        assertEquals(1, result.getMonster().size(), "Es sollte genau ein Monster ausgewählt werden.");
        assertEquals("Test Goblin", result.getMonster().get(0).getName(), "Das ausgewählte Monster sollte der Test Goblin sein.");

        // Wurden die Karten korrekt aufgeteilt?
        assertEquals(28, result.getTriPeaksCards().size(), "Es sollten 28 Karten auf dem Feld sein.");
        assertEquals(23, result.getDeckCards().size(), "Es sollten 23 Karten im Nachziehstapel sein.");
        assertNotNull(result.getTopCard(), "Eine Startkarte für den Ablagestapel sollte vorhanden sein.");

        // Wurde der faceUp-Status korrekt gesetzt?
        assertTrue(result.getTopCard().isFaceUp(), "Die Ablagestapel-Karte sollte aufgedeckt sein.");
    }

}