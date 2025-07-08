package com.Kuri01.Game.Server.Model.RPG.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EquipmentDTO {
    private ItemDTO weapon;
    private ItemDTO helmet;
    private ItemDTO armor;
    private ItemDTO necklace;
    private ItemDTO ring;
    private ItemDTO shoes;

    public EquipmentDTO() {
    }

}
