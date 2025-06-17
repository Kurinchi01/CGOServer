package com.Kuri01.Game.Server.Model.RPG;

import lombok.Getter;
import java.util.Arrays;
import java.util.Random;

public enum Rarity {
    // Die Reihenfolge von "selten" nach "häufig" ist hier nützlich
    legendary(0.01f),
    epic(0.04f),
    rare(0.15f),
    uncommon(0.3f),
    common(0.5f);

    @Getter
    private final float chance;

    private static final Random random = new Random();

    // Wir berechnen die Summe einmalig und sicher beim Laden der Klasse.
    // 'static final' macht sie zu einer unveränderlichen Konstante.
    private static final double TOTAL_CHANCE = Arrays.stream(values()).mapToDouble(Rarity::getChance).sum();

    Rarity(float chance) {
        this.chance = chance;
    }

    /**
     * Wählt eine zufällige Seltenheit basierend auf den definierten Wahrscheinlichkeiten.
     * Diese Methode ist threadsicher.
     * @return eine zufällige Rarity.
     */
    public static Rarity getRandomRarity() {
        double randomValue = random.nextDouble() * TOTAL_CHANCE; // nextDouble() ist hier besser geeignet
        double cumulative = 0;
        for (Rarity rarity : values()) {
            cumulative += rarity.getChance();
            if (randomValue < cumulative) {
                return rarity;
            }
        }
        return common; // Fallback, sollte theoretisch nie erreicht werden bei korrekten Wahrscheinlichkeiten
    }

    /**
     * Hilfsmethode, um die nächst-seltenere Stufe zu bekommen.
     * @return die nächst-seltenere Rarity, oder die seltenste, wenn bereits am Ende.
     */
    public Rarity getMoreRare() {
        int nextOrdinal = this.ordinal() - 1;
        return (nextOrdinal >= 0) ? values()[nextOrdinal] : this;
    }

    /**
     * Hilfsmethode, um die nächst-häufigere Stufe zu bekommen.
     * @return die nächst-häufigere Rarity, oder die häufigste, wenn bereits am Ende.
     */
    public Rarity getLessRare() {
        int nextOrdinal = this.ordinal() + 1;
        return (nextOrdinal < values().length) ? values()[nextOrdinal] : this;
    }
}
