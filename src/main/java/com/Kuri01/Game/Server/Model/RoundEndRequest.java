package com.Kuri01.Game.Server.Model;
import com.Kuri01.Game.Server.Model.Cards.Move;

import java.util.List;
/**
 * @param movesLog Liste von Zug-Objekten(Move)
 * @param outcome "WIN" or "LOOSE"
 *
 */
public record RoundEndRequest(
        RoundOutcome outcome,
        List<Move> movesLog,
        double timeTaken
) {}
