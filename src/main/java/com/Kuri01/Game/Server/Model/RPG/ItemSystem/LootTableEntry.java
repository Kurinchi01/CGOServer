package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LootTableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Die Truhe, zu der dieser Eintrag gehört.
    @ManyToOne
    @JoinColumn(name = "loot_chest_id")
    private LootChest lootChest;

    // Das Item, das droppen kann.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // Die Wahrscheinlichkeit, dass dieses Item droppt (z.B. 0.5 für 50%)
    @Column(nullable = false)
    private double dropChance;

    // Die Anzahl, die gedroppt wird (z.B. 1-5 Heiltränke)
    private int minQuantity = 1;
    private int maxQuantity = 1;

    public LootTableEntry() {}
}
