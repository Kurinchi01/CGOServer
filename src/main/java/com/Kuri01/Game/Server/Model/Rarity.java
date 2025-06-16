package com.Kuri01.Game.Server.Model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public enum Rarity {
    common(0.5f),
    uncommon(0.3f),
    rare(0.15f),
    epic(0.04f),
    legendary(0.01f);

    @Getter
    private final float chance;
    private static final Random random = new Random();
    private static float cumulativeProbability = 0;

    Rarity(float chance) {
        this.chance = chance;
    }

   public static Rarity getRandomRarity()
   {
       if (cumulativeProbability == 0) {
           for (Rarity drop : values()) {
               cumulativeProbability += drop.getChance();
           }
       }
       float randomValue = random.nextFloat() * cumulativeProbability;
       float cumulative = 0;
       for (Rarity drop : values()) {
           cumulative += drop.getChance();
           if (randomValue < cumulative) {
               return drop;
           }
       }
       //wird nie aufgerufen
       return values()[values().length - 1];
   }
}
