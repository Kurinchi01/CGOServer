package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InventorySlot extends ItemSlot {

    // Die Felder 'id' und 'item' werden von ItemSlot geerbt

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private int quantity = 1;
    private int slotIndex;

    public InventorySlot(int slotIndex) {
        this.slotIndex = slotIndex;
    }
}