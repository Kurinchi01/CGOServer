package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.DTO.EquipmentDTO;
import com.Kuri01.Game.Server.Model.RPG.DTO.ItemDTO;
import com.Kuri01.Game.Server.Model.RPG.DTO.PlayerDTO;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public PlayerDTO getPlayerProfile(String googleId) {
        Player player = playerRepository.findByGoogleId(googleId).orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + googleId));;

        // Hier, innerhalb der Transaktion, mappen wir die Entity auf ein DTO.
        return mapToPlayerDTO(player);
    }

    private PlayerDTO mapToPlayerDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setLevel(player.getLevel());
        dto.setExperiencePoints(player.getExperiencePoints());
        dto.setMaxHp(player.getMaxHp());
        dto.setAttack(player.getAttack());

        // KORRIGIERT: Rufe die neue Mapper-Methode für das Equipment auf.
        dto.setEquipmentDTO(mapToEquipmentDTO(player.getEquipment()));

        // KORRIGIERT: Streame über die Slots, nicht über die Items, um die 'quantity' zu erhalten.
        // Und filtere leere Slots heraus.
        if (player.getInventory() != null && player.getInventory().getSlots() != null) {
            List<ItemDTO> inventoryItems = player.getInventory().getSlots().stream()
                    .filter(slot -> slot.getItem() != null) // Ignoriere leere Inventarplätze
                    .map(this::mapToItemDTO) // Übergib den ganzen Slot an den Mapper
                    .collect(Collectors.toList());
            dto.setInventoryItemsDTO(inventoryItems);
        } else {
            dto.setInventoryItemsDTO(Collections.emptyList());
        }

        return dto;
    }

    private EquipmentDTO mapToEquipmentDTO(Equipment equipment) {
        if (equipment == null) return null;
        EquipmentDTO dto = new EquipmentDTO();
        dto.setWeapon(mapToItemDTO(equipment.getWeapon())); // Hier rufen wir mapToItemDTO für ein einzelnes Item auf
        dto.setHelmet(mapToItemDTO(equipment.getHelmet()));
        dto.setArmor(mapToItemDTO(equipment.getArmor()));
        dto.setNecklace(mapToItemDTO(equipment.getNecklace()));
        dto.setRing(mapToItemDTO(equipment.getRing()));
        dto.setShoes(mapToItemDTO(equipment.getShoes()));
        return dto;
    }

    private ItemDTO mapToItemDTO(InventorySlot slot) {
        if (slot == null || slot.getItem() == null) {
            return null;
        }
        ItemDTO dto = mapToItemDTO(slot.getItem()); // Ruft die untere Methode auf
//        if (dto != null) {
//            dto.setQuantity(slot.getQuantity()); // Setze die Anzahl aus dem Slot
//        }
        return dto;
    }
    private ItemDTO mapToItemDTO(Item item) {
        if (item == null) {
            return null;
        }
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setRarity(item.getRarity());
//      dto.setIconName(item.getIconName()); // Wichtiges, fehlendes Feld
//      dto.setQuantity(1); // Standard-Anzahl für nicht-gestapelte Items

        // Prüfe den spezifischen Typ des Items und setze die entsprechenden Felder.
        // Diese moderne 'instanceof'-Syntax ist sauberer als ein Cast.
        if (item instanceof EquipmentItem equipItem) {
            dto.setItemType("EQUIPMENT");
            dto.setEquipmentSlot(equipItem.getEquipmentSlot());
            dto.setStats(equipItem.getStats());
        } else if (item instanceof LootChest) {
            dto.setItemType("CHEST");
        }
        //  weitere 'else if' für andere Item-Typen folgen ...

        return dto;
    }
}
