package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Erlaubt uns später, spezielle Item-Typen zu haben
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
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
        return id == item.id &&
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
