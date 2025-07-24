package com.Kuri01.Game.Server.Model.RPG.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentSlotDTO {
    private Equipment equipment; // Gehört zum Equipment, nicht direkt zum Player
    private EquipmentSlotEnum slotEnum;

}
