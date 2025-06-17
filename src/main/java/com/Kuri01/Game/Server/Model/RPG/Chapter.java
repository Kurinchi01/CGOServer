package com.Kuri01.Game.Server.Model.RPG;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name; // z.B. "Kapitel 1: Der Goblinwald"
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private int monsterCount;

    // Ein Kapitel kann viele verschiedene Monster-Typen enthalten.
    // Ein Monster-Typ (z.B. Goblin) kann in vielen Kapiteln vorkommen.
    // Das ist eine klassische Many-to-Many-Beziehung.
    @ManyToMany(fetch = FetchType.EAGER) // EAGER damit die Monster direkt mitgeladen werden
    @JoinTable(
            name = "chapter_monster_pool", // Name der Zwischentabelle
            joinColumns = @JoinColumn(name = "chapter_id"),
            inverseJoinColumns = @JoinColumn(name = "monster_id"))
    @Getter
    @Setter
    private Set<Monster> monsters;

    // Leerer Konstruktor für JPA
    public Chapter() {
    }

}