package com.Kuri01.Game.Server.Model.Cards;

public enum CardActionType {
    /**
     * Eine Karte wird vom Spielfeld auf den Ablagestapel gespielt.
     */
    PLAY_CARD,

    /**
     * Eine neue Karte wird vom Nachziehstapel aufgedeckt, weil kein Zug möglich war.
     */
    DRAW_FROM_DECK
}