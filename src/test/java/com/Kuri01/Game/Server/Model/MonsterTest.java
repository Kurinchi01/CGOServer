package com.Kuri01.Game.Server.Model;

import com.Kuri01.Game.Server.Model.RPG.Monster;
import com.Kuri01.Game.Server.Model.RPG.Rarity;
import org.junit.jupiter.api.Test;

class MonsterTest {

    public int cCount, ucCount, rCount, eCount, lCount = 0;

    @Test
    public void monsterGenerator() {
        float sum=100000f;
        for (int i = 0; i < sum; i++) {
            Monster monster = new Monster("Monster" + i, 100, 5, Rarity.common);
            monster.setRarity(Rarity.getRandomRarity());

            switch (monster.getRarity()) {
                case common -> cCount++;
                case uncommon -> ucCount++;
                case rare -> rCount++;
                case epic -> eCount++;
                case legendary -> lCount++;
            }

        }

        System.out.println("Common: " + cCount + " " + cCount / sum * 100 + "%");
        System.out.println("Uncommon: " + ucCount + " " + ucCount / sum * 100 + "%");
        System.out.println("Rare: " + rCount + " " + rCount / sum * 100 + "%");
        System.out.println("Epic: " + eCount + " " + eCount / sum * 100 + "%");
        System.out.println("Legendary: " + lCount + " " + lCount / sum * 100 + "%");
        cCount= 0;
        ucCount= 0;
        rCount= 0;
        eCount= 0;
        lCount = 0;
    }


    @Test
    public void multipleMonsterGenerator() {
        for (int i = 0; i < 100; i++) {
            this.monsterGenerator();
            System.out.println();
            System.out.println("Neue Generierung:");
        }
    }
}