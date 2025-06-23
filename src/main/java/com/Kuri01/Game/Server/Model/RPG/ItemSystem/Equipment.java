package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Wir definieren für jeden Slot eine Verknüpfung zu einem Item.
    // @OneToOne bedeutet: Ein Equipment-Set hat höchstens eine Waffe.
    @OneToOne
    @JoinColumn(name = "weapon_item_id")
    private EquipmentItem weapon;

    @OneToOne
    @JoinColumn(name = "helmet_item_id")
    private EquipmentItem helmet;

    @OneToOne
    @JoinColumn(name = "armor_item_id")
    private EquipmentItem armor;

    @OneToOne
    @JoinColumn(name = "necklace_item_id")
    private EquipmentItem necklace;

    @OneToOne
    @JoinColumn(name = "ring_item_id")
    private EquipmentItem ring;

    @OneToOne
    @JoinColumn(name = "shoes_item_id")
    private EquipmentItem shoes;

    public Equipment() {}
}
