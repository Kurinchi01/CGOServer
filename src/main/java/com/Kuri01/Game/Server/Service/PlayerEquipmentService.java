package com.Kuri01.Game.Server.Service;


import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentItem;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.ItemRepository;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
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

    @Transactional // Sorgt dafür, dass alle DB-Änderungen als eine Einheit passieren
    public Equipment equipItem(Long playerId, Long itemToEquipId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden"));
        Item item = itemRepository.findById(itemToEquipId).orElseThrow(() -> new IllegalArgumentException("Item nicht gefunden"));

        // 1. Validierung: Ist das Item überhaupt ausrüstbar?
        if (!(item instanceof EquipmentItem equipmentItem)) {
            throw new IllegalArgumentException("Dieses Item kann nicht ausgerüstet werden.");
        }

        // 2. Ist das Item im Inventar des Spielers?
        boolean itemIsInInventory = player.getInventory().getSlots().stream()
                .anyMatch(slot -> slot.getItem() != null && slot.getItem().getId().equals(itemToEquipId));

        if (!itemIsInInventory) {
            // Der wichtigste Teil: Die Anfrage ablehnen!
            throw new IllegalStateException("Spieler versucht, ein Item auszurüsten, das er nicht besitzt.");
        }

        Equipment equipment = player.getEquipment();
        EquipmentSlotEnum targetSlot = equipmentItem.getEquipmentSlotEnum();

        // 3. Altes Item (falls vorhanden) ablegen und ins Inventar legen
        EquipmentItem oldItem = null;
        switch (targetSlot) {
            case WEAPON -> {
                oldItem = equipment.getWeapon();
                equipment.setWeapon(null);
            }
            case HELMET -> {
                oldItem = equipment.getHelmet();
                equipment.setHelmet(null);
            }
            case ARMOR -> {
                oldItem = equipment.getArmor();
                equipment.setArmor(null);
            }
            case NECKLACE -> {
                oldItem = equipment.getNecklace();
                equipment.setNecklace(null);
            }
            case RING -> {
                oldItem = equipment.getRing();
                equipment.setRing(null);
            }
            case SHOES -> {
                oldItem = equipment.getShoes();
                equipment.setShoes(null);
            }

        }
        if (oldItem != null) {
            // TODO: Füge oldItem zum Inventar des Spielers hinzu
            player.getInventory().addItem(oldItem);
        }

        // 4. Neues Item aus dem Inventar entfernen und anlegen
        // TODO: Entferne equipmentItem aus dem Inventar
        // player.getInventory().removeItem(equipmentItem);
        switch (targetSlot) {
            case WEAPON -> equipment.setWeapon(equipmentItem);
            case HELMET -> equipment.setHelmet(equipmentItem);
            // ... weitere cases für alle Slots
        }

        // 5. Änderungen speichern (passiert dank @Transactional automatisch am Ende der Methode)
        playerRepository.save(player);
        return player.getEquipment();
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
