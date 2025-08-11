package com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwapSlotInventoryAction extends PlayerInventoryAction {

    private  InventorySlot sourceSlot;
    private InventorySlot targetSlot;

    public SwapSlotInventoryAction(InventorySlot sourceSlot, InventorySlot targetSlot) {
        super("SWAP_INVENTORY");
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
    }
}