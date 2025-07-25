package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InventorySlot {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    // Ein Slot kann ein Item enthalten (oder null sein).
    @ManyToOne
    @Getter
    private Item item;

    private int quantity;

    private int slotIndex;

    public InventorySlot(Inventory inventory) {
        this.inventory = inventory;
    }
}