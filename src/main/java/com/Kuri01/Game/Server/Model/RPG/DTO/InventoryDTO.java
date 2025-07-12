package com.Kuri01.Game.Server.Model.RPG.DTO;

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
    private List<InventorySlotDTO> slots; // Die Liste der belegten Slots

}
