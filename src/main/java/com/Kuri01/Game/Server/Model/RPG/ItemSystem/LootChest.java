package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class LootChest {

    @Id
    Long id;

    Rarity rarity;
    // Eine Truhe hat einen Pool von möglichen Items.
    // Jeder Eintrag in diesem Pool hat eine eigene Drop-Chance.
    @OneToMany(mappedBy = "lootChest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LootTableEntry> lootTable;

    public LootChest() {
    }
}
