package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.*;
import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;
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
        Player player = playerRepository.findByGoogleId(googleId).orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + googleId));
        ;

        // Hier, innerhalb der Transaktion, mappen wir die Entity auf ein DTO.
        return mapToPlayerDTO(player);
    }

    public PlayerDTO getPlayerProfileJUnitTest(Player player) {
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

        //maper um DTO mit Wallte zu füllen
        dto.setPlayerWalletDTO(mapToPlayerWalletDTO(player.getPlayerWallet()));

        //maper um DTO mit equipment zu füllen
        dto.setEquipmentDTO(mapToEquipmentDTO(player.getEquipment()));

        //maper um DTO mit iventory zu füllen
        dto.setInventoryDTO(mapToInventoryDTO(player.getInventory()));

        return dto;
    }

    private InventoryDTO mapToInventoryDTO(Inventory inventory) {
        if (inventory == null) return null;

        InventoryDTO dto = new InventoryDTO();
        dto.setCapacity(inventory.getCapacity());
        dto.setPlayer(inventory.getPlayer());
        dto.setId(inventory.getId());


        List<InventorySlotDTO> slotDTOs = inventory.getSlots().stream()
                .map(slot -> {

                    assert slot != null;

                    return mapToInventorySlotDTO(slot);

                })
                .collect(Collectors.toList());

        dto.setInventorySlots(slotDTOs);
        return dto;
    }

    private InventorySlotDTO mapToInventorySlotDTO(InventorySlot inventorySlot) {
        InventorySlotDTO tmp = new InventorySlotDTO();

        tmp.setId(inventorySlot.getId());
        tmp.setItem(mapToItemDTO(inventorySlot.getItem()));
        tmp.setSlotIndex(inventorySlot.getSlotIndex());
        tmp.setInventory(inventorySlot.getInventory());

        return tmp;
    }

    private PlayerWalletDTO mapToPlayerWalletDTO(PlayerWallet playerWallet) {
        PlayerWalletDTO playerWalletDTO = new PlayerWalletDTO();

        playerWalletDTO.setCandy(playerWallet.getCandy());
        playerWalletDTO.setGold(playerWallet.getGold());

        return playerWalletDTO;
    }

    private EquipmentDTO mapToEquipmentDTO(Equipment equipment) {
        if (equipment == null) return null;
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        for (EquipmentSlot a : equipment.getEquipmentSlots().values()) {
            EquipmentSlotEnum equipmentSlotEnum = a.getSlotEnum();
            EquipmentSlot equipmentSlot = equipment.getEquipmentSlots().get(equipmentSlotEnum);
            dto.getEquipmentSlots().put(equipmentSlotEnum, mapToEquipmentSlotDTO(equipmentSlot));
        }
        return dto;
    }

    private EquipmentSlotDTO mapToEquipmentSlotDTO(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == null) return null;
        EquipmentSlotDTO equipmentSlotDTO = new EquipmentSlotDTO();
        equipmentSlotDTO.setId(equipmentSlot.getId());
        equipmentSlotDTO.setItem(mapToItemDTO(equipmentSlot.getItem()));
        equipmentSlotDTO.setSlotEnum(equipmentSlot.getSlotEnum());
        return equipmentSlotDTO;
    }


    private ItemDTO mapToItemDTO(Item item) {
        if (item == null) {
            return null;
        }
        ItemDTO dto = new ItemDTO();


        // Prüfe den spezifischen Typ des Items und setze die entsprechenden Felder.
        // Diese moderne 'instanceof'-Syntax ist sauberer als ein Cast.
        if (item instanceof EquipmentItem equipItem) {
            dto.setItemType("EQUIPMENT");
            dto.setEquipmentSlotEnum(equipItem.getEquipmentSlotEnum());
            dto.setStats(equipItem.getStats());
        } else if (item instanceof LootChest) {
            dto.setItemType("CHEST");
        }


        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setRarity(item.getRarity());
        dto.setIconName(item.getIconName());
        dto.setQuantity(item.getQuantity());

        return dto;
    }
}
