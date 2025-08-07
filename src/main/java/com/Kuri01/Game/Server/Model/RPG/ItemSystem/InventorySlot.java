package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Inventory inventory;


    private int slotIndex;
    @Column(nullable = false)
    private int quantity = 1;

    //Kopie Konstruktor um eine Kopie und keine Refferenz zu erstellen
    public InventorySlot(InventorySlot inventorySlot) {
        super(inventorySlot);
        this.slotIndex = inventorySlot.slotIndex;
        this.inventory = inventorySlot.inventory;
        this.quantity = inventorySlot.quantity;

    }

    public InventorySlot(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    @Override
    public boolean equals(Object obj) {
        // 1. Prüfe, ob es sich um dasselbe Objekt im Speicher handelt (schnellster Check)
        if (this == obj) return true;

        // 2. Prüfe, ob das Objekt null ist oder von einem anderen Typ stammt
        if (obj == null || getClass() != obj.getClass()) return false;

        InventorySlot slot = (InventorySlot) obj;
        if (this.getId() == null || slot.getId() == null) return false;

        return this.getId() == slot.getId() &&
                this.inventory.getId() == slot.getInventory().getId() &&
                this.slotIndex == slot.getSlotIndex();

    }
}