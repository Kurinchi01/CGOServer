package com.Kuri01.Game.Server.Model.Cards;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardState {
    public final Card card;
    public boolean isFaceUp;
    public boolean isRemoved;
    public List<Integer> blocksCards; // Indizes der Karten, die diese Karte blockiert

    public CardState(Card card) {
        this.card = card;
        this.blocksCards = new ArrayList<>();
        this.isFaceUp = false;
        this.isRemoved = false;

    }


    //copy Konstruktor
    public CardState(CardState cardState) {
        this.card = cardState.card;
        this.isRemoved = cardState.isRemoved;
        this.blocksCards = cardState.blocksCards;
        this.isFaceUp = cardState.isFaceUp;
    }
}
