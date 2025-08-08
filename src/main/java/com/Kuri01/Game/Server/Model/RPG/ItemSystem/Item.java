package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;


    @Column(nullable = false)
    private String iconName;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_slot")
    private EquipmentSlotEnum equipmentSlotEnum;

    @ElementCollection(fetch = FetchType.EAGER) // EAGER sorgt dafür, dass die Stats immer direkt mit dem Item geladen werden.
    @CollectionTable(name = "item_stats", joinColumns = @JoinColumn(name = "item_id")) // Definiert die Tabelle für die Stats und die Verknüpfung zur item-Tabelle.
    @MapKeyColumn(name = "stat_name")  // Name der Spalte für den Schlüssel der Map (z.B. "ATTACK").
    @Column(name = "stat_value")       // Name der Spalte für den Wert der Map (z.B. 10).
    private Map<String, Integer> stats = new HashMap<>();


    //Kopie Konstruktor um eine Kopie und keine Refferenz zu erstellen
    public Item(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.rarity = item.getRarity();
        this.iconName = item.getIconName();
    }

    @Override
    public boolean equals(Object o) {
        // 1. Prüfe, ob es sich um dasselbe Objekt im Speicher handelt (schnellster Check)
        if (this == o) return true;

        // 2. Prüfe, ob das Objekt null ist oder von einem anderen Typ stammt
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Jetzt ist der Cast sicher
        Item item = (Item) o;

        // 4. Vergleiche alle relevanten Felder
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(description, item.description) &&
                Objects.equals(rarity, item.rarity) &&
                Objects.equals(iconName, item.iconName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
