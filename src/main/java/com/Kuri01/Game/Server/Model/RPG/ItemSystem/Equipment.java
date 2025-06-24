package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    // Wir definieren für jeden Slot eine Verknüpfung zu einem Item.
    // @OneToOne bedeutet: Ein Equipment-Set hat höchstens eine Waffe.
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "weapon_item_id")
    private EquipmentItem weapon;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "helmet_item_id")
    private EquipmentItem helmet;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "armor_item_id")
    private EquipmentItem armor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "necklace_item_id")
    private EquipmentItem necklace;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ring_item_id")
    private EquipmentItem ring;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shoes_item_id")
    private EquipmentItem shoes;

}
