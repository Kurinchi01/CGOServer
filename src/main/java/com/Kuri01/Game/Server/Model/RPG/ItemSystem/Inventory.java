package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Verknüpfung zurück zum Spieler
    @OneToOne(mappedBy = "inventory")
    private Player player;

    // Ein Inventar hat viele Slots.
    @Getter
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventorySlot> slots = new ArrayList<>();

    public Inventory() {}

    public Inventory(Player player) {
        this.player = player;
        // z.B. 20 Start-Slots erstellen
        for (int i = 0; i < 20; i++) {
            this.slots.add(new InventorySlot(this));
        }
    }


    public void addItem(EquipmentItem newItem) {
        int index = hasSpace();
        if (index!=-1)
        {
            slots.get(index).setItem(newItem);
        }
        throw new IllegalStateException("Das Inventar ist voll!");
    }

    private int hasSpace()
    {
        for(int i=0;i< slots.size();i++)
        {
            if (slots.get(i).getItem()==null)
            {
                return i;
            }
        }
        return -1;
    }
}

