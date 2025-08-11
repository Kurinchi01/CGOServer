package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.RPG.ItemDTO;
import com.Kuri01.Game.Server.DTO.RPG.PlayerDTO;
import com.Kuri01.Game.Server.DTO.RPG.PlayerWalletDTO;
import com.Kuri01.Game.Server.DTOMapper.InventoryMapper;
import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.ItemRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final InventoryMapper inventoryMapper;

    public PlayerService(PlayerRepository playerRepository, ItemRepository itemRepository, InventoryMapper inventoryMapper) {
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
        this.inventoryMapper = inventoryMapper;
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
        playerDTO.setExperiencePoints(player.getExperiencePoints());
        playerDTO.setMaxHp(player.getMaxHp());
        playerDTO.setAttack(player.getAttack());

        //setze gekapseltewerte
        playerDTO.setEquipmentDTO(inventoryMapper.createDTOFromEquipment(player.getEquipment()));
        playerDTO.setInventoryDTO(inventoryMapper.createDTOFromInventory(player.getInventory()));
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

        //maps
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

}
