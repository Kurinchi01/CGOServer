package com.Kuri01.Game.Server.Model.RPG;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "character")
@DiscriminatorColumn(name = "character_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float maxHp;
    private float attack;
    @Column(nullable = true)
    private float chargeRate=5f;


    protected Character() {
    }

    public Character(String name, float maxHp, float attack) {
        this.name = name;
        this.maxHp = maxHp;
        this.attack = attack;
        chargeRate = 5;

    }

}
