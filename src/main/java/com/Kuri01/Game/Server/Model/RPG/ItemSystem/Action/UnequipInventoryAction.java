package com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlot;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnequipInventoryAction extends PlayerInventoryAction {
    private EquipmentSlot sourceEquipmentSlot; // Die Enum für den Quell-Slot
    private InventorySlot targetInventorySlot;

    public UnequipInventoryAction(EquipmentSlot sourceEquipmentSlot, InventorySlot targetInventorySlot) {
        super("UNEQUIP");
        this.sourceEquipmentSlot = sourceEquipmentSlot;
        this.targetInventorySlot = targetInventorySlot;
    }
}