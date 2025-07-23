package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert ein ausrüstbares Item, das von der Basisklasse Item erbt.
 * Diese Entität wird in derselben Tabelle wie 'Item' gespeichert (Single-Table-Strategie)
 * und durch den Discriminator-Wert "EQUIPMENT" unterschieden.
 */
@Entity
@DiscriminatorValue("EQUIPMENT")
@Getter
@Setter
@NoArgsConstructor
public class EquipmentItem extends Item {

    /**
     * Definiert den spezifischen Slot, in den dieser Gegenstand ausgerüstet werden kann.
     * @Enumerated(EnumType.STRING) sorgt dafür, dass der Name des Enums (z.B. "WEAPON")
     * in der Datenbank gespeichert wird, was lesbarer ist als der numerische Index.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_slot")
    private EquipmentSlotEnum equipmentSlotEnum;

    /**
     * @ElementCollection weist JPA an, diese Sammlung in einer separaten Tabelle zu verwalten,
     * da eine Map nicht direkt in einer einzelnen Spalte gespeichert werden kann.
     */
    @ElementCollection(fetch = FetchType.EAGER) // EAGER sorgt dafür, dass die Stats immer direkt mit dem Item geladen werden.
    @CollectionTable(name = "item_stats", joinColumns = @JoinColumn(name = "item_id")) // Definiert die Tabelle für die Stats und die Verknüpfung zur item-Tabelle.
    @MapKeyColumn(name = "stat_name")  // Name der Spalte für den Schlüssel der Map (z.B. "ATTACK").
    @Column(name = "stat_value")       // Name der Spalte für den Wert der Map (z.B. 10).
    private Map<String, Integer> stats = new HashMap<>();

}
