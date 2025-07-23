package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.DTO.*;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        //erstelle DTO
        PlayerDTO dto = new PlayerDTO();
        //fülle DTO mit player attributen
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setLevel(player.getLevel());
        dto.setExperiencePoints(player.getExperiencePoints());
        dto.setMaxHp(player.getMaxHp());
        dto.setAttack(player.getAttack());

        //maper um DTO mit equipment zu füllen
        dto.setEquipmentDTO(mapToEquipmentDTO(player.getEquipment()));

        //maper um DTO mit iventory zu füllen
        dto.setInventory(mapToInventoryDTO(player.getInventory()));

        return dto;
    }

    private InventoryDTO mapToInventoryDTO(Inventory inventory) {
        if (inventory == null) return null;

        InventoryDTO dto = new InventoryDTO();
        dto.setCapacity(inventory.getCapacity());

        // Wandle nur die belegten Slots in DTOs um, um Daten zu sparen
        List<InventorySlotDTO> slotDTOs = inventory.getSlots().stream()
                .filter(slot -> slot.getItem() != null)
                .map(slot -> {
                    InventorySlotDTO slotDto = new InventorySlotDTO();
                    // WICHTIG: Du musst einen Weg haben, den Index des Slots zu bekommen.
                    slotDto.setQuantity(slot.getQuantity());
                    slotDto.setItem(mapToItemDTO(slot.getItem()));
                    return slotDto;
                })
                .collect(Collectors.toList());

        dto.setSlots(slotDTOs);
        return dto;
    }

    private EquipmentDTO mapToEquipmentDTO(Equipment equipment) {
        if (equipment == null) return null;
        EquipmentDTO dto = new EquipmentDTO();
        dto.setWeapon(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.WEAPON))); // Hier rufen wir mapToItemDTO für ein einzelnes Item auf
        dto.setHelmet(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.HELMET)));
        dto.setArmor(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.ARMOR)));
        dto.setNecklace(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.NECKLACE)));
        dto.setRing(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.RING)));
        dto.setShoes(mapToItemDTO(equipment.getItemInSlot(EquipmentSlotEnum.SHOES)));
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
        dto.setIconName(item.getIconName());
//      dto.setQuantity(1); // Standard-Anzahl für nicht-gestapelte Items

        // Prüfe den spezifischen Typ des Items und setze die entsprechenden Felder.
        // Diese moderne 'instanceof'-Syntax ist sauberer als ein Cast.
        if (item instanceof EquipmentItem equipItem) {
            dto.setItemType("EQUIPMENT");
            dto.setEquipmentSlotEnum(equipItem.getEquipmentSlotEnum());
            dto.setStats(equipItem.getStats());
        } else if (item instanceof LootChest) {
            dto.setItemType("CHEST");
        }
        //  weitere 'else if' für andere Item-Typen folgen ...

        return dto;
    }
}
