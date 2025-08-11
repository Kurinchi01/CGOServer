package com.Kuri01.Game.Server.DTO.RPG;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Ein DTO für einen einzelnen Slot
@Getter
@Setter
@NoArgsConstructor
public class InventorySlotDTO {
    //von Oberklasse ItemSlot
    private Long itemID;

    //von Unterklasse InventorySlot
    private int slotIndex;
    private int quantity;

}
