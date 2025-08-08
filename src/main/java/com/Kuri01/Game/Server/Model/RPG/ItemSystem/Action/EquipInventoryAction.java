package com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlot;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipInventoryAction extends PlayerInventoryAction {
    private InventorySlot sourceInventorySlot;
    private EquipmentSlot targetEquipmentSlot;  // Die Enum für den Ziel-Slot

    public EquipInventoryAction(InventorySlot sourceInventorySlot, EquipmentSlot targetEquipmentSlot) {
        super("EQUIP");
        this.sourceInventorySlot = sourceInventorySlot;
        this.targetEquipmentSlot = targetEquipmentSlot;
    }

}