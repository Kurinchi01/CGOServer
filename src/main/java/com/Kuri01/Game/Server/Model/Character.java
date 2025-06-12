package com.Kuri01.Game.Server.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float maxHp;
    private float attack;
    private float chargeRate;//oder auch atkcooldown auf client-side


    protected Character() {
    }

    public Character(String name, float maxHp, float attack) {
        this.name = name;
        this.maxHp = maxHp;
        this.attack = attack;
        chargeRate = 5;

    }

}
