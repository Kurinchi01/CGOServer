package com.Kuri01.Game.Server.Model;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Character {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    public float getAttack() {
        return attack;
    }

    public void setAttack(float attack) {
        this.attack = attack;
    }

    public float getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(float chargeRate) {
        this.chargeRate = chargeRate;
    }
}
