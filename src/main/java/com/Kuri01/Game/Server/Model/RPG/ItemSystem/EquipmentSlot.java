package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EquipmentSlot extends ItemSlot {

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment; // Gehört zum Equipment, nicht direkt zum Player

    @Enumerated(EnumType.STRING)
    private EquipmentSlotEnum slotEnum;

    public EquipmentSlot(Equipment equipment, EquipmentSlotEnum slotEnum) {
        this.equipment = equipment;
        this.slotEnum = slotEnum;
    }


}