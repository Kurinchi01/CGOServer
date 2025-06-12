package com.Kuri01.Game.Server.Model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public enum Rarity {
    common(0.5f),
    uncommon(0.3f),
    rare(0.2f),
    epic(0.1f),
    legendary(0.002f);

    private final float chance;

    Rarity(float f) {
        this.chance = f;
    }

    public static Optional<Rarity> closest(float f) {
        return Arrays.stream(Rarity.values())
                .min(Comparator.comparingDouble(type -> Math.abs(type.chance - f)));
    }
}
