package com.Kuri01.Game.Server.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

/**
 * Data Transfer Object für die Spielerdaten
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
    private HashMap<Long, ItemDTO> itemBlueprints;

}
