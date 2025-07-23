package com.Kuri01.Game.Server.Model.RPG.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object für die Spielerdaten, die zum Start einer Runde benötigt werden.
 */
@Setter
@Getter
@NoArgsConstructor
public class PlayerDTO {

    // --- Basis-Charakterwerte ---
    private Long id;
    private String name;
    private int level;
    private int experiencePoints;
    private float maxHp; // Die maximalen HP, die der Server sendet.
    private float attack;

    // --- Zusammengesetzte Objekte ---
    private EquipmentDTO equipmentDTO;
    private InventoryDTO inventoryDTO;
    private PlayerWalletDTO playerWalletDTO;

}
