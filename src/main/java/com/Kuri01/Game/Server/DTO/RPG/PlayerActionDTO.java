package com.Kuri01.Game.Server.DTO.RPG;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerActionDTO {
    String actionType;
    InventorySlotDTO sourceInventorySlotDTO;
    InventorySlotDTO targetInventorySlotDTO;
    EquipmentSlotDTO sourceEquipmentSlotDTO;
    EquipmentSlotDTO targetEquipmentSlotDTO;

/// Redundanter Code??
//    public PlayerActionDTO(EquipmentSlotEnum sourceEquipmentSlotEnum, InventorySlot inventorySlot) {
//        this.actionType = "UNEQUIP";
//        this.sourceEquipmentSlotEnum = sourceEquipmentSlotEnum;
//        this.targetInventorySlotDTO = ModelFactory.createDTOFromInventorySlot(inventorySlot);
//    }
//
//    public PlayerActionDTO(InventorySlot sourInventorySlot, EquipmentSlotEnum targetEquipmentSlotEnum) {
//        this.actionType = "EQUIP";
//        this.sourceInventorySlotDTO = ModelFactory.createDTOFromInventorySlot(sourInventorySlot);
//        this.targetEquipmentSlotEnum = targetEquipmentSlotEnum;
//    }
//
//    public PlayerActionDTO(InventorySlot sourInventorySlot, InventorySlot inventorySlot) {
//        this.actionType = "SWAP_INVENTORY";
//        this.sourceInventorySlotDTO = ModelFactory.createDTOFromInventorySlot(sourInventorySlot);
//        this.targetInventorySlotDTO = ModelFactory.createDTOFromInventorySlot(inventorySlot);
//    }


}
