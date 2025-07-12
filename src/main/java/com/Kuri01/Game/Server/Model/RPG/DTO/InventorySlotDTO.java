package com.Kuri01.Game.Server.Model.RPG.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Ein DTO für einen einzelnen Slot
@Getter
@Setter
@NoArgsConstructor
public class InventorySlotDTO {
    private int slotIndex; // Der Index des Slots (0, 1, 2...)
    private ItemDTO item; // Das Item im Slot (kann null sein)
    private int quantity;

}
