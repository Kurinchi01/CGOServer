package com.Kuri01.Game.Server.Model.RPG.ItemSystem;


import lombok.Getter;
import lombok.Setter;

public enum EquipmentSlot {
    WEAPON("Waffe"),
    HELMET("Helm"),
    ARMOR("Rüstung"),
    NECKLACE("Kette"),
    RING("Ring"),
    SHOES("Schuhe");

    @Getter
    private final String displayName;

    @Getter
    @Setter
    private InventorySlot slot;

    EquipmentSlot(String displayName) {
        this.displayName = displayName;
    }

}
