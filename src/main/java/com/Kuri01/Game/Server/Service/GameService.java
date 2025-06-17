package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final ChapterRepository chapterRepository;
    private final Random random = new Random();

    private final Map<String, RoundStartData> activeRounds = new ConcurrentHashMap<>();

    @Autowired
    public GameService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    /**
     * Erstellt eine komplett neue Spielrunde für ein gegebenes Kapitel.
     *
     * @param chapterId Die ID des Kapitels, das gestartet werden soll.
     * @return Ein RoundStartData-Objekt mit allen notwendigen Informationen für den Client.
     */
    public RoundStartData createNewRound(Long chapterId) {
        // Schritt 1: Lade das Kapitel und den dazugehörigen Monster-Pool aus der Datenbank.
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Kapitel nicht gefunden: " + chapterId));

        Set<Monster> monsterPool = chapter.getMonsters();
        if (monsterPool == null || monsterPool.isEmpty()) {
            throw new IllegalStateException("Für Kapitel " + chapterId + " sind keine Monster definiert!");
        }

        // Schritt 2: Wähle ein Monster aus dem Pool basierend auf einer gewichteten Zufallslogik.
        List<Monster> selectedMonster = selectMonstersFromPool(monsterPool, chapter.getMonsterCount());

        // Schritt 3: Erstelle und mische ein Standard-52-Karten-Deck.
        List<Card> deck = createAndShuffleDeck();

        // Schritt 4: Teile die Karten gemäß den TriPeaks-Regeln auf.
        List<Card> triPeaksCards = new ArrayList<>(deck.subList(0, 28));
        Card initialWasteCard = deck.get(28);
        List<Card> tuckCards = new ArrayList<>(deck.subList(29, 52));

        // Schritt 5: Setze den 'faceUp'-Status für die Startkarten.
        // Die unterste Reihe im TriPeaks-Layout (typischerweise die letzten 10 der 28 Karten) ist aufgedeckt.
        for (int i = 18; i < 28; i++) {
            triPeaksCards.get(i).setFaceUp(true);
        }
        initialWasteCard.setFaceUp(true);

        // Schritt 6: Erstelle eine einzigartige ID für diese Runde.
        String roundId = UUID.randomUUID().toString();

        // Schritt 7: Erstelle das Datenpaket für den Client.
        RoundStartData roundData = new RoundStartData(roundId, selectedMonster, triPeaksCards, tuckCards, initialWasteCard);

        // Schritt 8: Speichere den Startzustand der Runde auf dem Server. Dies ist entscheidend für die spätere Validierung!
        activeRounds.put(roundId, roundData);

        return roundData;
    }

    //gruppierter Monsterpool
    public Map<Rarity, List<Monster>> groupMonstersByRarity(Set<Monster> monsterPool) {

        Map<Rarity, List<Monster>> monstersByRarity = monsterPool.stream()
                .collect(Collectors.groupingBy(Monster::getRarity));

        return monstersByRarity;
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
    private List<Card> createAndShuffleDeck() {
        List<Card> deck = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

}
