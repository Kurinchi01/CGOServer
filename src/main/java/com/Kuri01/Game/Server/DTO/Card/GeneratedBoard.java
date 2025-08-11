package com.Kuri01.Game.Server.DTO.Card;

import com.Kuri01.Game.Server.Model.Cards.Card;

import java.util.List;

// Dieser Record hält das Ergebnis einer Kartengenerierung.
public record GeneratedBoard(List<Card> boardLayout, List<Card> stockPile) {}