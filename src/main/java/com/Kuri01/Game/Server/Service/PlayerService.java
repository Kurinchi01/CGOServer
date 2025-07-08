package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.DTO.EquipmentDTO;
import com.Kuri01.Game.Server.Model.RPG.DTO.ItemDTO;
import com.Kuri01.Game.Server.Model.RPG.DTO.PlayerDTO;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentItem;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
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
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setLevel(player.getLevel());
        dto.setExperiencePoints(player.getExperiencePoints());
        dto.setMaxHp(player.getMaxHp());
        dto.setAttack(player.getAttack());

        // Hier greifen wir auf die lazy Sammlung zu - das ist sicher, weil die Session noch offen ist.
        List<ItemDTO> inventoryItems = player.getInventory().getSlots().stream()
                .map(slot -> mapToItemDTO(slot.getItem()))
                .collect(Collectors.toList());
        dto.setInventoryItems(inventoryItems);
        dto.setEquipment(player.getEquipment());
        return dto; // Das DTO-Objekt ist jetzt "getrennt" von der DB und sicher zu versenden.
    }



    private ItemDTO mapToItemDTO(Item item) {
        ItemDTO tmp = new ItemDTO();

        tmp.setId(item.getId());
        tmp.setName(item.getName());
        tmp.setDescription(item.getDescription());
        tmp.setRarity(item.getRarity());
        if(item instanceof EquipmentItem)
        {
            tmp.setEquipmentSlot(((EquipmentItem) item).getEquipmentSlot());
            tmp.setStats(((EquipmentItem) item).getStats());
        }

        return tmp;
    }
}
