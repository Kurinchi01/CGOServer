package com.Kuri01.Game.Server.Model.Cards;

import com.Kuri01.Game.Server.DTO.Card.GeneratedBoard;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BoardGenerator {

    public GeneratedBoard generateRandomBoard() {
        // 1. Erstelle ein vollständiges, sortiertes 52-Karten-Deck
        //    basierend auf Ihrer Card-Klasse.
        List<Card> deck = new ArrayList<>(52);
        for (Card.Suit suit : Card.Suit.values()) {
            // Iteriere von 1 (Ass) bis 13 (König)
            for (int value = 1; value <= 13; value++) {
                deck.add(new Card(suit, value));
            }
        }

        // 2. Mische das Deck gründlich
        Collections.shuffle(deck);

        // 3. Teile das Deck auf
        // Die ersten 28 Karten sind das Spielfeld-Layout
        List<Card> boardLayout = new ArrayList<>(deck.subList(0, 28));

        // Die restlichen 24 Karten sind der Nachziehstapel (Stock Pile)
        List<Card> stockPile = new ArrayList<>(deck.subList(28, 52));

        return new GeneratedBoard(boardLayout, stockPile);
    }
}
