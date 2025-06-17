package com.Kuri01.Game.Server.Model.Cards;

public class Card {
    public enum Suit {
        heart, diamond, club, spade;

    }

    private final Suit suit;
    private final int value; // 1 = Ass, 11 = Bube, 12 = Dame, 13 = König
    private boolean faceUp;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        this.faceUp = false;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }


    @Override
    public String toString() {
        if (faceUp) return "[" + " " + value + "_" + suit +"]";
        return "[" +"XX_XX" +"]";
    }
}
