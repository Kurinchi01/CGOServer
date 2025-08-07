package com.Kuri01.Game.Server.DTO.Action;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwapInvAction extends PlayerAction {

    private  InventorySlot sourceSlot;
    private InventorySlot targetSlot;

    public  SwapInvAction(InventorySlot sourceSlot, InventorySlot targetSlot) {
        super("SWAP_INVENTORY");
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
    }
}