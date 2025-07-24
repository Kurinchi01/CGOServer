package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EquipmentSlot> equipmentSlots = new HashSet<>();


    public Equipment() {
        // Erstelle beim Erstellen des Equipment-Sets für jeden Enum-Wert einen leeren Slot.
        for (EquipmentSlotEnum slotEnum : EquipmentSlotEnum.values()) {
            this.equipmentSlots.add(new EquipmentSlot(this, slotEnum));
        }
    }

    public Item getItemInSlot(EquipmentSlotEnum slotEnum) {
        return this.equipmentSlots.stream()
                .filter(slot -> slot.getSlotEnum() == slotEnum)
                .findFirst()
                .map(ItemSlot::getItem) // Gibt das Item oder null zurück
                .orElse(null);
    }

    public void setItemInSlot(EquipmentSlotEnum slotEnum, Item item) {
        EquipmentSlot tmp = equipmentSlots.stream().filter(slot -> slot.getSlotEnum() == slotEnum).findFirst().orElseThrow(() -> new IllegalStateException("Kein Equipment-Slot vom Typ " + slotEnum + " gefunden."));

        if (tmp != null) {
            tmp.setItem(item);
        }
    }


}
