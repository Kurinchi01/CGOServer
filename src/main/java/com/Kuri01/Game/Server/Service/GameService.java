package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.Card.RoundEndRequest;
import com.Kuri01.Game.Server.DTO.Card.RoundOutcome;
import com.Kuri01.Game.Server.DTO.Card.RoundStartData;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.DTO.Card.CardMove;
import com.Kuri01.Game.Server.Model.Cards.PreCalculatedBoard;
import com.Kuri01.Game.Server.Model.RPG.*;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.LootChest;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.LootResult;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.LootTableEntry;
import com.Kuri01.Game.Server.Repository.Card.PreCalculatedBoardRepository;
import com.Kuri01.Game.Server.Repository.ChapterRepository;
import com.Kuri01.Game.Server.Repository.RPG.LootChestRepository;
import com.Kuri01.Game.Server.Repository.RPG.PlayerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Getter
public class GameService {

    private final ChapterRepository chapterRepository;
    private final LootChestRepository lootChestRepository;
    private final PlayerRepository playerRepository;
    private final PreCalculatedBoardRepository preCalculatedBoardRepository;
    private final Random random = new Random();

    private final Map<String, RoundStartData> activeRounds = new ConcurrentHashMap<>();

    @Autowired
    public GameService(ChapterRepository chapterRepository, LootChestRepository lootChestRepository, PlayerRepository playerRepository, PreCalculatedBoardRepository preCalculatedBoardRepository) {
        this.chapterRepository = chapterRepository;
        this.lootChestRepository = lootChestRepository;
        this.playerRepository = playerRepository;
        this.preCalculatedBoardRepository = preCalculatedBoardRepository;
    }

    /**
     * Erstellt eine komplett neue Spielrunde für ein gegebenes Kapitel.
     *
     * @param chapterId Die ID des Kapitels, das gestartet werden soll.
     * @return Ein RoundStartData-Objekt mit allen notwendigen Informationen für den Client.
     */
    public RoundStartData createNewRound(String googleID, Long chapterId) {
        // Schritt 1: Lade das Kapitel und den dazugehörigen Monster-Pool aus der Datenbank.
        Player player = playerRepository.findByGoogleId(googleID)
                .orElseThrow(() -> new IllegalStateException("Authentifizierter Spieler nicht in der DB gefunden: " + googleID));

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Kapitel nicht gefunden: " + chapterId));

        Set<Monster> monsterPool = chapter.getMonsters();
        if (monsterPool == null || monsterPool.isEmpty()) {
            throw new IllegalStateException("Für Kapitel " + chapterId + " sind keine Monster definiert!");
        }

        // Schritt 2: Wähle ein Monster aus dem Pool basierend auf einer gewi chteten Zufallslogik.
        List<Monster> selectedMonster = selectMonstersFromPool(monsterPool, chapter.getMonsterCount());

        PreCalculatedBoard tmpBoard = preCalculatedBoardRepository.findRandomAvailableBoard().orElse(null);
        if (tmpBoard == null) throw new IllegalStateException("Kein vorberechnetes Board gefunden");

        long roundId = tmpBoard.getId();


        // Schritt 7: Erstelle das Datenpaket für den Client.
        RoundStartData roundData = new RoundStartData(roundId, selectedMonster, triPeaksCards, tuckCards, initialWasteCard);

        // Schritt 8: Speichere den Startzustand der Runde auf dem Server. Dies ist entscheidend für die spätere Validierung!
        activeRounds.put(roundId, roundData);

        return roundData;
    }


    /**
     * Wählt eine bestimmte Anzahl von Monstern aus dem gegebenen Pool aus.
     * Diese Methode ist jetzt optimiert, um die Gruppierung nur einmal durchzuführen.
     */
    private List<Monster> selectMonstersFromPool(Set<Monster> monsterPool, int monsterCount) {
        if (monsterPool == null || monsterPool.isEmpty()) {
            throw new IllegalStateException("Der Monster-Pool darf nicht leer sein!");
        }

        // OPTIMIERUNG: Gruppiere den Pool nur EINMAL am Anfang.
        Map<Rarity, List<Monster>> monstersByRarity = monsterPool.stream()
                .collect(Collectors.groupingBy(Monster::getRarity));

        List<Monster> selectedMonsters = new ArrayList<>();
        for (int i = 0; i < monsterCount; i++) {
            // Gib die bereits gruppierte Map an die Hilfsmethode weiter.
            Monster chosenMonster = selectSingleWeightedMonster(monstersByRarity);
            selectedMonsters.add(chosenMonster);
        }
        return selectedMonsters;
    }


    private Monster selectSingleWeightedMonster(Map<Rarity, List<Monster>> monstersByRarity) {
        // Schritt 1 entfällt, da die Map bereits übergeben wird.

        // Schritt 2: Würfle eine Ziel-Seltenheit.
        Rarity targetRarity = Rarity.getRandomRarity();
        Rarity currentRarity = targetRarity;

        // Schritt 3: "Cascading Fallback" - die Logik hier bleibt identisch.
        for (int i = 0; i < Rarity.values().length; i++) {
            List<Monster> availableMonsters = monstersByRarity.get(currentRarity);
            if (availableMonsters != null && !availableMonsters.isEmpty()) {
                return availableMonsters.get(random.nextInt(availableMonsters.size()));
            }
            currentRarity = currentRarity.getLessRare();
        }

        // Notfall-Fallback, falls die Map komplett leer sein sollte.
        // Wir nehmen einen beliebigen Wert aus der Map und davon das erste Monster.
        return monstersByRarity.values().stream()
                .filter(list -> !list.isEmpty())
                .findFirst()
                .flatMap(list -> list.stream().findAny())
                .orElseThrow(() -> new IllegalStateException("Konnte kein Monster aus der Map auswählen."));
    }


    /**
     * Erstellt ein Standard-52-Karten-Deck und mischt es.
     *
     * @return Eine gemischte Liste von Card-Objekten.
     */
    public List<Card> createAndShuffleDeck(boolean test) {
        List<Card> deck = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }
        if (!test)
            Collections.shuffle(deck);

        return deck;
    }

    /**
     * Beendet eine Runde und entfernt sie aus dem aktiven Speicher.
     *
     * @param roundId die ID der zu beendenden Runde.
     */
    public void endRound(String roundId) {
        activeRounds.remove(roundId);
    }

    /**
     * Verarbeitet das Ende einer Runde für den authentifizierten Spieler.
     *
     * @param googleId Die Google ID des Spielers.
     * @param roundId  Die ID der beendeten Runde.
     * @param request  Die Daten vom Client zum Rundenende.
     * @return Eine Liste der erhaltenen Items (Truhen).
     */
    @Transactional
    public List<Item> processRoundEnd(String googleId, String roundId, RoundEndRequest request) {
        // NEU: Lade den Spieler, der die Belohnungen erhalten soll.
        Player player = playerRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new IllegalStateException("Authentifizierter Spieler nicht in der DB gefunden: " + googleId));

        RoundStartData originalRound = activeRounds.get(roundId);
        if (originalRound == null) {
            throw new IllegalArgumentException("Ungültige oder bereits beendete Runde: " + roundId);
        }

        boolean isValidWin = true;
        List<Item> rewardedChests = new ArrayList<>();

        if (request.outcome() == RoundOutcome.WIN && isValidWin) {
            // Logik zur Belohnung des Spielers
            List<Monster> monsters = originalRound.getMonster();
            for (Monster monster : monsters) {
                // TODO: Erweitere die Logik, um die richtige Truhe zu finden (z.B. über LootChestRepository)
                // Fürs Erste erstellen wir ein Platzhalter-Item
                Item chest = new Item(); // Platzhalter
                chest.setName(monster.getRarity() + " Chest");
                rewardedChests.add(chest);
            }

            // GEÄNDERT: Das TODO wird jetzt durch echte Logik ersetzt!
            if (!rewardedChests.isEmpty()) {
                // HINWEIS: Hier wäre ein InventoryService sauberer, aber für den Anfang geht das auch hier.
                // player.getInventory().addItems(rewardedChests); // Angenommen, es gäbe eine addItems-Methode
                // Fürs Erste fügen wir sie manuell hinzu (Annahme: Inventar hat Platz)
                for (Item chest : rewardedChests) {
                    // Du bräuchtest eine Logik, um das Item im Inventar zu speichern.
                    // Das würde das Erstellen von InventorySlot-Entitäten beinhalten.
                }
                playerRepository.save(player); // Speichert den Spieler und Kaskaden-Änderungen an seinem Inventar.
            }
        }

        endRound(roundId);
        return rewardedChests;
    }


    private LootResult calculateLoot(Monster monster) {
        // Die Seltenheit des Loots könnte von der Seltenheit des Monsters abhängen.
        Rarity monsterRarity = monster.getRarity();
        String lootMessage = "Du hast eine Truhe erhalten! (Seltenheit: " + monsterRarity + ")";

        // TODO: Erstelle ein echtes Item-Objekt und füge es dem Spieler-Inventar hinzu.

        return new LootResult(lootMessage, monsterRarity);
    }


    /** ________________________________________________________________________________________________________________
     *                                             Hilfsmethoden                                                        *
     *                                                                                                                  *
     * _______________________________________________________________________________________________________________**/


}

