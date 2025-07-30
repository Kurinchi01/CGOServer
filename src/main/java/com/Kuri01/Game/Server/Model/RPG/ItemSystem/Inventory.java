package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.ItemRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inventory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Verknüpfung zurück zum Spieler
    @OneToOne(mappedBy = "inventory")
    @JsonBackReference
    private Player player;

    private int capacity;


    // Ein Inventar hat viele Slots.
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InventorySlot> slots = new ArrayList<>();

    public Inventory(Player player, int initialSize) {
        this.player = player;
        this.capacity = initialSize;
        for (int i = 0; i < initialSize; i++) {

            addSlot(new InventorySlot(i));
        }
    }


    public boolean addItem(EquipmentItem newItem) {
        int index = findFirstEmptySlotIndex();
        if (index != -1) {
            slots.get(index).setItem(newItem);
            return true;
        } else {
            System.out.println("Inventar ist voll. Item konnte nicht hinzugefügt werden.");
            return false;
        }
    }

    private int findFirstEmptySlotIndex() {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getItem() == null) {
                return i;
            }
        }
        return -1;
    }

    //Füge den Slot hinzu und setz dem Slot die Rück-Referenz zum Inventory
    public void addSlot(InventorySlot slot) {
        this.slots.add(slot);
        slot.setInventory(this);
    }


    //Annahme feld capacity gesetzt, wird nur aufgerufen um neuen Spieler zu erstellen
    public void fillSlots() {
        for (int i = 0; i < capacity; i++) {
            InventorySlot tmp = new InventorySlot(i);
            addSlot(tmp);
        }
    }

    public void setItemToSlot(int slotIndex, Item item) {
        if (item != null) {
            this.slots.get(slotIndex).setItem(item);
        }
    }
}

