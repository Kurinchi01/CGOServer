package com.Kuri01.Game.Server.Model.RPG.DTO;

/**
 * Data Transfer Object für die Spielerdaten, die zum Start einer Runde benötigt werden.
 */
public class PlayerDTO {

    // --- Basis-Charakterwerte ---
    private Long id;
    private String name;
    private int level;
    private int experiencePoints;
    private float maxHp; // Die maximalen HP, die der Server sendet.
    private float attack;

    // --- Zusammengesetzte Objekte ---
    private EquipmentDTO equipment;
    private List<ItemDTO> inventoryItems;

    // --- Leerer Konstruktor & Getter/Setter für alle Felder ---
    // Das Feld currentHp fehlt hier, da es vom Server nicht gesendet wird.

    public PlayerDTO() {}

    // ... Getter und Setter für die oben genannten Felder ...
}
