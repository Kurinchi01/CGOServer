package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("EQUIPMENT")
@Getter
@Setter
public class EquipmentItem extends Item {

    // Dieses Feld ist jetzt entscheidend.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentSlot equipmentSlot;


}
