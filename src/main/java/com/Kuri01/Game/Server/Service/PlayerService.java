package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.*;
import com.Kuri01.Game.Server.DTO.Action.EquipAction;
import com.Kuri01.Game.Server.DTO.Action.PlayerAction;
import com.Kuri01.Game.Server.DTO.Action.SwapInvAction;
import com.Kuri01.Game.Server.DTO.Action.UnequipAction;
import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.ItemRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;

    public PlayerService(PlayerRepository playerRepository, ItemRepository itemRepository) {
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public PlayerDTO getPlayerProfile(String googleId) {
        Player player = playerRepository.findByGoogleId(googleId).orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + googleId));
        ;

        // Hier, innerhalb der Transaktion, mappen wir die Entity auf ein DTO.
        return createDTOFromPlayer(player);
    }

    public PlayerDTO getPlayerProfileJUnitTest(Player player) {
        return createDTOFromPlayer(player);
    }

    private PlayerDTO createDTOFromPlayer(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();

        //setze single werte
        playerDTO.setId(player.getId());
        playerDTO.setName(player.getName());
        playerDTO.setLevel(player.getLevel());
        playerDTO.setMaxHp(player.getMaxHp());
        playerDTO.setAttack(player.getAttack());

        //setze gekapseltewerte
        playerDTO.setEquipmentDTO(createDTOFromEquipment(player.getEquipment()));
        playerDTO.setInventoryDTO(createDTOFromInventory(player.getInventory()));
        playerDTO.setPlayerWalletDTO(createDTOFromWallet(player.getPlayerWallet()));


        playerDTO.setItemBlueprints(createItemBluePrintFromServer(itemRepository.findAll()));

        return playerDTO;
    }

    private Map<Long, ItemDTO> createItemBluePrintFromServer(List<Item> all) {
        Map<Long, ItemDTO> bluePrints = new HashMap<>();
        for (Item a : all) {
            bluePrints.put(a.getId(), createDTOFromItem(a));
        }

        return bluePrints;
    }

    private ItemDTO createDTOFromItem(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        //setze single werte
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setRarity(item.getRarity());
        itemDTO.setIconName(item.getIconName());

        itemDTO.setEquipmentSlotEnum(item.getEquipmentSlotEnum());
        itemDTO.setStats(item.getStats());

        return itemDTO;
    }

    private PlayerWalletDTO createDTOFromWallet(PlayerWallet playerWallet) {
        PlayerWalletDTO playerWalletDTO = new PlayerWalletDTO();

        playerWalletDTO.setCandy(playerWallet.getCandy());
        playerWalletDTO.setGold(playerWallet.getGold());


        return playerWalletDTO;
    }

    private InventoryDTO createDTOFromInventory(Inventory inventory) {
        InventoryDTO inventoryDTO = new InventoryDTO();

        inventoryDTO.setCapacity(inventory.getCapacity());
        inventoryDTO.setInventorySlots(createDTOFromInventorySlot(inventory.getSlots()));

        return inventoryDTO;
    }

    private List<InventorySlotDTO> createDTOFromInventorySlot(List<InventorySlot> slots) {
        List<InventorySlotDTO> list = new ArrayList<>();
        for (InventorySlot a : slots) {
            InventorySlotDTO tmp = new InventorySlotDTO();
            tmp.setSlotIndex(a.getSlotIndex());
            tmp.setQuantity(a.getQuantity());
            if (a.getItem() != null)
                tmp.setItemID(a.getItem().getId());
            else tmp.setItemID(null);
            list.add(tmp);
        }

        return list;
    }

    private EquipmentDTO createDTOFromEquipment(Equipment equipment) {
        EquipmentDTO equipmentDTO = new EquipmentDTO();

        equipmentDTO.setEquipmentSlots(createDTOFromEquipmentSlot(equipment.getEquipmentSlots()));

        return equipmentDTO;
    }

    private Map<EquipmentSlotEnum, EquipmentSlotDTO> createDTOFromEquipmentSlot(Map<EquipmentSlotEnum, EquipmentSlot> equipmentSlots) {
        Map<EquipmentSlotEnum, EquipmentSlotDTO> map = new HashMap<>();

        for (Map.Entry<EquipmentSlotEnum, EquipmentSlot> a : equipmentSlots.entrySet()) {
            EquipmentSlotDTO tmp = new EquipmentSlotDTO();

            tmp.setSlotEnum(a.getKey());
            if (a.getValue().getItem() != null)
                tmp.setItemID(a.getValue().getItem().getId());
            else tmp.setItemID(null);

            map.put(a.getKey(), tmp);

        }

        return map;
    }


    /// _______________________ Reverse Methoden __________________________________
    ///                 Ertelle Models aus DTO


}
