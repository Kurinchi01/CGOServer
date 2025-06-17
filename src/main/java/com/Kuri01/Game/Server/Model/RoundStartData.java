package com.Kuri01.Game.Server.Model;

import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.RPG.Monster;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Dies ist KEINE @Entity, da wir es nur als Datenpaket zum Senden verwenden.
@Getter
@Setter
public class RoundStartData {

    private String roundId;
    private List<Monster>  monster;
    private List<Card> triPeaksCards; // Die 28 Karten auf dem Spielfeld
    private List<Card> deckCards;     // Der verbleibende Nachziehstapel
    private Card topCard;  // Die erste aufgedeckte Karte



    // Beispiel-Konstruktor:
    public RoundStartData(String roundId, List<Monster> monster, List<Card> triPeaksCards, List<Card> deckCards, Card topCard) {
        this.roundId = roundId;
        this.monster = monster;
        this.triPeaksCards = triPeaksCards;
        this.deckCards = deckCards;
        this.topCard = topCard;
    }

}
