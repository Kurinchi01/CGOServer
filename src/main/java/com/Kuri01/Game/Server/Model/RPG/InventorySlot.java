package com.Kuri01.Game.Server.Model.RPG;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InventorySlot {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    // Ein Slot kann ein Item enthalten (oder null sein).
    // TODO: Hier würdest du eine Verknüpfung zu deiner Item-Entity einfügen.
    // @ManyToOne
    // private Item item;

    private int quantity;

    public InventorySlot() {}

    public InventorySlot(Inventory inventory) {
        this.inventory = inventory;
    }
}