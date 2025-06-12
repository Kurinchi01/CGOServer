package com.Kuri01.Game.Server.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    public int cCount, ucCount, rCount, eCount, lCount = 0;
    @Test
    public void monsterGenerator()
    {
        for (int i = 0; i < 1000; i++) {
//          Monster monster = new Monster("Monster" + i, 100, 5);
//            switch (monster.getRarity())
//            {
//                case common -> cCount++;
//                case uncommon -> ucCount++;
//                case rare -> rCount++;
//                case epic -> eCount++;
//                case legendary -> lCount++;
//            }
//
        }

        System.out.println("Common: "+cCount +" " +cCount/1000f*100+"%");
        System.out.println("Uncommon: "+ucCount+" " +ucCount/1000f*100+"%");
        System.out.println("Rare: "+rCount+" " +rCount/1000f*100+"%");
        System.out.println("Epic: "+eCount+" " +eCount/1000f*100+"%");
        System.out.println("Legendary: "+lCount+" " +lCount/1000f*100+"%");
    }
}