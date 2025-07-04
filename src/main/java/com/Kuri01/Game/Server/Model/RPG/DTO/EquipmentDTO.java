package com.Kuri01.Game.Server.Model.RPG.DTO;

public class EquipmentDTO {
    private ItemDTO weapon;
    private ItemDTO helmet;
    private ItemDTO armor;
    private ItemDTO necklace;
    private ItemDTO ring;
    private ItemDTO shoes;

    public EquipmentDTO() {
    }

    public ItemDTO getShoes() {
        return shoes;
    }

    public void setShoes(ItemDTO shoes) {
        this.shoes = shoes;
    }

    public ItemDTO getRing() {
        return ring;
    }

    public void setRing(ItemDTO ring) {
        this.ring = ring;
    }

    public ItemDTO getNecklace() {
        return necklace;
    }

    public void setNecklace(ItemDTO necklace) {
        this.necklace = necklace;
    }

    public ItemDTO getArmor() {
        return armor;
    }

    public void setArmor(ItemDTO armor) {
        this.armor = armor;
    }

    public ItemDTO getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemDTO helmet) {
        this.helmet = helmet;
    }

    public ItemDTO getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemDTO weapon) {
        this.weapon = weapon;
    }
}
