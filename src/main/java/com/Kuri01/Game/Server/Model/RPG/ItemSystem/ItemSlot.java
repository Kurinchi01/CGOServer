package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // <-- Wichtig: Legt die Vererbungsstrategie fest
@Getter
@Setter
@NoArgsConstructor
public abstract class ItemSlot { // Machen Sie die Basisklasse abstrakt

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item;

    //Kopie Konstruktor um eine Kopie und keine Refferenz zu erstellen
    public ItemSlot(ItemSlot itemSlot) {
        this.item = itemSlot.getItem() != null ? new Item(itemSlot.getItem()) : null;
        this.id = itemSlot.getId();
    }

}
