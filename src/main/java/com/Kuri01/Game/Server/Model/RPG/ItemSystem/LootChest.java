package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("CHEST") // Wert für die 'item_type'-Spalte in der Item-Tabelle
@Getter
@Setter
public class LootChest extends Item {

    // Eine Truhe hat einen Pool von möglichen Items.
    // Jeder Eintrag in diesem Pool hat eine eigene Drop-Chance.
    @OneToMany(mappedBy = "lootChest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LootTableEntry> lootTable;

    public LootChest() {}
}
