package com.Kuri01.Game.Server.Model.RPG;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Inventory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends Character {

    @Column(unique = true)
    private String googleId; // Eindeutige ID vom Google Play Login

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    private int experiencePoints;
    private int level;

    // Jeder Spieler hat genau ein Inventar.
    // cascade = CascadeType.ALL: Wenn ein Spieler gelöscht wird, wird auch sein Inventar gelöscht.
    // orphanRemoval = true: Wenn die Verknüpfung zum Inventar entfernt wird, wird es gelöscht.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    private Inventory inventory;

    public Player() {
        // Beim Erstellen eines neuen Spielers, erstellen wir auch direkt ein leeres Inventar.
        this.inventory = new Inventory(this);
        this.equipment = new Equipment();
    }
}