package com.Kuri01.Game.Server.DTO.Card;

public record SolverResult(
        boolean isSolvable,
        int difficulty,
        int longestCombo
) {}
