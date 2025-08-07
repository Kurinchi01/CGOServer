package com.Kuri01.Game.Server.DTO.Action;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlot;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipAction extends PlayerAction {
    private InventorySlot sourceInventorySlot;
    private EquipmentSlot targetEquipmentSlot;  // Die Enum für den Ziel-Slot

    public EquipAction(InventorySlot sourceInventorySlot, EquipmentSlot targetEquipmentSlot) {
        super("EQUIP");
        this.sourceInventorySlot = sourceInventorySlot;
        this.targetEquipmentSlot = targetEquipmentSlot;
    }

}