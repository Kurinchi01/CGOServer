package com.Kuri01.Game.Server.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;



@Entity
public class Monster extends Character {

    @Setter
    @Getter
    @Enumerated
    private Rarity rarity;

    
    // Wichtig für JPA: ein leerer Konstruktor
    protected Monster() {
    }


    public Monster(String name, float maxHP, float attack, Rarity rarity) {
        super(name, maxHP, attack);
        this.rarity = rarity;
    }


}