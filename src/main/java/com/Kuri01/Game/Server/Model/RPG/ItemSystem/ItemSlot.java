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


}
