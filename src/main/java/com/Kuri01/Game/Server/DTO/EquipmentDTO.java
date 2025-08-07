package com.Kuri01.Game.Server.DTO;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
public class EquipmentDTO {

    private Map<EquipmentSlotEnum,EquipmentSlotDTO> equipmentSlots =  new HashMap<>();

}
