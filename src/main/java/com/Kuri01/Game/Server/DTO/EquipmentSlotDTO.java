package com.Kuri01.Game.Server.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentSlotDTO {

    //ItemID für Referenz auf Item
    private Long itemID;

    //von Unterklasse InventorySlot
    private EquipmentSlotEnum slotEnum;

}
