package com.Kuri01.Game.Server.Model.RPG.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlot;
import com.Kuri01.Game.Server.Model.RPG.Rarity;

import java.util.Map;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Rarity rarity;
    private String itemType;
    private String iconName;
    private int quantity;
    private EquipmentSlot equipmentSlot; // Nur für EquipmentItems relevant
    private Map<String, Integer> stats;  // Nur für EquipmentItems relevant

    public ItemDTO() {
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    public void setStats(Map<String, Integer> stats) {
        this.stats = stats;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public void setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
