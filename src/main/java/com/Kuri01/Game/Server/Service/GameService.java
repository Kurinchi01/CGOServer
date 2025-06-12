package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.Card;
import com.Kuri01.Game.Server.Model.Monster;
import com.Kuri01.Game.Server.Model.Rarity;
import com.Kuri01.Game.Server.Model.RoundStartData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    public RoundStartData createNewRound() {
        // 1. Erstelle ein komplettes Kartendeck mit deiner Card-Klasse
        List<Card> deck = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }

        // 2. Mische das Deck
        Collections.shuffle(deck);

        // 3. Teile die Karten für das Spielfeld auf (28 Karten)
        List<Card> triPeaksCards = new ArrayList<>(deck.subList(0, 28));

        // 4. Logik zum Aufdecken der untersten Reihe von TriPeaks
        // In einem Standard-TriPeaks-Layout mit 4 Reihen sind die letzten 10 Karten die unterste Reihe.
        // Wir gehen davon aus, dass die Karten in der Liste von oben nach unten angeordnet sind.
        for (int i = 18; i < 28; i++) {
            triPeaksCards.get(i).setFaceUp(true);
        }

        // 5. Nimm die nächste Karte für den Ablagestapel und decke sie auf
        Card initialWasteCard = deck.get(28);
        initialWasteCard.setFaceUp(true);

        // 6. Der Rest kommt in den Nachziehstapel
        List<Card> deckCards = new ArrayList<>(deck.subList(29, 52));

        // 7. Wähle ein Monster aus (wie zuvor)
        Monster monster = new Monster("",500,5, Rarity.common); // Platzhalter
        monster.setName("Grumpy Goblin");

        // 8. Erstelle eine Runden-ID
        String roundId = UUID.randomUUID().toString();

        // 9. Setze die Startdaten zusammen und gib sie zurück
        return new RoundStartData(roundId, monster, triPeaksCards, deckCards, initialWasteCard);
    }
}
