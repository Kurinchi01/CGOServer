package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Player;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "equipment")
    @JsonBackReference
    private Player player;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @MapKeyEnumerated(EnumType.STRING)
    @JsonManagedReference
    private Map<EquipmentSlotEnum, EquipmentSlot> equipmentSlots = new HashMap<>();

    public Equipment() {
        this.player = player;
        // Erstelle beim Erstellen des Equipment-Sets für jeden Enum-Wert einen leeren Slot.
        for (EquipmentSlotEnum slotEnum : EquipmentSlotEnum.values()) {
            this.equipmentSlots.put(slotEnum, new EquipmentSlot(this, slotEnum));
        }
    }

    public Item getItemInSlot(EquipmentSlotEnum slotEnum) {
        EquipmentSlot slot = this.equipmentSlots.get(slotEnum);
        return (slot != null) ? slot.getItem() : null;
    }

    public void setItemInSlot(EquipmentSlotEnum slotEnum, Item item) {
        this.equipmentSlots.get(slotEnum).setItem(item);
    }

    public void addSlot(EquipmentSlot slot) {
        this.equipmentSlots.put(slot.getSlotEnum(), slot);
        slot.setEquipment(this);
    }

}
