package com.Kuri01.Game.Server.Model;

import java.util.List;

// Dies ist KEINE @Entity, da wir es nur als Datenpaket zum Senden verwenden.
public class RoundStartData {

    private String roundId;
    private Monster monster;
    private List<Card> triPeaksCards; // Die 28 Karten auf dem Spielfeld
    private List<Card> deckCards;     // Der verbleibende Nachziehstapel
    private Card topCard;  // Die erste aufgedeckte Karte



    // Beispiel-Konstruktor:
    public RoundStartData(String roundId, Monster monster, List<Card> triPeaksCards, List<Card> deckCards, Card topCard) {
        this.roundId = roundId;
        this.monster = monster;
        this.triPeaksCards = triPeaksCards;
        this.deckCards = deckCards;
        this.topCard = topCard;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public List<Card> getTriPeaksCards() {
        return triPeaksCards;
    }

    public void setTriPeaksCards(List<Card> triPeaksCards) {
        this.triPeaksCards = triPeaksCards;
    }

    public List<Card> getTuckCards() {
        return deckCards;
    }

    public void setTuckCards(List<Card> deckCards) {
        this.deckCards = deckCards;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }
}
