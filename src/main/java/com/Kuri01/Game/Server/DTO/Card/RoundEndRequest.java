package com.Kuri01.Game.Server.DTO.Card;

import java.util.List;
/**
 * @param movesLog Liste von Zug-Objekten(Move)
 * @param outcome "WIN" or "LOOSE"
 *
 */
public record RoundEndRequest(
        RoundOutcome outcome,
        List<CardMove> movesLog,
        double timeTaken
) {}
