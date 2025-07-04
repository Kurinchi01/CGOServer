package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LootResult {
    private String message;
    private Rarity rarity;

    public LootResult(String message, Rarity rarity) {
        this.message = message;
        this.rarity = rarity;
    }

    public LootResult() {
    }
}
