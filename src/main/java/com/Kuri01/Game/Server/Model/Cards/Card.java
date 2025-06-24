package com.Kuri01.Game.Server.Model.Cards;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    public enum Suit {
        heart, diamond, club, spade;

    }

    private Suit suit;
    private int value; // 1 = Ass, 11 = Bube, 12 = Dame, 13 = König
    private boolean faceUp;

    //Wichtig! Leerer Konstruktor für JPA
    public Card() {
    }

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        this.faceUp = false;
    }


    @Override
    public String toString() {
        if (faceUp) return "[" + " " + value + "_" + suit +"]";
        return "[" +"XX_XX" +"]";
    }
}
