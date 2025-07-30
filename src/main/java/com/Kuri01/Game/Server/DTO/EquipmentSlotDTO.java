package com.Kuri01.Game.Server.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentSlotDTO {

    //von Oberklasse ItemSlot
    private Long id;
    private ItemDTO item;

    //von Unterklasse InventorySlot
    private EquipmentSlotEnum slotEnum;

}
