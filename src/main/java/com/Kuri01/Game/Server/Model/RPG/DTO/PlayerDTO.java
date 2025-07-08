package com.Kuri01.Game.Server.Model.RPG.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object für die Spielerdaten, die zum Start einer Runde benötigt werden.
 */
@Setter
@Getter
public class PlayerDTO {

    // --- Basis-Charakterwerte ---
    private Long id;
    private String name;
    private int level;
    private int experiencePoints;
    private float maxHp; // Die maximalen HP, die der Server sendet.
    private float attack;

    // --- Zusammengesetzte Objekte ---
    private Equipment equipment;
    private List<ItemDTO> inventoryItems;

    // --- Leerer Konstruktor & Getter/Setter für alle Felder ---

    public PlayerDTO() {}

}
