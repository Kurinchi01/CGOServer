package com.Kuri01.Game.Server.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

import static com.Kuri01.Game.Server.Model.Rarity.closest;

@Entity
public class Monster extends Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private Rarity rarity;
    Random r;


    // Wichtig für JPA: ein leerer Konstruktor
    protected Monster() {
    }


    public Monster(String name, float maxHP, float attack) {
        super(name, maxHP, attack);
        r = new Random();
        this.rarity = closest(r.nextFloat()).get();

    }

}