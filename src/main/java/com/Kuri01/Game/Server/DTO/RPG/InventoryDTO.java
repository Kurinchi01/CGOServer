package com.Kuri01.Game.Server.DTO.RPG;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Ein DTO für das gesamte Inventar
@Getter
@Setter
@NoArgsConstructor
public class InventoryDTO {

    private int capacity; // Die Gesamtgröße des Inventars
    private List<InventorySlotDTO> inventorySlots; // Die Liste der belegten Slots

}
