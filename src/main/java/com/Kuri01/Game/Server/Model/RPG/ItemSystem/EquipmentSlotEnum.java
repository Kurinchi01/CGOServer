package com.Kuri01.Game.Server.Model.RPG.ItemSystem;


import lombok.Getter;

public enum EquipmentSlotEnum {
    WEAPON("Waffe"),
    HELMET("Helm"),
    ARMOR("Rüstung"),
    NECKLACE("Kette"),
    RING("Ring"),
    SHOES("Schuhe");

    @Getter
    private final String displayName;


    EquipmentSlotEnum(String displayName) {
        this.displayName = displayName;
    }

}
