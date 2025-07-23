package com.Kuri01.Game.Server.Model.RPG.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.Rarity;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Rarity rarity;
    private String itemType;
    private EquipmentSlotEnum equipmentSlotEnum; // Nur für EquipmentItems relevant
    private Map<String, Integer> stats;
    private int quantity;
    private String iconName;

    // --- Leerer Konstruktor ---
    public ItemDTO() {
    }

}
