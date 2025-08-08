package com.Kuri01.Game.Server.Service;


import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.ItemRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerEquipmentService {

    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;

    public PlayerEquipmentService(PlayerRepository playerRepository, ItemRepository itemRepository) {
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
    }


    @Transactional
    public Equipment unequipItem(Long playerId, EquipmentSlotEnum slotToUnequip) {
        // Ähnliche Logik wie oben, nur in die andere Richtung...
        // 1. Spieler und Equipment laden
        // 2. Item aus dem Slot holen
        // 3. Item ins Inventar legen
        // 4. Slot im Equipment auf null setzen
        // 5. Spieler speichern
        return null; // Platzhalter
    }
}
